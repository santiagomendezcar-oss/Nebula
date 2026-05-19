package com.example.Nebula.Service;

import com.example.Nebula.Model.Producto;
import java.util.List;

public interface ProductoService {

    List<Producto> getAllProductos();
    List<Producto> getProductosDisponibles();
    Producto getProductoById(Long id);
    Producto createProducto(Producto producto, List<Long> ingredientesBaseIds);
    Producto updateProducto(Long id, Producto productoDetails);
    void deleteProducto(Long id);

}