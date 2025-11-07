package com.example.segundoparcialweb.Controllers;


import com.example.segundoparcialweb.DTO.*;
import com.example.segundoparcialweb.Service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    // Servicio 2: Listar solicitudes
    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> listarSolicitudes() {
        List<SolicitudDTO> solicitudes = solicitudService.listarSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    // Servicio 3: Crear validación de email
    @PostMapping("/validacion")
    public ResponseEntity<ValidacionResponseDTO> crearValidacionEmail(@RequestBody ValidacionRequestDTO request) {
        ValidacionResponseDTO response = solicitudService.crearValidacionEmail(request);
        return ResponseEntity.ok(response);
    }

    // Servicio 4: Registrar solicitud
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> registrarSolicitud(@RequestBody SolicitudRequestDTO request) {
        SolicitudResponseDTO response = solicitudService.registrarSolicitud(request);
        return ResponseEntity.ok(response);
    }

    // Servicio 5: Validar token
    @PostMapping("/validar-token")
    public ResponseEntity<Boolean> validarToken(@RequestParam String token) {
        boolean resultado = solicitudService.validarToken(token);
        return ResponseEntity.ok(resultado);
    }
}
