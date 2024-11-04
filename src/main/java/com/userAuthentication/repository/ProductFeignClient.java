package com.userAuthentication.repository;

import com.userAuthentication.config.FeignConfig;
import com.userAuthentication.dto.ProductRequestDto;
import com.userAuthentication.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Configuration
@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:9091/api/v1", configuration = FeignConfig.class)
public interface ProductFeignClient {

    @GetMapping("/products")
    ResponseEntity<List<ProductResponseDto>> getAllProducts(@RequestHeader("Authorization") String token);

}
