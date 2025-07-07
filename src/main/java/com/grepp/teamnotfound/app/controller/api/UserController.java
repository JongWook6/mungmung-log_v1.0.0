package com.grepp.teamnotfound.app.controller.api;

import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.user.dto.LoginRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        Long userId = userService.registerUser(requestDto);

        RegisterResponseDto responseDto = new RegisterResponseDto(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        userService.login(requestDto);

        //TODO jwt

        return ResponseEntity.ok("User logged in successfully");
    }

}
