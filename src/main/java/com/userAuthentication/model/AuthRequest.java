package com.userAuthentication.model;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
