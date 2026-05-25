package com.example.Nebula.Controller;

import com.example.Nebula.DTO.ProductoDTO;
import com.example.Nebula.Model.Producto;
import com.example.Nebula.Service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestión de productos del menú")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }


    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllProductos());
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener productos disponibles")
    public ResponseEntity<List<Producto>> getProductosDisponibles() {
        return ResponseEntity.ok(productoService.getProductosDisponibles());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }


    @PostMapping
    @Operation(summary = "Crear nuevo producto (Admin)")
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDTO request) {
        return new ResponseEntity<>(productoService.crearProducto(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto (Admin)")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO request) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (Admin)")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disponible")
    @Operation(summary = "Cambiar disponibilidad del producto (Admin)")
    public ResponseEntity<Producto> cambiarDisponibilidad(
            @PathVariable Long id,
            @RequestParam Boolean disponible) {
        return ResponseEntity.ok(productoService.cambiarDisponibilidad(id, disponible));
    }
}