package com.example.Nebula.Service;

import com.example.Nebula.DTO.NotificacionDTO;
import com.example.Nebula.Model.Notificacion;
import com.example.Nebula.Model.EstadoNotificacion;
import com.example.Nebula.Model.Usuario;
import com.example.Nebula.Repository.NotificacionRepository;
import com.example.Nebula.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository, UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Notificacion crearNotificacion(Long usuarioId, String titulo, String mensaje, String tipo, Long referenciaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setReferenciaId(referenciaId);

        return notificacionRepository.save(notificacion);
    }

    @Override
    @Transactional
    public Notificacion crearNotificacionParaInvitado(String sesionId, String titulo, String mensaje, String tipo, Long referenciaId) {
        Notificacion notificacion = new Notificacion();
        notificacion.setSesionInvitadoId(sesionId);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setEstado(EstadoNotificacion.NO_LEIDA);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacion.setReferenciaId(referenciaId);
        notificacion.setEsParaInvitado(true);

        return notificacionRepository.save(notificacion);
    }

    @Override
    public List<NotificacionDTO> getNotificacionesByUsuario(Long usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        return notificaciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NotificacionDTO> getNotificacionesByInvitado(String sesionId) {
        List<Notificacion> notificaciones = notificacionRepository.findBySesionInvitadoIdOrderByFechaCreacionDesc(sesionId);
        return notificaciones.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarComoLeida(Long notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setEstado(EstadoNotificacion.LEIDA);
        notificacionRepository.save(notificacion);
    }

    @Override
    @Transactional
    public void marcarTodasComoLeidas(Long usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioIdAndEstado(usuarioId, EstadoNotificacion.NO_LEIDA);
        notificaciones.forEach(n -> n.setEstado(EstadoNotificacion.LEIDA));
        notificacionRepository.saveAll(notificaciones);
    }

    @Override
    public long getNotificacionesNoLeidas(Long usuarioId) {
        return notificacionRepository.countByUsuarioIdAndEstado(usuarioId, EstadoNotificacion.NO_LEIDA);
    }

    @Override
    public void notificarPedidoCreado(Long usuarioId, Long pedidoId) {
        crearNotificacion(usuarioId, "Pedido Creado",
                "Tu pedido #" + pedidoId + " ha sido creado exitosamente",
                "PEDIDO", pedidoId);
    }

    @Override
    public void notificarPedidoActualizado(Long usuarioId, Long pedidoId, String estado) {
        String mensaje = "";
        switch (estado) {
            case "EN_PREPARACION":
                mensaje = "Tu pedido #" + pedidoId + " está en preparación";
                break;
            case "LISTO":
                mensaje = "Tu pedido #" + pedidoId + " está listo";
                break;
            default:
                mensaje = "Tu pedido #" + pedidoId + " ha cambiado a estado: " + estado;
        }
        crearNotificacion(usuarioId, "Pedido Actualizado", mensaje, "PEDIDO", pedidoId);
    }

    @Override
    public void notificarDomicilioAsignado(Long usuarioId, Long domicilioId, String domiciliarioNombre) {
        crearNotificacion(usuarioId, "Domicilio Asignado",
                "Tu domicilio ha sido asignado a " + domiciliarioNombre,
                "DOMICILIO", domicilioId);
    }

    @Override
    public void notificarDomicilioEnCamino(Long usuarioId, Long domicilioId) {
        crearNotificacion(usuarioId, "Domicilio en Camino",
                "Tu pedido está en camino hacia tu dirección",
                "DOMICILIO", domicilioId);
    }

    @Override
    public void notificarPedidoEntregado(Long usuarioId, Long pedidoId) {
        crearNotificacion(usuarioId, "Pedido Entregado",
                "Tu pedido #" + pedidoId + " ha sido entregado. ¡Disfruta tu comida!",
                "PEDIDO", pedidoId);
    }

    private NotificacionDTO convertToDTO(Notificacion notificacion) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(notificacion.getId());
        dto.setTitulo(notificacion.getTitulo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setTipo(notificacion.getTipo());
        dto.setEstado(notificacion.getEstado().toString());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setReferenciaId(notificacion.getReferenciaId());
        return dto;
    }

}

