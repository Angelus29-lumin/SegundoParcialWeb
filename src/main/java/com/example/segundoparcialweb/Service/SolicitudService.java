package com.example.segundoparcialweb.Service;
import com.example.segundoparcialweb.DTO.*;
import com.example.segundoparcialweb.Repository.*;
import com.example.segundoparcialweb.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private ValidacionRepository validacionRepository;

    // Servicio 2: Listar solicitudes
    public List<SolicitudDTO> listarSolicitudes() {
        List<Solicitud> solicitudes = solicitudRepository.findAllWithDetails();
        return solicitudes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SolicitudDTO convertToDTO(Solicitud solicitud) {
        return new SolicitudDTO(
                solicitud.getId(),
                solicitud.getFecha(),
                solicitud.getSolicitante().getNombre(),
                solicitud.getCodeudor().getNombre(),
                solicitud.getEstado().getDescripcion(),
                solicitud.getCodigoRadicado()
        );
    }

    // Servicio 3: Crear solicitud de validación de email
    @Transactional
    public ValidacionResponseDTO crearValidacionEmail(ValidacionRequestDTO request) {
        // Validar que el email no esté vacío
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (request.getDocumento() == null || request.getDocumento().trim().isEmpty()) {
            throw new RuntimeException("El documento es obligatorio");
        }

        String token = UUID.randomUUID().toString();
        String codigo = generarCodigo(6);

        Validacion validacion = new Validacion(
                request.getEmail(),
                request.getDocumento(),
                LocalDateTime.now(),
                "Pendiente",
                token,
                codigo
        );

        validacionRepository.save(validacion);

        return new ValidacionResponseDTO(token, codigo);
    }

    // Servicio 4: Registro de solicitud
    @Transactional
    public SolicitudResponseDTO registrarSolicitud(SolicitudRequestDTO request) {
        // Validar que los datos no sean nulos
        if (request.getSolicitante() == null || request.getCodeudor() == null) {
            throw new RuntimeException("Debe proporcionar datos del solicitante y codeudor");
        }

        // Validar que el codeudor y solicitante sean distintos
        if (request.getSolicitante().getDocumento().equals(request.getCodeudor().getDocumento())) {
            throw new RuntimeException("El solicitante y codeudor no pueden ser la misma persona");
        }

        // Validar datos de correspondencia (correo y teléfono)
        if (request.getSolicitante().getEmail().equals(request.getCodeudor().getEmail())) {
            throw new RuntimeException("El solicitante y codeudor no pueden tener el mismo correo");
        }

        if (request.getSolicitante().getTelefono().equals(request.getCodeudor().getTelefono())) {
            throw new RuntimeException("El solicitante y codeudor no pueden tener el mismo teléfono");
        }

        // Validar que el correo del solicitante esté validado
        Optional<Validacion> validacionOpt = validacionRepository.findByEmailAndEstado(
                request.getSolicitante().getEmail(), "Validada");
        if (validacionOpt.isEmpty()) {
            throw new RuntimeException("El correo del solicitante no está validado");
        }

        // Validar que el solicitante no tenga solicitudes activas (Aprobada o Solicitud)
        List<Solicitud> solicitudesActivas = solicitudRepository
                .findActiveSolicitudesBySolicitante(request.getSolicitante().getDocumento());
        if (!solicitudesActivas.isEmpty()) {
            throw new RuntimeException("El solicitante ya tiene una solicitud en estado Solicitud o Aprobada");
        }

        // Buscar o crear personas
        Persona solicitante = personaRepository.findByDocumento(request.getSolicitante().getDocumento())
                .orElse(crearPersona(request.getSolicitante()));

        Persona codeudor = personaRepository.findByDocumento(request.getCodeudor().getDocumento())
                .orElse(crearPersona(request.getCodeudor()));

        // Determinar estado según si tiene solicitudes rechazadas previas
        Optional<Solicitud> solicitudRechazada = solicitudRepository
                .findRechazadaSolicitudBySolicitante(request.getSolicitante().getDocumento());

        Estado estado;
        if (solicitudRechazada.isPresent()) {
            // Si tiene una solicitud rechazada, la nueva también será rechazada
            estado = obtenerEstado("Rechazada");
        } else {
            // Si no tiene rechazadas, pasa a estado "Solicitud"
            estado = obtenerEstado("Solicitud");
        }

        // Crear solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setFecha(LocalDate.now());
        solicitud.setSolicitante(solicitante);
        solicitud.setCodeudor(codeudor);
        solicitud.setValor(BigDecimal.ZERO);
        solicitud.setEstado(estado);
        solicitud.setObservacion(request.getObservacion());
        solicitud.setCodigoRadicado(generarCodigoRadicado());

        solicitud = solicitudRepository.save(solicitud);

        return new SolicitudResponseDTO(
                solicitud.getId(),
                solicitud.getFecha(),
                solicitud.getCodigoRadicado()
        );
    }

    // Servicio 5: Validar token
    @Transactional
    public boolean validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("El token es obligatorio");
        }

        // Verificar que el token no haya vencido (15 minutos)
        LocalDateTime fechaLimite = LocalDateTime.now().minusMinutes(15);
        Optional<Validacion> validacionOpt = validacionRepository.findValidToken(token, fechaLimite);

        if (validacionOpt.isEmpty()) {
            throw new RuntimeException("Token inválido o vencido");
        }

        // Cambiar estado a "Validada"
        Validacion validacion = validacionOpt.get();
        validacion.setEstado("Validada");
        validacionRepository.save(validacion);

        return true;
    }

    private Persona crearPersona(SolicitudRequestDTO.PersonaDTO personaDTO) {
        Persona persona = new Persona();
        persona.setDocumento(personaDTO.getDocumento());
        persona.setNombre(personaDTO.getNombre());
        persona.setEmail(personaDTO.getEmail());
        persona.setTelefono(personaDTO.getTelefono());
        persona.setFechaNacimiento(personaDTO.getFechaNacimiento());
        return personaRepository.save(persona);
    }

    private String generarCodigo(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    private String generarCodigoRadicado() {
        // Genera códigos como "eed43rd" (7-8 caracteres alfanuméricos)
        String caracteres = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    private Estado obtenerEstado(String descripcionEstado) {
        return estadoRepository.findByDescripcion(descripcionEstado)
                .orElseGet(() -> {
                    Estado nuevoEstado = new Estado();
                    nuevoEstado.setDescripcion(descripcionEstado);
                    return estadoRepository.save(nuevoEstado);
                });
    }
}