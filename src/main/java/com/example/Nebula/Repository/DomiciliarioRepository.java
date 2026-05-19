package com.example.Nebula.Repository;

import com.example.Nebula.Model.Domiciliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Long> {

    List<Domiciliario> findByOrderByPedidosEntregadosAsc();

}