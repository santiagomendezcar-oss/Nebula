package com.example.Nebula.Service;

import com.example.Nebula.DTO.PedidoDTO;
import com.example.Nebula.DTO.PedidoDomicilioDTO;
import com.example.Nebula.Model.EstadoPedido;
import com.example.Nebula.Model.Pedido;
import java.util.List;

public interface PedidoService {

    List<Pedido> getAllPedidos();
    List<Pedido> getPedidosByUsuario(Long usuarioId);
    List<Pedido> getPedidosByInvitado(String sesionId);
    Pedido getPedidoById(Long id);
    Pedido crearPedido(PedidoDTO request);
    Pedido crearPedidoConDomicilio(PedidoDomicilioDTO request);
    Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado);
    Pedido crearPedidoParaUsuario(Long usuarioId, PedidoDTO request);
    Pedido crearPedidoParaInvitado(String sesionId, PedidoDTO request);
    Pedido crearPedidoConDomicilioParaUsuario(Long usuarioId, PedidoDomicilioDTO request);
    Pedido crearPedidoConDomicilioParaInvitado(String sesionId, PedidoDomicilioDTO request);

}