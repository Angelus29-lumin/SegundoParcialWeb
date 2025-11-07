package com.example.segundoparcialweb.DTO;

import java.time.LocalDate;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudRequestDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonaDTO {
        private String documento;
        private String nombre;
        private String email;
        private String telefono;
        private LocalDate fechaNacimiento;

    }

    private PersonaDTO solicitante;
    private PersonaDTO codeudor;
    private String observacion;


}