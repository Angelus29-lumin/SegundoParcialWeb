package com.example.segundoparcialweb.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidacionResponseDTO {
    private String token;
    private String codigo;
}