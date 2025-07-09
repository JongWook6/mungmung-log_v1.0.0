package com.grepp.teamnotfound.app.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {


    @PostMapping("/v1/auth")
    public ResponseEntity<String> logInTest(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok("인증된 사용자만 접근 가능");
    }

}
