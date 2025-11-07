package com.example.segundoparcialweb.Controllers;

import com.example.segundoparcialweb.DTO.RegistroSolicitudRequest;
import com.example.segundoparcialweb.DTO.SolicitudListadoDTO;
import com.example.segundoparcialweb.Models.Solicitud;
import com.example.segundoparcialweb.Service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;


    @GetMapping
    public ResponseEntity<List<SolicitudListadoDTO>> listar() {
        return ResponseEntity.ok(solicitudService.listarSolicitudes());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RegistroSolicitudRequest req) {
        Solicitud s = solicitudService.registrarSolicitud(req);
        return ResponseEntity.ok(java.util.Map.of(
                "id", s.getId(),
                "fecha", s.getFecha().toString(),
                "codigo_radicado", s.getCodigoRadicado()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Solicitud s = solicitudService.getById(id);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s);
    }
}
