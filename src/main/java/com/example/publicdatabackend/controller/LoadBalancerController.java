package com.example.publicdatabackend.controller;

import com.example.publicdatabackend.global.res.DataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadBalancerController {
    @GetMapping("/healthCheck")
    public ResponseEntity<DataResponse> healthCheck() {
        return ResponseEntity.ok().body(new DataResponse("OK"));
    }
}
