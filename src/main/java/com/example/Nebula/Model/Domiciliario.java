package com.example.Nebula.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "domiciliarios")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Domiciliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    private String telefono;
    private String email;
    private String vehiculo;

    private Double calificacionPromedio = 5.0;
    private Integer pedidosEntregados = 0;

    @OneToMany(mappedBy = "domiciliario")
    private List<Domicilio> domicilios = new ArrayList<>();

}