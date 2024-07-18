package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfRestController {
    private final UserService userService;

    @Autowired
    public CsrfRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-csrf-token")
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }


    @GetMapping("/api/get-authentication")
    public ResponseEntity<User> getAuthentication(){
        User user = this.userService.getCurrentUser();
        if (user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
