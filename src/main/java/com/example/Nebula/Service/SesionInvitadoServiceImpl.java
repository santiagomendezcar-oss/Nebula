package com.example.Nebula.Service;

import com.example.Nebula.Model.SesionInvitado;
import com.example.Nebula.Repository.SesionInvitadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SesionInvitadoServiceImpl implements SesionInvitadoService {

    private final SesionInvitadoRepository sesionInvitadoRepository;

    public SesionInvitadoServiceImpl(SesionInvitadoRepository sesionInvitadoRepository) {
        this.sesionInvitadoRepository = sesionInvitadoRepository;
    }

    @Override
    @Transactional
    public String crearSesionInvitado() {
        String id = UUID.randomUUID().toString();
        SesionInvitado sesion = new SesionInvitado();
        sesion.setId(id);
        sesion.setFechaCreacion(LocalDateTime.now());
        sesion.setUltimaActividad(LocalDateTime.now());
        sesion.setActiva(true);
        sesionInvitadoRepository.save(sesion);
        return id;
    }

    @Override
    public SesionInvitado validarSesion(String sesionId) {
        if (sesionId == null || sesionId.isEmpty()) {
            throw new RuntimeException("ID de sesión inválido");
        }

        SesionInvitado sesion = sesionInvitadoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión de invitado no encontrada"));

        if (!sesion.getActiva()) {
            throw new RuntimeException("Sesión de invitado inactiva");
        }

        // Verificar si la sesión ha expirado (más de 24 horas sin actividad)
        LocalDateTime limite = LocalDateTime.now().minusHours(24);
        if (sesion.getUltimaActividad().isBefore(limite)) {
            sesion.setActiva(false);
            sesionInvitadoRepository.save(sesion);
            throw new RuntimeException("Sesión de invitado expirada");
        }

        return sesion;
    }

    @Override
    public SesionInvitado getSesionById(String id) {
        return sesionInvitadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión de invitado no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public void cerrarSesion(String sesionId) {
        SesionInvitado sesion = getSesionById(sesionId);
        sesion.setActiva(false);
        sesionInvitadoRepository.save(sesion);
    }

    @Override
    public List<SesionInvitado> getSesionesActivas() {
        return sesionInvitadoRepository.findByActivaTrue();
    }

    @Override
    @Transactional
    public void limpiarSesionesExpiradas() {
        LocalDateTime limite = LocalDateTime.now().minusHours(24);
        List<SesionInvitado> sesiones = sesionInvitadoRepository.findByActivaTrue();

        for (SesionInvitado sesion : sesiones) {
            if (sesion.getUltimaActividad().isBefore(limite)) {
                sesion.setActiva(false);
                sesionInvitadoRepository.save(sesion);
            }
        }
    }

    @Override
    @Transactional
    public void renovarSesion(String sesionId) {
        SesionInvitado sesion = validarSesion(sesionId);
        sesion.setUltimaActividad(LocalDateTime.now());
        sesionInvitadoRepository.save(sesion);
    }

}