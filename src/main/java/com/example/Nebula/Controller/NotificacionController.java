package com.example.Nebula.Controller;

import com.example.Nebula.DTO.NotificacionDTO;
import com.example.Nebula.Service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
@Tag(name = "Notificaciones", description = "API para gestión de notificaciones")

public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener notificaciones de un usuario")
    public ResponseEntity<List<NotificacionDTO>> getNotificacionesByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacionService.getNotificacionesByUsuario(usuarioId));
    }

    @GetMapping("/invitado/{sesionId}")
    @Operation(summary = "Obtener notificaciones de un invitado")
    public ResponseEntity<List<NotificacionDTO>> getNotificacionesByInvitado(@PathVariable String sesionId) {
        return ResponseEntity.ok(notificacionService.getNotificacionesByInvitado(sesionId));
    }

    @PatchMapping("/{id}/leer")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(Map.of("mensaje", "Notificación marcada como leída"));
    }

    @PatchMapping("/usuario/{usuarioId}/leer-todas")
    @Operation(summary = "Marcar todas las notificaciones como leídas")
    public ResponseEntity<?> marcarTodasComoLeidas(@PathVariable Long usuarioId) {
        notificacionService.marcarTodasComoLeidas(usuarioId);
        return ResponseEntity.ok(Map.of("mensaje", "Todas las notificaciones marcadas como leídas"));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    @Operation(summary = "Contar notificaciones no leídas")
    public ResponseEntity<?> contarNoLeidas(@PathVariable Long usuarioId) {
        long count = notificacionService.getNotificacionesNoLeidas(usuarioId);
        return ResponseEntity.ok(Map.of("noLeidas", count));
    }

}