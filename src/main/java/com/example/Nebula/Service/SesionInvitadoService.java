package com.example.Nebula.Service;

import com.example.Nebula.Model.SesionInvitado;
import java.util.List;

public interface SesionInvitadoService {

    String crearSesionInvitado();
    SesionInvitado validarSesion(String sesionId);
    SesionInvitado getSesionById(String id);
    void cerrarSesion(String sesionId);
    List<SesionInvitado> getSesionesActivas();
    void limpiarSesionesExpiradas();
    void renovarSesion(String sesionId);

}