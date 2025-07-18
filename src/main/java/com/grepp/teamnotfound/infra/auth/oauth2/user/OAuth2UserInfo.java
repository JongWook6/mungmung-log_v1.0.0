package com.grepp.teamnotfound.infra.auth.oauth2.user;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {


    // 제공자가 발급하는 id
    String getProviderId();
    // 제공자(naver, google, ...) -> User Entity의 provider
    String getProvider();
    // 사용자 실명 -> User Entity의 name
    String getName();
    // 사용자 이메일 -> User Entity의 email
    String getEmail();


    static OAuth2UserInfo create(String path, OAuth2User user) {
        if(path.equals("/login/oauth2/code/naver"))
            return new NaverOAuth2UserInfo(user.getAttributes());

        if(path.equals("/login/oauth2/code/google"))
            return new GoogleOAuth2UserInfo(user.getAttributes());

        throw new IllegalArgumentException("부적합한 경로 요청 : " + path);
    }

}
