package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.mail.MailService;
import com.grepp.teamnotfound.app.model.auth.token.RefreshTokenService;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.auth.token.JwtProvider;
import com.grepp.teamnotfound.infra.error.exception.AuthException;
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
    private final MailService mailService;



    // 요청
    @Transactional
    public void requestRegisterVerification(RegisterRequestDto requestDto) {
        // 1. 이메일 중복 확인
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new BusinessException(UserErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        // 2. 닉네임 중복 확인
        if(userRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new BusinessException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }

        // 3. 사용자 정보 임시 저장
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName()) // 이름 필드 추가
                .nickname(requestDto.getNickname())
                .password(encodedPassword)
                .role(Role.ROLE_USER) // 기본 역할 설정
                .provider("local") // 로컬 가입
                .verifiedEmail(false) // 이메일 미인증 상태로 저장
                .build();

        userRepository.save(newUser);

        // 4. 인증 이메일 발송 및 코드 Redis 저장
        mailService.sendVerificationEmail(requestDto.getEmail());

    }

    // 인증 코드 검증 및 최종 회원가입
    @Transactional
    public Long completeRegistration(String email, String verificationCode) {
        // 1. 인증 코드 검증
        mailService.verifyEmailCode(email, verificationCode);

        // 2. 사용자 인증 완료
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(UserErrorCode.USER_NOT_FOUND));

        if (user.getVerifiedEmail()) {
            throw new AuthException(UserErrorCode.EMAIL_ALREADY_VERIFIED);
        }

        user.setVerifiedEmail(true);
        userRepository.save(user);

        return user.getUserId();
    }

}
