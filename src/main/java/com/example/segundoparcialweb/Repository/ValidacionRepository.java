package com.example.segundoparcialweb.Repository;

import com.example.segundoparcialweb.Models.Validacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ValidacionRepository extends JpaRepository<Validacion, Integer> {
    Optional<Validacion> findByToken(String token);
    Optional<Validacion> findByEmailAndEstado(String email, String estado);

    @Query("SELECT v FROM Validacion v WHERE v.token = :token AND v.estado = 'Pendiente' AND v.fecha >= :fechaLimite")
    Optional<Validacion> findValidToken(@Param("token") String token, @Param("fechaLimite") LocalDateTime fechaLimite);
}