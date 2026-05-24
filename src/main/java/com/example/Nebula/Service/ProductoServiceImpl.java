package com.example.Nebula.Service;

import com.example.Nebula.DTO.ProductoDTO;
import com.example.Nebula.Model.Producto;
import com.example.Nebula.Model.Ingrediente;
import com.example.Nebula.Repository.ProductoRepository;
import com.example.Nebula.Repository.IngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final IngredienteRepository ingredienteRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, IngredienteRepository ingredienteRepository) {
        this.productoRepository = productoRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> getProductosDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // ========== MÉTODOS LEGACY (existentes) ==========

    @Override
    @Transactional
    public Producto createProducto(Producto producto, List<Long> ingredientesBaseIds) {
        if (ingredientesBaseIds != null && !ingredientesBaseIds.isEmpty()) {
            List<Ingrediente> ingredientes = ingredienteRepository.findAllById(ingredientesBaseIds);
            producto.setIngredientesBase(ingredientes);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto updateProducto(Long id, Producto productoDetails) {
        Producto producto = getProductoById(id);
        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecioBase(productoDetails.getPrecioBase());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setDisponible(productoDetails.getDisponible());
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    // ========== NUEVOS MÉTODOS PARA ADMIN (con DTO) ==========

    @Override
    @Transactional
    public Producto crearProducto(ProductoDTO request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioBase(request.getPrecioBase());
        producto.setCategoria(request.getCategoria());
        producto.setDisponible(request.getDisponible() != null ? request.getDisponible() : true);

        // Asignar ingredientes base si existen
        if (request.getIngredientesBaseIds() != null && !request.getIngredientesBaseIds().isEmpty()) {
            List<Ingrediente> ingredientes = ingredienteRepository.findAllById(request.getIngredientesBaseIds());
            producto.setIngredientesBase(ingredientes);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Long id, ProductoDTO request) {
        Producto producto = getProductoById(id);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioBase(request.getPrecioBase());
        producto.setCategoria(request.getCategoria());
        producto.setDisponible(request.getDisponible() != null ? request.getDisponible() : producto.getDisponible());

        // Actualizar ingredientes base si se proporcionan
        if (request.getIngredientesBaseIds() != null) {
            List<Ingrediente> ingredientes = ingredienteRepository.findAllById(request.getIngredientesBaseIds());
            producto.setIngredientesBase(ingredientes);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Producto cambiarDisponibilidad(Long id, Boolean disponible) {
        Producto producto = getProductoById(id);
        producto.setDisponible(disponible);
        return productoRepository.save(producto);
    }
}