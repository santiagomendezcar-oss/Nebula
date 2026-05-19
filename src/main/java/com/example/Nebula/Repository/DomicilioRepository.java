package com.example.Nebula.Repository;

import com.example.Nebula.Model.Domicilio;
import com.example.Nebula.Model.EstadoDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {

    Optional<Domicilio> findByPedidoId(Long pedidoId);
    List<Domicilio> findByEstado(EstadoDomicilio estado);
    List<Domicilio> findByDomiciliarioId(Long domiciliarioId);
    List<Domicilio> findByEstadoAndFechaSolicitudBetween(EstadoDomicilio estado, LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT d FROM Domicilio d WHERE d.estado IN :estados ORDER BY d.fechaSolicitud ASC")
    List<Domicilio> findDomiciliosPendientes(@Param("estados") List<EstadoDomicilio> estados);

    @Query("SELECT COUNT(d) FROM Domicilio d WHERE d.domiciliario.id = :domiciliarioId AND d.estado NOT IN ('ENTREGADO', 'CANCELADO')")
    Long countPedidosActivosByDomiciliarioId(@Param("domiciliarioId") Long domiciliarioId);

}