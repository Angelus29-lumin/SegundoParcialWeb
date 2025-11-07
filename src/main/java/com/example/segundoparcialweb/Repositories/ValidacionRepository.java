package com.example.segundoparcialweb.Repositories;

import com.example.segundoparcialweb.Models.Validacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidacionRepository extends JpaRepository<Validacion,Long> {
    Optional<Validacion> findByToken(String token);
    Optional<Validacion> findByCodigo(String codigo);
}
