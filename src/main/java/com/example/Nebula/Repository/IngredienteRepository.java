package com.example.Nebula.Repository;

import com.example.Nebula.Model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    Optional<Ingrediente> findByNombre(String nombre);
    List<Ingrediente> findByCategoria(String categoria);
    List<Ingrediente> findByDisponibleTrue();

}