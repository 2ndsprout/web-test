package com.example.ms1.note.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 구글로 부터 userRequest 데이터에 대한 후처리 되는 함수
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId 로 어떤 OAuth로 로그인했는지 확인 가능
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        // google 로그인 버튼 클릭-> google 로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> google 로 부터 회원프로필을 받아줌
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
