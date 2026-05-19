package com.example.Nebula.Controller;

import com.example.Nebula.Model.Domicilio;
import com.example.Nebula.Model.EstadoDomicilio;
import com.example.Nebula.Service.DomicilioService;
import com.example.Nebula.Service.DomiciliarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@CrossOrigin(origins = "*")
@Tag(name = "Domicilios", description = "API para gestión de domicilios")

public class DomicilioController {

    private final DomicilioService domicilioService;
    private final DomiciliarioService domiciliarioService;

    public DomicilioController(DomicilioService domicilioService, DomiciliarioService domiciliarioService) {
        this.domicilioService = domicilioService;
        this.domiciliarioService = domiciliarioService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los domicilios")
    public ResponseEntity<List<Domicilio>> getAllDomicilios() {
        return ResponseEntity.ok(domicilioService.getAllDomicilios());
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Obtener domicilios pendientes")
    public ResponseEntity<List<Domicilio>> getDomiciliosPendientes() {
        return ResponseEntity.ok(domicilioService.getDomiciliosPendientes());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener domicilios por estado")
    public ResponseEntity<List<Domicilio>> getDomiciliosPorEstado(@PathVariable EstadoDomicilio estado) {
        return ResponseEntity.ok(domicilioService.getDomiciliosPorEstado(estado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener domicilio por ID")
    public ResponseEntity<Domicilio> getDomicilioById(@PathVariable Long id) {
        return ResponseEntity.ok(domicilioService.getDomicilioById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener domicilio por ID de pedido")
    public ResponseEntity<Domicilio> getDomicilioByPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(domicilioService.getDomicilioByPedidoId(pedidoId));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del domicilio")
    public ResponseEntity<Domicilio> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDomicilio estado) {
        return ResponseEntity.ok(domicilioService.actualizarEstadoDomicilio(id, estado));
    }

    @PostMapping("/{domicilioId}/asignar/{domiciliarioId}")
    @Operation(summary = "Asignar domiciliario a un domicilio")
    public ResponseEntity<Domicilio> asignarDomiciliario(
            @PathVariable Long domicilioId,
            @PathVariable Long domiciliarioId) {
        return ResponseEntity.ok(domicilioService.asignarDomiciliario(domicilioId, domiciliarioId));
    }

    @PostMapping("/{domicilioId}/asignar-automatico")
    @Operation(summary = "Asignar automáticamente el domiciliario menos ocupado")
    public ResponseEntity<Domicilio> asignarDomiciliarioAutomatico(@PathVariable Long domicilioId) {
        com.example.Nebula.Model.Domiciliario domiciliario = domiciliarioService.getDomiciliarioMenosOcupado();
        return ResponseEntity.ok(domicilioService.asignarDomiciliario(domicilioId, domiciliario.getId()));
    }
}