package com.spring.ecommercesystem.services;

import com.spring.ecommercesystem.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String email =  oAuth2User.getAttribute("email");
//
//        //Get User from DB
//        User user = this.userService.findByEmail(email);
//        if (user == null){
//            throw new OAuth2AuthenticationException("User is not found in the DB!!!");
//        }
//
//
//
//    }
}
