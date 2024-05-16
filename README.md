## [ Spring Batch + WebClient ] 공무원 업무 추진비 데이터 수집 시간 단축
공무원 업무 추진비 데이터를 Spring Batch를 통해 수집하고 있었습니다. <br>
총 데이터의 수는 약 6만 개이며, 한 번 요청할 때 1000개 단위로 요청을 보낼 수 있습니다.
<br>

### 기존 코드
기존의 `tasklet1` 구현에서는 for 루프를 사용하여 데이터를 1000개 단위로 요청했습니다. 각 요청에 대한 응답 데이터를 받아 데이터베이스에 저장하는 작업을 순차적으로 수행했습니다. 작업의 성능을 측정하기 위해 작업 시작 전후의 시간을 측정했습니다.

```
@Bean
public Tasklet tasklet1(PublicDataRepository publicDataRepository) {
    return ((contribution, chunkContext) -> {
        long beforeTime = System.currentTimeMillis(); // 코드 실행 전 시간

        for(int start = 1; start <= 56_000; start += 1000) {
            int end = start + 999;
            end = Math.min(end, 57_000);

            List<PublicDataDto> publicDataDtos
                    = publicDataUtils.getPublicDataAsDtoList(start, end);

            for(PublicDataDto publicDataDto : publicDataDtos) {
                PublicData publicData = PublicData.builder()
                        .deptNm(publicDataDto.getDeptNm())
                        .execDt(publicDataDto.getExecDt())
                        .execLoc(publicDataDto.getExecLoc())
                        .targetNm(publicDataDto.getTargetNm())
                        .execAmount(publicDataDto.getExecAmount())
                        .execMonth(publicDataDto.getExecMonth())
                        .build();

                publicDataRepository.save(publicData);
            }
        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후 시간
        long secDiffTime = (afterTime - beforeTime)/1000; // 코드 실행 전후 시간 차이 계산(초 단위)
        System.out.println("시간차이(s) : " + secDiffTime);

        return RepeatStatus.FINISHED;
    });
}
```

또한, WebClient를 활용하여 **동기 방식**으로 `getPublicDataSync` 메서드에서 API 데이터를 처리했습니다.

```
public JsonNode getPublicDataSync(int start, int end) {

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

    webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB로 설정
            .build();

    String responseBody = webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .scheme("http")
                    .host("openapi.seoul.go.kr")
                    .port(8088)
                    .path("/{KEY}/json/odExpense/{START_INDEX}/{END_INDEX}")
                    .build(authkey, start, end))
            .retrieve()
            .bodyToMono(String.class)
            .block(); // 동기적으로 결과를 얻음
    return parseJson(responseBody);
}
```

코드를 돌려 시간을 측정하면 아래와 같이 약 9분의 시간이 걸렸습니다.

```
Step: [step1] executed in 8m56s512ms
```

<br>

## 성능 개선 방법
WebClient를 사용하여 API 호출을 비동기적으로 수행하고, 결과를 비동기적으로 데이터베이스에 저장했습니다. 이를 통해 I/O 대기 시간을 최소화하고, 전체적인 처리 속도를 향상시킬 수 있었습니다.

### 개선된 코드

```
@Bean
public Tasklet tasklet1(PublicDataRepository publicDataRepository) {
    return (contribution, chunkContext) -> {
        int totalRecords = 57000;
        int chunkSize = 1000;
        List<Mono<Void>> tasks = new ArrayList<>();

        for (int start = 1; start <= totalRecords; start += chunkSize) {
            int end = Math.min(start + chunkSize - 1, totalRecords);
            tasks.add(processChunk(start, end, publicDataRepository));
        }

        // 모든 비동기 작업이 완료될 때까지 대기
        Mono.when(tasks).block();
        return RepeatStatus.FINISHED;
    };
}
```

#### 가장 중요한 부분은 Mono<Void>의 List에 `tasks`를 넣자마자 `getPublicDataAsDtoListAsyn` 메서드에서 이를 비동기로 처리한다는 것입니다.

```
public Mono<List<PublicDataDto>> getPublicDataAsDtoListAsync(int start, int end) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/{KEY}/json/odExpense/{START_INDEX}/{END_INDEX}")
                    .build(authkey, start, end))
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(this::parseJsonToDtoList);
}
```

이 작업을 시각화하면 아래와 같습니다.

```
[ Tasklet 시작 ]
       ↓
[ 변수 초기화 (totalRecords, chunkSize) ]
       ↓
[ 비동기 작업 스케줄링 (for 루프) ]
       ↓
  [ 데이터 청크별 비동기 처리 (processChunk) ]
       ↓
    [ 데이터 변환 및 DB 저장 (savePublicData) ]
       ↓
[ 모든 작업 완료 대기 (Mono.when(tasks).block()) ]
       ↓
[ Tasklet 완료 (RepeatStatus.FINISHED 반환) ]
```

코드를 돌렸을 때, 나오는 총 시간 약 2분이었습니다.

```
Step: [step1] executed in 1m50s830ms
```

결론적으로, 비동기 방식을 통해 기존의 9분보다 **약 7분 단축**할 수 있었습니다.
