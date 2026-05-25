package com.example.Nebula.Controller;

import com.example.Nebula.DTO.*;
import com.example.Nebula.Model.SesionInvitado;
import com.example.Nebula.Model.Usuario;
import com.example.Nebula.Service.NotificacionService;
import com.example.Nebula.Service.SesionInvitadoService;
import com.example.Nebula.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "API para registro, login y sesiones de invitado")
public class AutenticacionController {

    private final UsuarioService usuarioService;
    private final SesionInvitadoService sesionInvitadoService;
    private final NotificacionService notificacionService;

    public AutenticacionController(UsuarioService usuarioService,
                                   SesionInvitadoService sesionInvitadoService,
                                   NotificacionService notificacionService) {
        this.usuarioService = usuarioService;
        this.sesionInvitadoService = sesionInvitadoService;
        this.notificacionService = notificacionService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroDTO request,
                                       BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            Map<String, String> error = new HashMap<>();
            error.put("error", errores);
            return ResponseEntity.badRequest().body(error);
        }

        try {
            Usuario usuario = usuarioService.registrarUsuario(request);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuarioId", usuario.getId());
            response.put("email", usuario.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO request,
                                   BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            String errores = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            Map<String, String> error = new HashMap<>();
            error.put("error", errores);
            return ResponseEntity.badRequest().body(error);
        }

        try {
            LoginResponseDTO response = usuarioService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/invitado")
    @Operation(summary = "Iniciar sesión como invitado")
    public ResponseEntity<?> iniciarInvitado() {
        String sesionId = sesionInvitadoService.crearSesionInvitado();
        Map<String, String> response = new HashMap<>();
        response.put("sesionId", sesionId);
        response.put("mensaje", "Sesión de invitado creada");
        response.put("tipo", "INVITADO");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invitado/validar")
    @Operation(summary = "Validar sesión de invitado")
    public ResponseEntity<?> validarInvitado(@RequestBody Map<String, String> request) {
        String sesionId = request.get("sesionId");
        try {
            SesionInvitado sesion = sesionInvitadoService.validarSesion(sesionId);
            Map<String, Object> response = new HashMap<>();
            response.put("valida", true);
            response.put("sesionId", sesion.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("valida", false);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/verificar-email")
    @Operation(summary = "Verificar si un email ya existe")
    public ResponseEntity<?> verificarEmail(@RequestParam String email) {
        boolean existe = usuarioService.existeEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);
        return ResponseEntity.ok(response);
    }
}