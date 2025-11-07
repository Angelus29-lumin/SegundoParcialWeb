package com.example.segundoparcialweb.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Persona solicitante;

    @ManyToOne
    @JoinColumn(name = "codeudor_id")
    private Persona codeudor;

    @Column(precision = 10, scale = 0)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @Column(length = 10)
    private String codigoRadicado;

    @Column(columnDefinition = "text")
    private String observacion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solicitud)) return false;
        Solicitud solicitud = (Solicitud) o;
        return Objects.equals(id, solicitud.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
