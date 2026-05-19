package com.example.Nebula.DTO;

import lombok.Data;
import com.example.Nebula.Model.Rol;

@Data

public class LoginResponseDTO {

    private String token;
    private Long usuarioId;
    private String email;
    private String nombre;
    private Rol rol;
    private String mensaje;

}