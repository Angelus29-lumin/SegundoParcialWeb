package com.example.segundoparcialweb.Repository;

import com.example.segundoparcialweb.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByDocumento(String documento);

    @Query("SELECT COUNT(p) > 0 FROM Persona p WHERE p.email = :email OR p.telefono = :telefono")
    boolean existsByEmailOrTelefono(@Param("email") String email, @Param("telefono") String telefono);
}