package com.example.Nebula.Service;

import com.example.Nebula.Model.Domiciliario;
import java.util.List;

public interface DomiciliarioService {

    Domiciliario createDomiciliario(Domiciliario domiciliario);
    Domiciliario getDomiciliarioById(Long id);
    List<Domiciliario> getAllDomiciliarios();
    Domiciliario updateCalificacion(Long id, Double nuevaCalificacion);
    Domiciliario getDomiciliarioMenosOcupado();

}