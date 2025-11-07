package com.example.segundoparcialweb.Service;

import com.example.segundoparcialweb.DTO.CrearValidacionRequest;
import com.example.segundoparcialweb.DTO.CrearValidacionResponse;
import com.example.segundoparcialweb.Models.Validacion;
import com.example.segundoparcialweb.Repositories.ValidacionRepository;
import com.example.segundoparcialweb.Utills.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ValidacionService {

    @Autowired
    private ValidacionRepository validacionRepository;

    public CrearValidacionResponse crearValidacion(CrearValidacionRequest req) {
        // generar token y código (únicos)
        String token = TokenUtils.generateToken(24); // ejemplo
        String codigo = TokenUtils.generateCodigo(6);

        Validacion v = new Validacion();
        v.setEmail(req.getEmail());
        v.setDocumento(req.getDocumento());
        v.setFecha(LocalDateTime.now());
        v.setEstado("Pendiente");
        v.setToken(token);
        v.setCodigo(codigo);

        validacionRepository.save(v);

        // NOTE: aquí en la vida real enviarías email con token/codigo
        return new CrearValidacionResponse(token, codigo);
    }

    public boolean validarToken(String token) {
        var opt = validacionRepository.findByToken(token);
        if (opt.isEmpty()) return false;
        Validacion v = opt.get();
        // 15 minutos de validez desde creación (creadoEn)
        if (v.getCreadoEn() == null) return false;
        LocalDateTime ahora = LocalDateTime.now();
        if (v.getCreadoEn().plusMinutes(15).isBefore(ahora)) {
            return false;
        }
        // cambiar estado a Validada
        v.setEstado("Validada");
        validacionRepository.save(v);
        return true;
    }

    public boolean isEmailValidado(String email) {
        // check if there's a validacion with email and estado == Validada
        return validacionRepository.findAll().stream()
                .anyMatch(v -> v.getEmail().equalsIgnoreCase(email) && "Validada".equalsIgnoreCase(v.getEstado()));
    }
}