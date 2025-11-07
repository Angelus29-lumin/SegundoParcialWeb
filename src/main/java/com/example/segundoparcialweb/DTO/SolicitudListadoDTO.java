package com.example.segundoparcialweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudListadoDTO {
    private Long id;
    private String fecha;
    private String solicitante;
    private String codeudor;
    private String estado;
    private String codigo_radicado;
}
