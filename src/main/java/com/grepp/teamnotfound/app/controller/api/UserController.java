package com.grepp.teamnotfound.app.controller.api;

import com.grepp.teamnotfound.app.model.auth.AuthService;
import com.grepp.teamnotfound.app.model.auth.payload.LoginRequest;
import com.grepp.teamnotfound.app.model.auth.payload.TokenResponse;
import com.grepp.teamnotfound.app.model.auth.token.dto.TokenDto;
import com.grepp.teamnotfound.app.model.user.AdminService;
import com.grepp.teamnotfound.app.model.user.UserService;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterResponseDto;
import com.grepp.teamnotfound.app.model.user.dto.VerifyEmailRequestDto;
import com.grepp.teamnotfound.infra.auth.token.TokenCookieFactory;
import com.grepp.teamnotfound.infra.auth.token.code.GrantType;
import com.grepp.teamnotfound.infra.auth.token.code.TokenType;
import com.grepp.teamnotfound.infra.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final AdminService adminService;

    // 이메일 발송 완료 시점까지 작동
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequestDto requestDto) {
        userService.requestRegisterVerification(requestDto);

        return ResponseEntity.ok(ApiResponse.success("회원가입 인증 메일이 발송되었습니다. 이메일을 확인해주세요."));
    }

    // 입력 인증코드 유효성 검증 및 가입 완료
    @PostMapping("/register/verify-email")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> verifyEmail(@RequestBody VerifyEmailRequestDto requestDto){
        Long userId = userService.completeRegistration(requestDto.getEmail(), requestDto.getVerificationCode());

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


    @PostMapping("/admin/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> registerAdmin(@RequestBody RegisterRequestDto requestDto) {
        Long userId = adminService.registerAdmin(requestDto);
        RegisterResponseDto responseDto = new RegisterResponseDto(userId);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }


    @PostMapping("/admin/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<TokenResponse>> adminLogin(@RequestBody LoginRequest request, HttpServletResponse response) {

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
