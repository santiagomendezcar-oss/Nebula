package com.example.Nebula.Service;

import com.example.Nebula.DTO.LoginDTO;
import com.example.Nebula.DTO.LoginResponseDTO;
import com.example.Nebula.DTO.RegistroDTO;
import com.example.Nebula.Model.Usuario;

public interface UsuarioService {

    Usuario registrarUsuario(RegistroDTO request);
    LoginResponseDTO login(LoginDTO request);
    Usuario getUsuarioById(Long id);
    Usuario getUsuarioByEmail(String email);
    Usuario actualizarPerfil(Long id, Usuario usuario);
    boolean existeEmail(String email);

}
