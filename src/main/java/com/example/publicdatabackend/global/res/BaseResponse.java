package com.example.publicdatabackend.global.res;

import com.example.publicdatabackend.global.res.constant.ResponseMessageConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse {
    private Integer status;
    private String message;
    public BaseResponse(){
        this.message  = ResponseMessageConstant.SUCCESS;
        this.status = 200;
    }
}
