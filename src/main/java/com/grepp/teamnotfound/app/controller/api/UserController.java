package com.grepp.teamnotfound.app.controller.api;

import com.grepp.teamnotfound.app.model.auth.AuthService;
import com.grepp.teamnotfound.app.model.auth.payload.LoginRequest;
import com.grepp.teamnotfound.app.model.auth.payload.TokenResponse;
import com.grepp.teamnotfound.app.model.auth.token.dto.TokenDto;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterResponseDto;
import com.grepp.teamnotfound.infra.auth.token.JwtProvider;
import com.grepp.teamnotfound.infra.auth.token.TokenCookieFactory;
import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import com.grepp.teamnotfound.infra.auth.token.code.TokenType;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        Long userId = userService.registerUser(requestDto);

        RegisterResponseDto responseDto = new RegisterResponseDto(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        String accessToken = jwtProvider.resolveToken(request, TokenType.ACCESS_TOKEN);
        if (accessToken != null) {
            authService.logout(accessToken);
        }

        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }


}
