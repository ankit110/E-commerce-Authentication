package com.userAuthentication.controller;

import com.userAuthentication.dto.ProductResponseDto;
import com.userAuthentication.exception.CmsResponseStatusException;
import com.userAuthentication.model.AuthRequest;
import com.userAuthentication.model.UserAuthEntity;
import com.userAuthentication.repository.UserRepository;
import com.userAuthentication.service.UserDetailsServiceImpl;
import com.userAuthentication.util.JwtUtil;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private UserDetailsServiceImpl service;

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
            throw new CmsResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Username or password", HttpStatus.BAD_REQUEST.toString());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//            e.printStackTrace();
        }
        final String token = jwtUtil.generateToken(authRequest);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductResponseDto>> getUserProfile(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));

        UserAuthEntity userAuthEntity = userRepository.findById(userId)
            .orElseThrow(() -> new CmsResponseStatusException(HttpStatus.BAD_REQUEST, "User Not Found", HttpStatus.BAD_REQUEST.toString()));

        if (!userAuthEntity.getUsername().equals(usernameFromToken)) {
            throw new CmsResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access", HttpStatus.UNAUTHORIZED.toString());
        }


        return ResponseEntity.ok(service.getAllProduct());
    }
}
