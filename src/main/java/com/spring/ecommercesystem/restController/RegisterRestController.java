package com.spring.ecommercesystem.restController;

import com.spring.ecommercesystem.entities.Role;
import com.spring.ecommercesystem.entities.User;
import com.spring.ecommercesystem.services.RoleService;
import com.spring.ecommercesystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegisterRestController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegisterRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<Role>> getUserAuthentication(){
        List<Role> roles = this.roleService.findAll();
        return ResponseEntity.ok(roles);
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();

        if (user == null){
            response.put("message", "User object is null!!!");
            return ResponseEntity.badRequest().body(response);
        }

        String email = user.getEmail();
        User userExisted = this.userService.findByEmail(email);
        if (userExisted != null){
            response.put("message", "Email already exists!, Please enter another email");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        this.userService.saveAndUpdate(user);

        response.put("message", "Account registered successfully, Please sign in with this account");
        response.put("user", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
