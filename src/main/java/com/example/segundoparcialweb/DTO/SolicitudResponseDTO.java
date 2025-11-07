package com.example.segundoparcialweb.DTO;
import java.time.LocalDate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudResponseDTO {
    private Integer id;
    private LocalDate fecha;
    private String codigoRadicado;

}