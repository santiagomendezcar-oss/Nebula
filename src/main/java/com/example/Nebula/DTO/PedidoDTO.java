package com.example.Nebula.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data

public class PedidoDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String clienteNombre;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotNull(message = "Debe incluir al menos un producto")
    @Size(min = 1, message = "Debe incluir al menos un producto")
    private List<ProductoPersonalizadoDTO> productos;

}