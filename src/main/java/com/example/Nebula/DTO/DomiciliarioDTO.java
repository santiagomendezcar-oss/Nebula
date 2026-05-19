package com.example.Nebula.DTO;

import lombok.Data;

@Data

public class DomiciliarioDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String vehiculo;
    private Double calificacionPromedio;
    private Integer pedidosEntregados;

}