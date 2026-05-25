package com.example.Nebula.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteNombre;
    private LocalDateTime fechaPedido;
    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // ← CAMBIA a @JsonIgnore (más simple)
    private List<PedidoProducto> productos = new ArrayList<>();

    private String metodoPago;

    @Column(nullable = false)
    private Boolean esDomicilio = false;

    @OneToOne(mappedBy = "pedido")
    @JsonIgnore  // ← IGNORAR para evitar recursión
    private Domicilio domicilio;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    private String sesionInvitadoId;
}