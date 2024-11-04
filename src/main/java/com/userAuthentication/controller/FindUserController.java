package com.userAuthentication.controller;

import com.userAuthentication.exception.CmsResponseStatusException;
import com.userAuthentication.model.UserAuthEntity;
import com.userAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/findUsers")
public class FindUserController {

    @Autowired
    UserRepository userRepository;
    @GetMapping
    public ResponseEntity<UserAuthEntity> getUserByUsername(@PathVariable String username) {
        UserAuthEntity userAuthEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new CmsResponseStatusException(HttpStatus.NOT_FOUND, "User not found", "User not found"));

        return ResponseEntity.ok(userAuthEntity);
    }
}
