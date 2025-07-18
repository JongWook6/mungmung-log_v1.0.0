package com.grepp.teamnotfound.infra.auth.oauth2.user;

public interface OAuth2UserInfo {


    // 제공자가 발급하는 id
    String getProviderId();
    // 제공자(naver, google, ...)
    String getProvider();
    // 사용자 실명
    String getName();
    // 사용자 이메일
    String getEmail();


}
