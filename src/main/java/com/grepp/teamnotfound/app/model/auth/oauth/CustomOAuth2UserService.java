package com.grepp.teamnotfound.app.model.auth.oauth;

import com.grepp.teamnotfound.app.model.auth.code.Role;
import com.grepp.teamnotfound.app.model.auth.oauth.dto.OAuth2UserDto;
import com.grepp.teamnotfound.app.model.auth.oauth.dto.CustomOAuth2User;
import com.grepp.teamnotfound.app.model.user.entity.User;
import com.grepp.teamnotfound.app.model.user.repository.UserRepository;
import com.grepp.teamnotfound.infra.auth.oauth2.user.NaverOAuth2UserInfo;
import com.grepp.teamnotfound.infra.auth.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 리소스가 제공하는 유저 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        // 어디서 주는지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;
        // 각 provider별 로그인 진행
        if (registrationId.equals("naver")){
            oAuth2UserInfo = new NaverOAuth2UserInfo(oAuth2User.getAttributes());

        } else if(registrationId.equals("google")){
            oAuth2UserInfo = new NaverOAuth2UserInfo(oAuth2User.getAttributes());

        } else {
            return null;
        }

        String username = oAuth2UserInfo.getProvider()+" "+oAuth2UserInfo.getProviderId();

        // TODO 기 존재 여부 검증 username -> email
        User existData = userRepository.findByName(username);

        if(existData == null){

            // TODO nickname , email 등 값
            User user = User.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .name(oAuth2UserInfo.getName())
                    .nickname(oAuth2UserInfo.getName())
                    .role(Role.ROLE_USER)
                    .provider(oAuth2UserInfo.getProvider())
                    .build();

            userRepository.save(user);

            OAuth2UserDto userDto = OAuth2UserDto.builder()
                    .username(username)
                    .name(oAuth2UserInfo.getName())
                    // email
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(userDto);

        }
        // 업데이트라는데, 우린 그냥 거절할...
        // TODO
        else {
            return null;
        }


    }
}
