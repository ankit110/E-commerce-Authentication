package com.userAuthentication.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rating_id;

    private int stars;
    private String review;

    @ManyToOne
    @JoinColumn(name = "id")
    private ProductRequestDto product;
}
