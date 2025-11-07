package com.example.segundoparcialweb.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Validacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String documento;
    private LocalDate fecha;
    private String estado; // Pendiente o Validada
    @Column(unique = true)
    private String token;
    @Column(unique = true)
    private String codigo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Validacion)) return false;
        Validacion that = (Validacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
