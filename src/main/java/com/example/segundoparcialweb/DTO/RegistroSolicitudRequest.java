package com.example.segundoparcialweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroSolicitudRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonaIn {
        private String documento;
        private String nombre;
        private String email;
        private String telefono;
        private String fecha_nacimiento; // "YYYY-MM-DD"
    }

    private PersonaIn solicitante;
    private PersonaIn codeudor;
    private String observacion;
    private Long valor;

}
