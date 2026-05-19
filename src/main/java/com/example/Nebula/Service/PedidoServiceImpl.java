package com.example.Nebula.Service;

import com.example.Nebula.DTO.PedidoDTO;
import com.example.Nebula.DTO.PedidoDomicilioDTO;
import com.example.Nebula.DTO.ProductoPersonalizadoDTO;
import com.example.Nebula.Model.*;
import com.example.Nebula.Repository.PedidoRepository;
import com.example.Nebula.Repository.ProductoRepository;
import com.example.Nebula.Repository.IngredienteRepository;
import com.example.Nebula.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final DomicilioService domicilioService;
    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;
    private final SesionInvitadoService sesionInvitadoService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProductoRepository productoRepository, IngredienteRepository ingredienteRepository, DomicilioService domicilioService, NotificacionService notificacionService, UsuarioRepository usuarioRepository, SesionInvitadoService sesionInvitadoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.domicilioService = domicilioService;
        this.notificacionService = notificacionService;
        this.usuarioRepository = usuarioRepository;
        this.sesionInvitadoService = sesionInvitadoService;
    }

    @Override
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Pedido crearPedido(PedidoDTO request) {
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto");
        }

        Pedido pedido = new Pedido();
        pedido.setClienteNombre(request.getClienteNombre());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMetodoPago(request.getMetodoPago());
        pedido.setEsDomicilio(false);

        List<PedidoProducto> pedidoProductos = new ArrayList<>();
        double total = 0.0;

        for (ProductoPersonalizadoDTO prodDTO : request.getProductos()) {
            Producto productoBase = productoRepository.findById(prodDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + prodDTO.getProductoId()));

            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(productoBase);
            pedidoProducto.setCantidad(prodDTO.getCantidad());

            double precioUnitario = productoBase.getPrecioBase();

            if (prodDTO.getIngredientesAdicionalesIds() != null && !prodDTO.getIngredientesAdicionalesIds().isEmpty()) {
                List<Ingrediente> adicionales = ingredienteRepository.findAllById(prodDTO.getIngredientesAdicionalesIds());
                pedidoProducto.setIngredientesAdicionales(adicionales);
                for (Ingrediente ing : adicionales) {
                    if (ing.getPrecioExtra() != null) {
                        precioUnitario += ing.getPrecioExtra();
                    }
                }
            }

            if (prodDTO.getIngredientesEliminadosIds() != null && !prodDTO.getIngredientesEliminadosIds().isEmpty()) {
                List<Ingrediente> eliminados = ingredienteRepository.findAllById(prodDTO.getIngredientesEliminadosIds());
                pedidoProducto.setIngredientesEliminados(eliminados);
            }

            pedidoProducto.setPrecioUnitario(precioUnitario);
            total += precioUnitario * prodDTO.getCantidad();
            pedidoProductos.add(pedidoProducto);
        }

        pedido.setProductos(pedidoProductos);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido crearPedidoConDomicilio(PedidoDomicilioDTO request) {
        Pedido pedido = crearPedido(request.getPedido());
        domicilioService.crearDomicilio(request.getDomicilio(), pedido.getId());
        return pedido;
    }

    @Override
    @Transactional
    public Pedido actualizarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = getPedidoById(id);

        if (pedido.getEstado() == EstadoPedido.ENTREGADO && nuevoEstado != EstadoPedido.ENTREGADO) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido ya entregado");
        }
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new RuntimeException("No se puede cambiar el estado de un pedido cancelado");
        }

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido crearPedidoParaUsuario(Long usuarioId, PedidoDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = crearPedidoBase(request);
        pedido.setUsuario(usuario);
        pedido = pedidoRepository.save(pedido);

        notificacionService.notificarPedidoCreado(usuarioId, pedido.getId());

        return pedido;
    }

    @Transactional
    public Pedido crearPedidoParaInvitado(String sesionId, PedidoDTO request) {
        SesionInvitado sesion = sesionInvitadoService.validarSesion(sesionId);

        Pedido pedido = crearPedidoBase(request);
        pedido.setSesionInvitadoId(sesionId);
        pedido = pedidoRepository.save(pedido);

        notificacionService.crearNotificacionParaInvitado(sesionId, "Pedido Creado",
                "Tu pedido #" + pedido.getId() + " ha sido creado", "PEDIDO", pedido.getId());

        return pedido;
    }

    @Transactional
    public Pedido crearPedidoConDomicilioParaUsuario(Long usuarioId, PedidoDomicilioDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = crearPedidoBase(request.getPedido());
        pedido.setUsuario(usuario);
        pedido.setEsDomicilio(true);
        pedido = pedidoRepository.save(pedido);

        Domicilio domicilio = domicilioService.crearDomicilio(request.getDomicilio(), pedido.getId());
        domicilio.setUsuario(usuario);

        notificacionService.notificarPedidoCreado(usuarioId, pedido.getId());

        return pedido;
    }

    @Transactional
    public Pedido crearPedidoConDomicilioParaInvitado(String sesionId, PedidoDomicilioDTO request) {
        SesionInvitado sesion = sesionInvitadoService.validarSesion(sesionId);

        Pedido pedido = crearPedidoBase(request.getPedido());
        pedido.setSesionInvitadoId(sesionId);
        pedido.setEsDomicilio(true);
        pedido = pedidoRepository.save(pedido);

        Domicilio domicilio = domicilioService.crearDomicilio(request.getDomicilio(), pedido.getId());

        notificacionService.crearNotificacionParaInvitado(sesionId, "Pedido Creado",
                "Tu pedido con domicilio #" + pedido.getId() + " ha sido creado", "PEDIDO", pedido.getId());

        return pedido;
    }

    // Método auxiliar
    private Pedido crearPedidoBase(PedidoDTO request) {
        // El mismo código que crearPedido() pero sin guardar aún
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un producto");
        }

        Pedido pedido = new Pedido();
        pedido.setClienteNombre(request.getClienteNombre());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setMetodoPago(request.getMetodoPago());

        List<PedidoProducto> pedidoProductos = new ArrayList<>();
        double total = 0.0;

        for (ProductoPersonalizadoDTO prodDTO : request.getProductos()) {
            Producto productoBase = productoRepository.findById(prodDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + prodDTO.getProductoId()));

            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(productoBase);
            pedidoProducto.setCantidad(prodDTO.getCantidad());

            double precioUnitario = productoBase.getPrecioBase();

            if (prodDTO.getIngredientesAdicionalesIds() != null && !prodDTO.getIngredientesAdicionalesIds().isEmpty()) {
                List<Ingrediente> adicionales = ingredienteRepository.findAllById(prodDTO.getIngredientesAdicionalesIds());
                pedidoProducto.setIngredientesAdicionales(adicionales);
                for (Ingrediente ing : adicionales) {
                    if (ing.getPrecioExtra() != null) {
                        precioUnitario += ing.getPrecioExtra();
                    }
                }
            }

            if (prodDTO.getIngredientesEliminadosIds() != null && !prodDTO.getIngredientesEliminadosIds().isEmpty()) {
                List<Ingrediente> eliminados = ingredienteRepository.findAllById(prodDTO.getIngredientesEliminadosIds());
                pedidoProducto.setIngredientesEliminados(eliminados);
            }

            pedidoProducto.setPrecioUnitario(precioUnitario);
            total += precioUnitario * prodDTO.getCantidad();
            pedidoProductos.add(pedidoProducto);
        }

        pedido.setProductos(pedidoProductos);
        pedido.setTotal(total);

        return pedido;
    }

}