package com.userAuthentication.controller;

import com.userAuthentication.model.AuthRequest;
import com.userAuthentication.model.UserAuthEntity;
import com.userAuthentication.repository.UserRepository;
import com.userAuthentication.util.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserAuthEntity userAuthEntity) {
        userAuthEntity.setPassword(passwordEncoder.encode(userAuthEntity.getPassword()));
        userRepository.save(userAuthEntity);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            e.printStackTrace();
        }
        final String token = jwtUtil.generateToken(authRequest);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserAuthEntity> getUserProfile(@PathVariable Long userId) {
        UserAuthEntity userAuthEntity = userRepository.findById(userId)
            .orElseThrow(() -> new SignatureException("User not found"));
        return ResponseEntity.ok(userAuthEntity);
    }
}
