package com.example.segundoparcialweb.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidacionRequestDTO {
    private String email;
    private String documento;
}