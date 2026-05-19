package com.example.Nebula.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data

public class DomicilioDTO {

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String barrio;
    private String ciudad;
    private String codigoPostal;
    private String instruccionesEspeciales;

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    private String telefonoContacto;

    private String nombreContacto;

}