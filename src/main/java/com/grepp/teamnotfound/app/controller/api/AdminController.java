package com.grepp.teamnotfound.app.controller.api;

import com.grepp.teamnotfound.app.model.auth.AuthService;
import com.grepp.teamnotfound.app.model.auth.payload.LoginRequest;
import com.grepp.teamnotfound.app.model.auth.payload.TokenResponse;
import com.grepp.teamnotfound.app.model.auth.token.dto.TokenDto;
import com.grepp.teamnotfound.app.model.user.AdminService;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterResponseDto;
import com.grepp.teamnotfound.infra.auth.token.TokenCookieFactory;
import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import com.grepp.teamnotfound.infra.auth.token.code.TokenType;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> registerAdmin(@RequestBody RegisterRequestDto requestDto) {
        Long userId = adminService.registerAdmin(requestDto);
        RegisterResponseDto responseDto = new RegisterResponseDto(userId);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        TokenDto dto = authService.login(request);
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(TokenType.ACCESS_TOKEN.name(),
                dto.getAccessToken(), dto.getAtExpiresIn());
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(TokenType.REFRESH_TOKEN.name(),
                dto.getRefreshToken(), dto.getRtExpiresIn());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(dto.getAccessToken())
                .expiresIn(dto.getAtExpiresIn())
                .grantType(GrantType.BEARER)
                .refreshToken(dto.getRefreshToken())
                .build();
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));

    }
}
