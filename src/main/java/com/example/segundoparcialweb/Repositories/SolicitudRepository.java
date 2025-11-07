package com.example.segundoparcialweb.Repositories;

import com.example.segundoparcialweb.Models.Persona;
import com.example.segundoparcialweb.Models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findBySolicitante(Persona solicitante);
    List<Solicitud> findBySolicitanteAndEstadoDescripcion(Persona solicitante, String estadoDescripcion);
}
