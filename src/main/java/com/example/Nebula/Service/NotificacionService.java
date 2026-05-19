package com.example.Nebula.Service;

import com.example.Nebula.DTO.NotificacionDTO;
import com.example.Nebula.Model.Notificacion;
import java.util.List;

public interface NotificacionService {

    Notificacion crearNotificacion(Long usuarioId, String titulo, String mensaje, String tipo, Long referenciaId);
    Notificacion crearNotificacionParaInvitado(String sesionId, String titulo, String mensaje, String tipo, Long referenciaId);
    List<NotificacionDTO> getNotificacionesByUsuario(Long usuarioId);
    List<NotificacionDTO> getNotificacionesByInvitado(String sesionId);
    void marcarComoLeida(Long notificacionId);
    void marcarTodasComoLeidas(Long usuarioId);
    long getNotificacionesNoLeidas(Long usuarioId);
    void notificarPedidoCreado(Long usuarioId, Long pedidoId);
    void notificarPedidoActualizado(Long usuarioId, Long pedidoId, String estado);
    void notificarDomicilioAsignado(Long usuarioId, Long domicilioId, String domiciliarioNombre);
    void notificarDomicilioEnCamino(Long usuarioId, Long domicilioId);
    void notificarPedidoEntregado(Long usuarioId, Long pedidoId);

}