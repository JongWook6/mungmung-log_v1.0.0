package com.grepp.teamnotfound.app.model.user;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.user.dto.RegisterRequestDto;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
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
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long registerAdmin(RegisterRequestDto requestDto) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new BusinessException(UserErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        // 닉네임 중복 확인
        if (userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new BusinessException(UserErrorCode.USER_NICKNAME_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User adminUser = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .password(encodedPassword)
                .role(Role.ROLE_ADMIN)
                .provider("local")
                .verifiedEmail(true) // 관리자는 이메일 인증 X
                .build();

        userRepository.save(adminUser);

        return adminUser.getUserId();
    }
}
