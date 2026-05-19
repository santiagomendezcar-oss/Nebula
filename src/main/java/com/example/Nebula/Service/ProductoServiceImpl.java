package com.example.Nebula.Service;

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

}