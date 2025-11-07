package com.example.segundoparcialweb.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Persona solicitante;

    @ManyToOne
    @JoinColumn(name = "codeudor_id", nullable = false)
    private Persona codeudor;

    @Column(name = "valor", precision = 10, scale = 0)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "codigo_radicado", length = 10, unique = true)
    private String codigoRadicado;

}