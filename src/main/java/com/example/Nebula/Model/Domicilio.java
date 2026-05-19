package com.example.Nebula.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "domicilios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;

    @Column(nullable = false)
    private String direccion;

    private String barrio;
    private String ciudad;
    private String codigoPostal;
    private String instruccionesEspeciales;

    @Column(nullable = false)
    private String telefonoContacto;

    private String nombreContacto;

    @Enumerated(EnumType.STRING)
    private EstadoDomicilio estado = EstadoDomicilio.PENDIENTE;

    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaRecogida;
    private LocalDateTime fechaEntrega;

    private Double costoDomicilio = 0.0;
    private Double distanciaKm;

    @ManyToOne
    @JoinColumn(name = "domiciliario_id")
    private Domiciliario domiciliario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}