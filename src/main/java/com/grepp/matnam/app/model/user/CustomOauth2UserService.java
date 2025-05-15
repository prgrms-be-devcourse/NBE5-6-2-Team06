package com.grepp.matnam.app.model.user;

import com.grepp.matnam.app.model.user.dto.CustomOAuth2User;
import com.grepp.matnam.app.model.user.dto.GoogleResponse;
import com.grepp.matnam.app.model.user.dto.OAuth2Response;
import com.grepp.matnam.app.model.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 사용자 로드: {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String provider = oAuth2Response.getProvider();
        String providerId = oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();
        String name = oAuth2Response.getName();

        String userId = provider + "_" + providerId;

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setUsername(userId);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setRole("ROLE_USER");
        userDto.setProvider(provider);
        userDto.setProviderId(providerId);

        return new CustomOAuth2User(userDto);
    }
}