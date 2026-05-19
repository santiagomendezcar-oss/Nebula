package com.example.Nebula.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_invitado")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SesionInvitado {

    @Id
    private String id; // UUID

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime ultimaActividad;

    @Column(nullable = false)
    private Boolean activa = true;

}