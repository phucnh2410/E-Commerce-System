package com.spring.ecommercesystem.securities;

import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.RoleService;
import com.spring.ecommercesystem.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email").toString();
        String fullName = oAuth2User.getAttribute("name").toString();
        String picture = oAuth2User.getAttribute("picture").toString();

        User existingUser = this.userService.findByEmail(email);
        List<GrantedAuthority> authorities = new ArrayList<>(oAuth2User.getAuthorities());

        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            //Don't need set password, because system will authenticate by GG token
            newUser.setAvatar(picture);
            newUser.setRole(this.roleService.findById(2L));

            this.userService.saveAndUpdate(newUser);

            //If the user authenticated by GG but user is not found in the DB, Updating the role and permission for New User in the Security Context
            authorities.add(new SimpleGrantedAuthority(newUser.getRole().getName()));
        }else {
            //If the user authenticated by GG and existing in DB, Updating the role and permission for existing user in the Security context
            authorities.add(new SimpleGrantedAuthority(existingUser.getRole().getName()));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(oAuth2User, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
    }
}




















