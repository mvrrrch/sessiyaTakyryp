
package com.app.todoapp.controller;


import com.app.todoapp.model.User;
import com.app.todoapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthContrll {

    private final UserService userService;

    public AuthContrll(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
}
