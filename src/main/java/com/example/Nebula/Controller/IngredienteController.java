package com.example.Nebula.Controller;

import com.example.Nebula.Model.Ingrediente;
import com.example.Nebula.Service.IngredienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@CrossOrigin(origins = "*")
@Tag(name = "Ingredientes", description = "API para gestión de ingredientes")

public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los ingredientes")
    public ResponseEntity<List<Ingrediente>> getAllIngredientes() {
        return ResponseEntity.ok(ingredienteService.getAllIngredientes());
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener ingredientes disponibles")
    public ResponseEntity<List<Ingrediente>> getIngredientesDisponibles() {
        return ResponseEntity.ok(ingredienteService.getIngredientesDisponibles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ingrediente por ID")
    public ResponseEntity<Ingrediente> getIngredienteById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredienteService.getIngredienteById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo ingrediente")
    public ResponseEntity<Ingrediente> createIngrediente(@Valid @RequestBody Ingrediente ingrediente) {
        return new ResponseEntity<>(ingredienteService.createIngrediente(ingrediente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ingrediente")
    public ResponseEntity<Ingrediente> updateIngrediente(@PathVariable Long id, @Valid @RequestBody Ingrediente ingrediente) {
        return ResponseEntity.ok(ingredienteService.updateIngrediente(id, ingrediente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ingrediente")
    public ResponseEntity<Void> deleteIngrediente(@PathVariable Long id) {
        ingredienteService.deleteIngrediente(id);
        return ResponseEntity.noContent().build();
    }

}