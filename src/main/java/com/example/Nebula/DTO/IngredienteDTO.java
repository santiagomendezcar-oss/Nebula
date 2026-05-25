package com.example.Nebula.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class IngredienteDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @PositiveOrZero(message = "El precio extra debe ser 0 o mayor")
    private Double precioExtra;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getPrecioExtra() { return precioExtra; }
    public void setPrecioExtra(Double precioExtra) { this.precioExtra = precioExtra; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}