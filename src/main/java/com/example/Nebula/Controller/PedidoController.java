package com.example.Nebula.Controller;

import com.example.Nebula.DTO.PedidoDTO;
import com.example.Nebula.DTO.PedidoDomicilioDTO;
import com.example.Nebula.Model.EstadoPedido;
import com.example.Nebula.Model.Pedido;
import com.example.Nebula.Service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "API para gestión de pedidos con personalización")

public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pedidoService.getPedidosByUsuario(usuarioId));
    }

    @GetMapping("/invitado/{sesionId}")
    public ResponseEntity<List<Pedido>> getPedidosByInvitado(@PathVariable String sesionId) {
        return ResponseEntity.ok(pedidoService.getPedidosByInvitado(sesionId));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pedido sin domicilio")
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody PedidoDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedido(request), HttpStatus.CREATED);
    }

    @PostMapping("/con-domicilio")
    @Operation(summary = "Crear nuevo pedido con domicilio")
    public ResponseEntity<Pedido> crearPedidoConDomicilio(@Valid @RequestBody PedidoDomicilioDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedidoConDomicilio(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, estado));
    }


    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Crear pedido para usuario registrado")
    public ResponseEntity<Pedido> crearPedidoParaUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody PedidoDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedidoParaUsuario(usuarioId, request), HttpStatus.CREATED);
    }

    @PostMapping("/invitado/{sesionId}")
    @Operation(summary = "Crear pedido para invitado")
    public ResponseEntity<Pedido> crearPedidoParaInvitado(
            @PathVariable String sesionId,
            @Valid @RequestBody PedidoDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedidoParaInvitado(sesionId, request), HttpStatus.CREATED);
    }

    @PostMapping("/usuario/{usuarioId}/con-domicilio")
    @Operation(summary = "Crear pedido con domicilio para usuario registrado")
    public ResponseEntity<Pedido> crearPedidoConDomicilioParaUsuario(
            @PathVariable Long usuarioId,
            @Valid @RequestBody PedidoDomicilioDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedidoConDomicilioParaUsuario(usuarioId, request), HttpStatus.CREATED);
    }

    @PostMapping("/invitado/{sesionId}/con-domicilio")
    @Operation(summary = "Crear pedido con domicilio para invitado")
    public ResponseEntity<Pedido> crearPedidoConDomicilioParaInvitado(
            @PathVariable String sesionId,
            @Valid @RequestBody PedidoDomicilioDTO request) {
        return new ResponseEntity<>(pedidoService.crearPedidoConDomicilioParaInvitado(sesionId, request), HttpStatus.CREATED);
    }

}