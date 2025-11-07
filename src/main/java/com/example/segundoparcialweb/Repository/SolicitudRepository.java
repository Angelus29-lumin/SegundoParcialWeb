package com.example.segundoparcialweb.Repository;

import com.example.segundoparcialweb.Models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    @Query("SELECT s FROM Solicitud s JOIN FETCH s.solicitante JOIN FETCH s.codeudor JOIN FETCH s.estado")
    List<Solicitud> findAllWithDetails();

    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.documento = :documento AND s.estado.descripcion IN ('Solicitud', 'Aprobada')")
    List<Solicitud> findActiveSolicitudesBySolicitante(@Param("documento") String documento);

    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.documento = :documento AND s.estado.descripcion = 'Rechazada'")
    Optional<Solicitud> findRechazadaSolicitudBySolicitante(@Param("documento") String documento);
}