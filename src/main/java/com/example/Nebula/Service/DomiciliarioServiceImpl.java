package com.example.Nebula.Service;

import com.example.Nebula.Model.Domiciliario;
import com.example.Nebula.Repository.DomiciliarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service

public class DomiciliarioServiceImpl implements DomiciliarioService {

    private final DomiciliarioRepository domiciliarioRepository;

    public DomiciliarioServiceImpl(DomiciliarioRepository domiciliarioRepository) {
        this.domiciliarioRepository = domiciliarioRepository;
    }

    @Override
    @Transactional
    public Domiciliario createDomiciliario(Domiciliario domiciliario) {
        if (domiciliario.getNombre() == null || domiciliario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del domiciliario es obligatorio");
        }
        return domiciliarioRepository.save(domiciliario);
    }

    @Override
    public Domiciliario getDomiciliarioById(Long id) {
        return domiciliarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domiciliario no encontrado con ID: " + id));
    }

    @Override
    public List<Domiciliario> getAllDomiciliarios() {
        return domiciliarioRepository.findAll();
    }

    @Override
    @Transactional
    public Domiciliario updateCalificacion(Long id, Double nuevaCalificacion) {
        if (nuevaCalificacion < 1.0 || nuevaCalificacion > 5.0) {
            throw new RuntimeException("La calificación debe estar entre 1.0 y 5.0");
        }

        Domiciliario domiciliario = getDomiciliarioById(id);
        double promedioActual = domiciliario.getCalificacionPromedio();
        int totalPedidos = domiciliario.getPedidosEntregados();
        double nuevoPromedio = ((promedioActual * totalPedidos) + nuevaCalificacion) / (totalPedidos + 1);
        domiciliario.setCalificacionPromedio(Math.round(nuevoPromedio * 10.0) / 10.0);
        domiciliario.setPedidosEntregados(totalPedidos + 1);

        return domiciliarioRepository.save(domiciliario);
    }

    @Override
    public Domiciliario getDomiciliarioMenosOcupado() {
        List<Domiciliario> domiciliarios = domiciliarioRepository.findByOrderByPedidosEntregadosAsc();
        if (domiciliarios.isEmpty()) {
            throw new RuntimeException("No hay domiciliarios registrados en el sistema");
        }
        return domiciliarios.get(0);
    }

}