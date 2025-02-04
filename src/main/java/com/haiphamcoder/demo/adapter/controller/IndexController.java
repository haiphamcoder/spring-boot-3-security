package com.haiphamcoder.demo.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haiphamcoder.demo.shared.ApiResponseFactory;

@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok().body(ApiResponseFactory.createSuccessResponse(new String("Hello, world!")));
    }
}
