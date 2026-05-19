package com.example.Nebula.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Data

public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio base es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Double precioBase;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    private List<Long> ingredientesBaseIds;
    private Boolean disponible = true;

}