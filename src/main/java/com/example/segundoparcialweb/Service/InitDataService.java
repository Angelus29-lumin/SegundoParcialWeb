package com.example.segundoparcialweb.Service;

import com.example.segundoparcialweb.Models.Estado;
import com.example.segundoparcialweb.Repositories.EstadoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitDataService {
    @Autowired
    private EstadoRepository estadoRepository;

    @PostConstruct
    public void init() {
        String[] estados = {"Solicitud", "Aprobada", "Rechazada", "Finalizada"};
        for (String e : estados) {
            estadoRepository.findByDescripcion(e).orElseGet(() -> estadoRepository.save(new Estado(e)));
        }
    }
}