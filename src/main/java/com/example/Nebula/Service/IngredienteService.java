package com.example.Nebula.Service;

import com.example.Nebula.DTO.IngredienteDTO;
import com.example.Nebula.Model.Ingrediente;
import java.util.List;

public interface IngredienteService {

    List<Ingrediente> getAllIngredientes();
    List<Ingrediente> getIngredientesDisponibles();
    Ingrediente getIngredienteById(Long id);
    Ingrediente createIngrediente(Ingrediente ingrediente);
    Ingrediente updateIngrediente(Long id, Ingrediente ingredienteDetails);

    void deleteIngrediente(Long id);
    Ingrediente crearIngrediente(IngredienteDTO request);
    Ingrediente actualizarIngrediente(Long id, IngredienteDTO request);
    void eliminarIngrediente(Long id);
}