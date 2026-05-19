package com.example.Nebula.Service;

import com.example.Nebula.DTO.DomicilioDTO;
import com.example.Nebula.Model.Domicilio;
import com.example.Nebula.Model.EstadoDomicilio;
import java.util.List;

public interface DomicilioService {

    Domicilio crearDomicilio(DomicilioDTO domicilioRequest, Long pedidoId);
    Domicilio getDomicilioById(Long id);
    Domicilio getDomicilioByPedidoId(Long pedidoId);
    Domicilio actualizarEstadoDomicilio(Long id, EstadoDomicilio nuevoEstado);
    Domicilio asignarDomiciliario(Long domicilioId, Long domiciliarioId);
    List<Domicilio> getDomiciliosPendientes();
    List<Domicilio> getDomiciliosPorEstado(EstadoDomicilio estado);
    Double calcularCostoDomicilio(Double distanciaKm);
    List<Domicilio> getAllDomicilios();

}
