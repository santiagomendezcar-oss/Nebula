package com.example.Nebula.Repository;

import com.example.Nebula.Model.Pedido;
import com.example.Nebula.Model.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByEsDomicilio(Boolean esDomicilio);
    List<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin);

}