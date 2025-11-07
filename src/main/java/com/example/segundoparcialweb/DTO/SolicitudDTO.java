package com.example.segundoparcialweb.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private Integer id;
    private LocalDate fecha;
    private String solicitante;
    private String codeudor;
    private String estado;
    private String codigoRadicado;

}