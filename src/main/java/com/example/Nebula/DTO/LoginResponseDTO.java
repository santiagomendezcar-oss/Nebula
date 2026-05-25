package com.example.Nebula.DTO;

public class LoginResponseDTO {
    private String token;
    private Long usuarioId;
    private String email;
    private String nombre;
    private String rol;  // ← String, no Rol
    private String mensaje;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}