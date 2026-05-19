package com.example.Nebula.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String titulo;
    private String mensaje;
    private String tipo; // PEDIDO, DOMICILIO, PROMOCION, SISTEMA

    @Enumerated(EnumType.STRING)
    private EstadoNotificacion estado = EstadoNotificacion.NO_LEIDA;

    private LocalDateTime fechaCreacion;
    private Long referenciaId; // ID del pedido, domicilio, etc.

    private Boolean esParaInvitado = false;
    private String sesionInvitadoId; // Para notificaciones de invitados

}