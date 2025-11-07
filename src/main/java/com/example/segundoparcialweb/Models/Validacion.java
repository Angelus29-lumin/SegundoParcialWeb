package com.example.segundoparcialweb.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table
@AllArgsConstructor
@Builder
public class Validacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 10)
    private String documento;

    private LocalDateTime fecha;

    @Column(length = 20)
    private String estado; // Pendiente o Validada

    @Column(length = 100)
    private String token; // único

    @Column(length = 10)
    private String codigo; // único

    private LocalDateTime creadoEn;

    public Validacion() {}

    @PrePersist
    public void prePersist() {
        creadoEn = LocalDateTime.now();
    }

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
