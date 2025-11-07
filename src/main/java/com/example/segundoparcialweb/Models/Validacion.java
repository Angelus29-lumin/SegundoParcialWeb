package com.example.segundoparcialweb.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "validacion")
public class Validacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "documento", length = 10, nullable = false)
    private String documento;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "token", length = 100, unique = true, nullable = false)
    private String token;

    @Column(name = "codigo", length = 10, unique = true, nullable = false)
    private String codigo;

    public Validacion(String email, String documento, LocalDateTime fecha,
                      String estado, String token, String codigo) {
        this.email = email;
        this.documento = documento;
        this.fecha = fecha;
        this.estado = estado;
        this.token = token;
        this.codigo = codigo;
    }
}