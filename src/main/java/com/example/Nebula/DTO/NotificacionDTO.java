package com.example.Nebula.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data

public class NotificacionDTO {

    private Long id;
    private String titulo;
    private String mensaje;
    private String tipo;
    private String estado;
    private LocalDateTime fechaCreacion;
    private Long referenciaId;

}