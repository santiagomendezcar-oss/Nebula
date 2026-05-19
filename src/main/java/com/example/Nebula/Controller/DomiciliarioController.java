package com.example.Nebula.Controller;

import com.example.Nebula.Model.Domiciliario;
import com.example.Nebula.Service.DomiciliarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/domiciliarios")
@CrossOrigin(origins = "*")
@Tag(name = "Domiciliarios", description = "API para gestión de domiciliarios")


public class DomiciliarioController {

    private final DomiciliarioService domiciliarioService;

    public DomiciliarioController(DomiciliarioService domiciliarioService) {
        this.domiciliarioService = domiciliarioService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los domiciliarios")
    public ResponseEntity<List<Domiciliario>> getAllDomiciliarios() {
        return ResponseEntity.ok(domiciliarioService.getAllDomiciliarios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener domiciliario por ID")
    public ResponseEntity<Domiciliario> getDomiciliarioById(@PathVariable Long id) {
        return ResponseEntity.ok(domiciliarioService.getDomiciliarioById(id));
    }

    @GetMapping("/menos-ocupado")
    @Operation(summary = "Obtener domiciliario con menos pedidos entregados")
    public ResponseEntity<Domiciliario> getDomiciliarioMenosOcupado() {
        return ResponseEntity.ok(domiciliarioService.getDomiciliarioMenosOcupado());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo domiciliario")
    public ResponseEntity<Domiciliario> createDomiciliario(@Valid @RequestBody Domiciliario domiciliario) {
        return new ResponseEntity<>(domiciliarioService.createDomiciliario(domiciliario), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/calificacion")
    @Operation(summary = "Actualizar calificación del domiciliario")
    public ResponseEntity<Domiciliario> updateCalificacion(
            @PathVariable Long id,
            @RequestParam Double calificacion) {
        return ResponseEntity.ok(domiciliarioService.updateCalificacion(id, calificacion));
    }

}