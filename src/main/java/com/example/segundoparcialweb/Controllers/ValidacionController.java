package com.example.segundoparcialweb.Controllers;

import com.example.segundoparcialweb.DTO.CrearValidacionRequest;
import com.example.segundoparcialweb.DTO.CrearValidacionResponse;
import com.example.segundoparcialweb.DTO.ValidarTokenRequest;
import com.example.segundoparcialweb.Service.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ValidacionController {

    @Autowired
    private ValidacionService validacionService;

    @PostMapping
    public ResponseEntity<CrearValidacionResponse> crear(@RequestBody CrearValidacionRequest req) {
        return ResponseEntity.ok(validacionService.crearValidacion(req));
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validar(@RequestBody ValidarTokenRequest req) {
        boolean ok = validacionService.validarToken(req.getToken());
        if (!ok) {
            return ResponseEntity.badRequest().body(java.util.Map.of("validado", false));
        }
        return ResponseEntity.ok(java.util.Map.of("validado", true));
    }
}
