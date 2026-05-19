package com.example.Nebula.Service;

import com.example.Nebula.DTO.DomicilioDTO;
import com.example.Nebula.Model.*;
import com.example.Nebula.Repository.DomicilioRepository;
import com.example.Nebula.Repository.PedidoRepository;
import com.example.Nebula.Repository.DomiciliarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class DomicilioServiceImpl implements DomicilioService {

    private final DomicilioRepository domicilioRepository;
    private final PedidoRepository pedidoRepository;
    private final DomiciliarioRepository domiciliarioRepository;
    private final NotificacionService notificacionService;

    public DomicilioServiceImpl(DomicilioRepository domicilioRepository, PedidoRepository pedidoRepository, DomiciliarioRepository domiciliarioRepository, NotificacionService notificacionService) {
        this.domicilioRepository = domicilioRepository;
        this.pedidoRepository = pedidoRepository;
        this.domiciliarioRepository = domiciliarioRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    @Transactional
    public Domicilio crearDomicilio(DomicilioDTO domicilioRequest, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (pedido.getDomicilio() != null) {
            throw new RuntimeException("Este pedido ya tiene un domicilio asignado");
        }

        Domicilio domicilio = new Domicilio();
        domicilio.setPedido(pedido);
        domicilio.setDireccion(domicilioRequest.getDireccion());
        domicilio.setBarrio(domicilioRequest.getBarrio());
        domicilio.setCiudad(domicilioRequest.getCiudad() != null ? domicilioRequest.getCiudad() : "Ciudad");
        domicilio.setCodigoPostal(domicilioRequest.getCodigoPostal());
        domicilio.setInstruccionesEspeciales(domicilioRequest.getInstruccionesEspeciales());
        domicilio.setTelefonoContacto(domicilioRequest.getTelefonoContacto());
        domicilio.setNombreContacto(domicilioRequest.getNombreContacto());
        domicilio.setFechaSolicitud(LocalDateTime.now());
        domicilio.setEstado(EstadoDomicilio.PENDIENTE);
        domicilio.setCostoDomicilio(calcularCostoDomicilio(domicilioRequest.getBarrio()));

        pedido.setEsDomicilio(true);
        pedido.setDomicilio(domicilio);
        pedidoRepository.save(pedido);

        return domicilioRepository.save(domicilio);
    }

    @Override
    public Domicilio getDomicilioById(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));
    }

    @Override
    public Domicilio getDomicilioByPedidoId(Long pedidoId) {
        return domicilioRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("No hay domicilio asociado a este pedido"));
    }

    @Override
    @Transactional
    public Domicilio actualizarEstadoDomicilio(Long id, EstadoDomicilio nuevoEstado) {
        Domicilio domicilio = getDomicilioById(id);
        validarTransicionEstado(domicilio.getEstado(), nuevoEstado);

        switch (nuevoEstado) {
            case ASIGNADO:
                domicilio.setFechaAsignacion(LocalDateTime.now());
                break;
            case RECOGIDO:
                domicilio.setFechaRecogida(LocalDateTime.now());
                break;
            case EN_ENTREGA:
                if (domicilio.getPedido().getUsuario() != null) {
                    notificacionService.notificarDomicilioEnCamino(
                            domicilio.getPedido().getUsuario().getId(), id
                    );
                } else if (domicilio.getPedido().getSesionInvitadoId() != null) {
                    notificacionService.crearNotificacionParaInvitado(
                            domicilio.getPedido().getSesionInvitadoId(),
                            "Domicilio en Camino",
                            "Tu pedido está en camino hacia tu dirección",
                            "DOMICILIO",
                            id
                    );
                }
                break;
            case ENTREGADO:
                domicilio.setFechaEntrega(LocalDateTime.now());
                domicilio.getPedido().setEstado(EstadoPedido.ENTREGADO);
                if (domicilio.getPedido().getUsuario() != null) {
                    notificacionService.notificarPedidoEntregado(
                            domicilio.getPedido().getUsuario().getId(),
                            domicilio.getPedido().getId()
                    );
                } else if (domicilio.getPedido().getSesionInvitadoId() != null) {
                    notificacionService.crearNotificacionParaInvitado(
                            domicilio.getPedido().getSesionInvitadoId(),
                            "Pedido Entregado",
                            "Tu pedido #" + domicilio.getPedido().getId() + " ha sido entregado",
                            "PEDIDO",
                            domicilio.getPedido().getId()
                    );
                }
                break;
        }

        domicilio.setEstado(nuevoEstado);
        return domicilioRepository.save(domicilio);
    }

    @Override
    @Transactional
    public Domicilio asignarDomiciliario(Long domicilioId, Long domiciliarioId) {
        Domicilio domicilio = getDomicilioById(domicilioId);
        Domiciliario domiciliario = domiciliarioRepository.findById(domiciliarioId)
                .orElseThrow(() -> new RuntimeException("Domiciliario no encontrado"));

        domicilio.setDomiciliario(domiciliario);
        domicilio.setEstado(EstadoDomicilio.ASIGNADO);
        domicilio.setFechaAsignacion(LocalDateTime.now());

        Domicilio saved = domicilioRepository.save(domicilio);

        // Enviar notificación
        if (domicilio.getPedido().getUsuario() != null) {
            notificacionService.notificarDomicilioAsignado(
                    domicilio.getPedido().getUsuario().getId(),
                    domicilioId,
                    domiciliario.getNombre()
            );
        } else if (domicilio.getPedido().getSesionInvitadoId() != null) {
            notificacionService.crearNotificacionParaInvitado(
                    domicilio.getPedido().getSesionInvitadoId(),
                    "Domicilio Asignado",
                    "Tu domicilio ha sido asignado a " + domiciliario.getNombre(),
                    "DOMICILIO",
                    domicilioId
            );
        }

        return saved;
    }

    @Override
    public List<Domicilio> getDomiciliosPendientes() {
        return domicilioRepository.findDomiciliosPendientes(
                List.of(EstadoDomicilio.PENDIENTE, EstadoDomicilio.ASIGNADO)
        );
    }

    @Override
    public List<Domicilio> getDomiciliosPorEstado(EstadoDomicilio estado) {
        return domicilioRepository.findByEstado(estado);
    }

    @Override
    public Double calcularCostoDomicilio(Double distanciaKm) {
        if (distanciaKm == null) return 5000.0;
        double costoBase = 5000.0;
        double costoPorKm = 1500.0;
        return costoBase + (distanciaKm * costoPorKm);
    }

    private Double calcularCostoDomicilio(String barrio) {
        return 5000.0;
    }

    private void validarTransicionEstado(EstadoDomicilio actual, EstadoDomicilio nuevo) {
        if (actual == EstadoDomicilio.ENTREGADO && nuevo != EstadoDomicilio.ENTREGADO) {
            throw new RuntimeException("No se puede cambiar estado de domicilio entregado");
        }
        if (actual == EstadoDomicilio.CANCELADO) {
            throw new RuntimeException("No se puede cambiar estado de domicilio cancelado");
        }
    }

    @Override
    public List<Domicilio> getAllDomicilios() {
        return domicilioRepository.findAll();
    }

}