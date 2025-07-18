package com.grepp.teamnotfound.app.model.auth.oauth;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.oauth.dto.CustomOAuth2UserDto;
import com.grepp.teamnotfound.app.model.auth.oauth.dto.OAuth2UserDto;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.auth.oauth2.user.GoogleOAuth2UserInfo;
import com.grepp.teamnotfound.infra.auth.oauth2.user.NaverOAuth2UserInfo;
import com.grepp.teamnotfound.infra.auth.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 리소스가 제공하는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("1️⃣ 리소스가 제공하는 유저 정보 - oAuth2User: {}", oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo;

        // 각 provider별 Info 객체 제작
        if (registrationId.equals("naver")) {
            oAuth2UserInfo = new NaverOAuth2UserInfo(oAuth2User.getAttributes());

        } else if (registrationId.equals("google")) {
            oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());

        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 provider : " + userRequest.getClientRegistration().getProviderDetails());
        }

        String userEmail = oAuth2UserInfo.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user;
        if (optionalUser.isEmpty()) {
            log.info("2️⃣-1️⃣ 신규 생성되는 유저 email - oAuth2UserInfo.getEmail: {}", oAuth2UserInfo.getEmail());
            user = User.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .name(oAuth2UserInfo.getName())
                    // TODO nickname 받는 로직 재설계 필요
                    .nickname(oAuth2UserInfo.getName())
                    .role(Role.ROLE_USER)
                    .provider(oAuth2UserInfo.getProvider())
                    .build();

            userRepository.save(user);

        } else if (Objects.equals(optionalUser.get().getProvider(), oAuth2UserInfo.getProvider())) {
            log.info("2️⃣-2️⃣ 기존에 존재하는 동일 공급자 email - oAuth2UserInfo.getEmail: {}", oAuth2UserInfo.getEmail());
            user = optionalUser.get();
        } else {
            log.info("2️⃣-3️⃣ 기존에 존재하는 다른 공급자 email - oAuth2UserInfo.getEmail: {}", oAuth2UserInfo.getEmail());
            throw new OAuth2AuthenticationException("다른 provider로 가입된 이메일: " + oAuth2UserInfo.getEmail());
        }

        // 인증 객체 생성을 위한 dto
        OAuth2UserDto userDto = OAuth2UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .userId(user.getUserId())
                .role("ROLE_USER")
                .build();

        return new CustomOAuth2UserDto(userDto);

    }
}