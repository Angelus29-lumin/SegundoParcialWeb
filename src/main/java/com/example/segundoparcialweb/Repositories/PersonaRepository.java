package com.example.segundoparcialweb.Repositories;

import com.example.segundoparcialweb.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona,Long> {
    Optional<Persona> findByDocumento(String documento);
    Optional<Persona> findByEmail(String email);
}
