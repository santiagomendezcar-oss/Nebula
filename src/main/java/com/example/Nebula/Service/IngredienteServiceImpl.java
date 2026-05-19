package com.example.Nebula.Service;

import com.example.Nebula.Model.Ingrediente;
import com.example.Nebula.Repository.IngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service

public class IngredienteServiceImpl implements IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteServiceImpl(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    @Override
    public List<Ingrediente> getAllIngredientes() {
        return ingredienteRepository.findAll();
    }

    @Override
    public List<Ingrediente> getIngredientesDisponibles() {
        return ingredienteRepository.findByDisponibleTrue();
    }

    @Override
    public Ingrediente getIngredienteById(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Ingrediente createIngrediente(Ingrediente ingrediente) {
        if (ingredienteRepository.findByNombre(ingrediente.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un ingrediente con el nombre: " + ingrediente.getNombre());
        }
        return ingredienteRepository.save(ingrediente);
    }

    @Override
    @Transactional
    public Ingrediente updateIngrediente(Long id, Ingrediente ingredienteDetails) {
        Ingrediente ingrediente = getIngredienteById(id);
        ingrediente.setNombre(ingredienteDetails.getNombre());
        ingrediente.setPrecioExtra(ingredienteDetails.getPrecioExtra());
        ingrediente.setCategoria(ingredienteDetails.getCategoria());
        ingrediente.setDisponible(ingredienteDetails.getDisponible());
        return ingredienteRepository.save(ingrediente);
    }

    @Override
    @Transactional
    public void deleteIngrediente(Long id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new RuntimeException("Ingrediente no encontrado con ID: " + id);
        }
        ingredienteRepository.deleteById(id);
    }

}