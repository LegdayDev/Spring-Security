package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 구글 로그인 후 후처리가 여기서 일어난다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest.getClientRegistration() = " + userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken() = " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());


        //회원가입 로직
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 회원가입 여부
        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{

        }

        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
