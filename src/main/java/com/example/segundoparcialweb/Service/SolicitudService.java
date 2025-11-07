package com.example.segundoparcialweb.Service;

import com.example.segundoparcialweb.DTO.RegistroSolicitudRequest;
import com.example.segundoparcialweb.DTO.SolicitudListadoDTO;
import com.example.segundoparcialweb.Exception.ApiException;
import com.example.segundoparcialweb.Models.Estado;
import com.example.segundoparcialweb.Models.Persona;
import com.example.segundoparcialweb.Models.Solicitud;
import com.example.segundoparcialweb.Repositories.EstadoRepository;
import com.example.segundoparcialweb.Repositories.PersonaRepository;
import com.example.segundoparcialweb.Repositories.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private  PersonaRepository personaRepository;
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private  ValidacionService validacionService;


    public List<SolicitudListadoDTO> listarSolicitudes() {
        List<Solicitud> all = solicitudRepository.findAll();
        return all.stream().map(this::toListadoDTO).collect(Collectors.toList());
    }

    private SolicitudListadoDTO toListadoDTO(Solicitud s) {
        SolicitudListadoDTO dto = new SolicitudListadoDTO();
        dto.setId(s.getId());
        dto.setFecha(s.getFecha().toString());
        dto.setSolicitante(s.getSolicitante() != null ? s.getSolicitante().getNombre() : null);
        dto.setCodeudor(s.getCodeudor() != null ? s.getCodeudor().getNombre() : null);
        dto.setEstado(s.getEstado() != null ? s.getEstado().getDescripcion() : null);
        dto.setCodigo_radicado(s.getCodigoRadicado());
        return dto;
    }

    public Solicitud registrarSolicitud(RegistroSolicitudRequest req) {
        // Validaciones según enunciado
        RegistroSolicitudRequest.PersonaIn solIn = req.getSolicitante();
        RegistroSolicitudRequest.PersonaIn codeuIn = req.getCodeudor();
        if (solIn == null || codeuIn == null) {
            throw new ApiException("Solicitante y codeudor son requeridos");
        }

        if (solIn.getDocumento().equalsIgnoreCase(codeuIn.getDocumento())) {
            throw new ApiException("El codeudor y solicitante deben ser distintos (documento).");
        }

        if (solIn.getEmail().equalsIgnoreCase(codeuIn.getEmail())
                || solIn.getTelefono().equalsIgnoreCase(codeuIn.getTelefono())) {
            throw new ApiException("Solicitante y codeudor no pueden compartir email o teléfono.");
        }

        // validar que correo del solicitante esté validado previamente
        if (!validacionService.isEmailValidado(solIn.getEmail())) {
            throw new ApiException("El correo del solicitante no está validado.");
        }

        // buscar persona solicitante o crear
        Persona solicitante = personaRepository.findByDocumento(solIn.getDocumento())
                .orElseGet(() -> {
                    Persona p = new Persona();
                    p.setDocumento(solIn.getDocumento());
                    p.setNombre(solIn.getNombre());
                    p.setEmail(solIn.getEmail());
                    p.setTelefono(solIn.getTelefono());
                    if (solIn.getFecha_nacimiento() != null) p.setFechaNacimiento(LocalDate.parse(solIn.getFecha_nacimiento()));
                    return personaRepository.save(p);
                });

        Persona codeudor = personaRepository.findByDocumento(codeuIn.getDocumento())
                .orElseGet(() -> {
                    Persona p = new Persona();
                    p.setDocumento(codeuIn.getDocumento());
                    p.setNombre(codeuIn.getNombre());
                    p.setEmail(codeuIn.getEmail());
                    p.setTelefono(codeuIn.getTelefono());
                    if (codeuIn.getFecha_nacimiento() != null) p.setFechaNacimiento(LocalDate.parse(codeuIn.getFecha_nacimiento()));
                    return personaRepository.save(p);
                });

        // verificar si solicitante ya tiene solicitud aprobada o en estado "Solicitud"
        List<Solicitud> porSolicitante = solicitudRepository.findBySolicitante(solicitante);
        boolean tieneAprobada = porSolicitante.stream()
                .anyMatch(s -> s.getEstado() != null && "Aprobada".equalsIgnoreCase(s.getEstado().getDescripcion()));
        if (tieneAprobada) {
            throw new ApiException("El solicitante ya tiene una solicitud Aprobada.");
        }
        boolean tieneSolicitudPendiente = porSolicitante.stream()
                .anyMatch(s -> s.getEstado() != null && "Solicitud".equalsIgnoreCase(s.getEstado().getDescripcion()));
        if (tieneSolicitudPendiente) {
            throw new ApiException("El solicitante ya tiene una solicitud en estado Solicitud.");
        }

        // si existió solicitud Rechazada -> registrar como Rechazada (según enunciado: "Si tiene una solicitud Rechazada se debe registrar la solicitud en estado rechazado.")
        boolean tuvoRechazada = porSolicitante.stream()
                .anyMatch(s -> s.getEstado() != null && "Rechazada".equalsIgnoreCase(s.getEstado().getDescripcion()));
        Estado estado;
        if (tuvoRechazada) {
            estado = estadoRepository.findByDescripcion("Rechazada").orElseThrow(() -> new ApiException("Estado Rechazada no existe"));
        } else {
            estado = estadoRepository.findByDescripcion("Solicitud").orElseThrow(() -> new ApiException("Estado Solicitud no existe"));
        }

        Solicitud s = new Solicitud();
        s.setFecha(LocalDate.now());
        s.setSolicitante(solicitante);
        s.setCodeudor(codeudor);
        s.setValor(req.getValor() != null ? java.math.BigDecimal.valueOf(req.getValor()) : java.math.BigDecimal.ZERO);
        s.setEstado(estado);
        // generar codigo_radidado (ej: UUID truncated)
        s.setCodigoRadicado(UUID.randomUUID().toString().substring(0, 8));
        s.setObservacion(req.getObservacion());

        solicitudRepository.save(s);

        return s;
    }

    public Solicitud getById(Long id) {
        return solicitudRepository.findById(id).orElse(null);
    }
}
