package ru.poly.studentstestingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.poly.studentstestingsystem.entity.User;
import ru.poly.studentstestingsystem.pojo.SignUpRequest;
import ru.poly.studentstestingsystem.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(user);
    }
}
