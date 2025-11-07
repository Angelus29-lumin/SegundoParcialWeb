package com.example.segundoparcialweb.Utills;


import com.example.segundoparcialweb.Repository.EstadoRepository;
import com.example.segundoparcialweb.Models.Estado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public void run(String... args) throws Exception {
        crearEstadosSiNoExisten();
    }

    private void crearEstadosSiNoExisten() {
        String[] estados = {"Solicitud", "Aprobada", "Rechazada", "Finalizada"};

        for (String descripcion : estados) {
            if (estadoRepository.findByDescripcion(descripcion).isEmpty()) {
                Estado estado = new Estado();
                estado.setDescripcion(descripcion);
                estadoRepository.save(estado);
                System.out.println(" Estado creado: " + descripcion);
            }
        }
        System.out.println("Todos los estados están listos!");
    }
}