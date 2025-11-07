package com.example.segundoparcialweb.Repositories;

import com.example.segundoparcialweb.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona,Integer> {
}
