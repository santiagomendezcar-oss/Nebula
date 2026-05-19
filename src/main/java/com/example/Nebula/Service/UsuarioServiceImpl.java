package com.example.Nebula.Service;

import com.example.Nebula.DTO.LoginDTO;
import com.example.Nebula.DTO.LoginResponseDTO;
import com.example.Nebula.DTO.RegistroDTO;
import com.example.Nebula.Model.Usuario;
import com.example.Nebula.Repository.UsuarioRepository;
import com.example.Nebula.security.JwtUtil;
import com.example.Nebula.security.ValidarContraseña;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final ValidarContraseña validarContraseña;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              JwtUtil jwtUtil,
                              ValidarContraseña validarContraseña) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.validarContraseña = validarContraseña;
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(RegistroDTO request) {
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Formato de email inválido");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (!validarContraseña.isValid(request.getPassword())) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(validarContraseña.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol("CLIENTE");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    @Override
    public LoginResponseDTO login(LoginDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        if (!validarContraseña.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario desactivado");
        }

        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getId(), usuario.getRol().toString());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setUsuarioId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setNombre(usuario.getNombre());
        response.setRol(usuario.getRol());
        response.setMensaje("Login exitoso");

        return response;
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public Usuario actualizarPerfil(Long id, Usuario usuarioDetails) {
        Usuario usuario = getUsuarioById(id);
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setDireccion(usuarioDetails.getDireccion());
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

}
