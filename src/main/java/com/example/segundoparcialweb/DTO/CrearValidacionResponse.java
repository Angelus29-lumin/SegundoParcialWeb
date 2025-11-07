package com.example.segundoparcialweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearValidacionResponse {
    private String token;
    private String codigo;

    }
