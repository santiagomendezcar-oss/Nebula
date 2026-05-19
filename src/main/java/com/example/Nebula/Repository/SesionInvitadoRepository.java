package com.example.Nebula.Repository;

import com.example.Nebula.Model.SesionInvitado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository

public interface SesionInvitadoRepository extends JpaRepository<SesionInvitado, String> {

    Optional<SesionInvitado> findByIdAndActivaTrue(String id);
    List<SesionInvitado> findByActivaTrue();
    List<SesionInvitado> findByActivaTrueAndUltimaActividadBefore(java.time.LocalDateTime fecha);

}