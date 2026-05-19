package com.example.Nebula.Repository;

import com.example.Nebula.Model.Notificacion;
import com.example.Nebula.Model.EstadoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
    List<Notificacion> findBySesionInvitadoIdOrderByFechaCreacionDesc(String sesionInvitadoId);
    List<Notificacion> findByUsuarioIdAndEstado(Long usuarioId, EstadoNotificacion estado);
    long countByUsuarioIdAndEstado(Long usuarioId, EstadoNotificacion estado);

}