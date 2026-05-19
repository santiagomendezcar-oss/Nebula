package com.example.Nebula.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Data

public class ProductoPersonalizadoDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    private List<Long> ingredientesAdicionalesIds;
    private List<Long> ingredientesEliminadosIds;

}