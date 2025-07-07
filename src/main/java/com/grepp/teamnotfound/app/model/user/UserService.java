package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.dto.TokenResponseDto;
import com.grepp.teamnotfound.app.model.auth.token.RefreshTokenService;
import com.grepp.teamnotfound.app.model.user.dto.LoginRequestDto;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.auth.token.JwtProvider;
import com.grepp.teamnotfound.infra.error.exception.BusinessException;
import com.grepp.teamnotfound.infra.error.exception.code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public Long registerUser(RegisterRequestDto requestDto) {

        // 중복 확인
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new BusinessException(UserErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        if(userRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new BusinessException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .password(encodedPassword)
                .role(Role.ROLE_USER)
                .provider("local")
                .build();

        User savedUser = userRepository.save(user);

        return savedUser.getUserId();
    }


    @Transactional(readOnly = true)
    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        // 비밀번호 확인
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(UserErrorCode.USER_LOGIN_FAILED);
        }

        // token 생성
        TokenResponseDto responseDto = jwtProvider.createToken(user.getUserId(), user.getRole());

        // rfToken 저장
        refreshTokenService.saveRefreshToken(
                String.valueOf(user.getUserId()),
                responseDto.getRefreshToken(),
                jwtProvider.getRtExpiration()
        );

        // TODO jwt
        return responseDto;
    }
}
