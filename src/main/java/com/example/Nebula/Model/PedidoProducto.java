package com.example.Nebula.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference  // ← Rompe el bucle, no envía el pedido de vuelta
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;
    private Double precioUnitario;

    @ManyToMany
    @JoinTable(
            name = "pedido_ingredientes_adicionales",
            joinColumns = @JoinColumn(name = "pedido_producto_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientesAdicionales = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "pedido_ingredientes_eliminados",
            joinColumns = @JoinColumn(name = "pedido_producto_id"),
            inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private List<Ingrediente> ingredientesEliminados = new ArrayList<>();
}