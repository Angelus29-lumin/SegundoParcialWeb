package com.example.segundoparcialweb.Controllers;


import com.example.segundoparcialweb.DTO.*;
import com.example.segundoparcialweb.Service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // Servicio 2: Listar solicitudes
    @GetMapping("/ListarSolicitudes")
    public ResponseEntity<?> listarSolicitudes() {
        try {
            List<SolicitudDTO> solicitudes = solicitudService.listarSolicitudes();
            return ResponseEntity.ok(solicitudes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearMensajeError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Servicio 3: Crear validación de email
    @PostMapping("/validacionEmail")
    public ResponseEntity<?> crearValidacionEmail(@RequestBody ValidacionRequestDTO request) {
        try {
            ValidacionResponseDTO response = solicitudService.crearValidacionEmail(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearMensajeError(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    // Servicio 4: Registrar solicitud
    @PostMapping("/RegistrarSolicitud")
    public ResponseEntity<?> registrarSolicitud(@RequestBody SolicitudRequestDTO request) {
        try {
            SolicitudResponseDTO response = solicitudService.registrarSolicitud(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearMensajeError(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    // Servicio 5: Validar token
    @PostMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        try {
            boolean resultado = solicitudService.validarToken(token);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearMensajeError(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    private Map<String, Object> crearMensajeError(String mensaje, int status) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", status);
        error.put("error", status == 400 ? "Error en la solicitud" : "Error interno del servidor");
        error.put("message", mensaje);
        return error;
    }
}