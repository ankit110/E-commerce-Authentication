package com.userAuthentication.service;

import com.userAuthentication.dto.ProductResponseDto;
import com.userAuthentication.model.UserAuthEntity;
import com.userAuthentication.repository.ProductFeignClient;
import com.userAuthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductFeignClient client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthEntity userAuthEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(userAuthEntity.getUsername(), userAuthEntity.getPassword(),
            new ArrayList<>());
    }


    public List<ProductResponseDto> getAllProduct(String token) {
        return client.getAllProducts(token).getBody();
    }

//    public ProductResponseDto addProduct(String token) {
//
//    }
}
