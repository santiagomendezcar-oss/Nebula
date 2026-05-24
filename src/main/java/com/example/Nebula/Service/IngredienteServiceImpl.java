package com.example.Nebula.Service;

import com.example.Nebula.DTO.IngredienteDTO;
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

    // ========== MÉTODOS PARA ADMIN CON DTO ==========

    @Override
    @Transactional
    public Ingrediente crearIngrediente(IngredienteDTO request) {
        // Verificar si ya existe un ingrediente con el mismo nombre
        if (ingredienteRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un ingrediente con el nombre: " + request.getNombre());
        }

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre(request.getNombre());
        ingrediente.setCategoria(request.getCategoria());
        ingrediente.setPrecioExtra(request.getPrecioExtra());
        ingrediente.setDisponible(request.getDisponible() != null ? request.getDisponible() : true);

        return ingredienteRepository.save(ingrediente);
    }

    @Override
    @Transactional
    public Ingrediente actualizarIngrediente(Long id, IngredienteDTO request) {
        Ingrediente ingrediente = getIngredienteById(id);
        ingrediente.setNombre(request.getNombre());
        ingrediente.setCategoria(request.getCategoria());
        ingrediente.setPrecioExtra(request.getPrecioExtra());
        ingrediente.setDisponible(request.getDisponible() != null ? request.getDisponible() : ingrediente.getDisponible());

        return ingredienteRepository.save(ingrediente);
    }

    @Override
    @Transactional
    public void eliminarIngrediente(Long id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new RuntimeException("Ingrediente no encontrado con ID: " + id);
        }
        ingredienteRepository.deleteById(id);
    }

    // ========== MÉTODOS LEGACY (si los necesitas por compatibilidad) ==========

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