package com.example.Nebula.DTO;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data

public class PedidoDomicilioDTO {

    @NotNull(message = "Los datos del pedido son obligatorios")
    @Valid
    private PedidoDTO pedido;

    @NotNull(message = "Los datos del domicilio son obligatorios")
    @Valid
    private DomicilioDTO domicilio;

}