package com.example.segundoparcialweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearValidacionRequest {
    private String email;
    private String documento;

}
