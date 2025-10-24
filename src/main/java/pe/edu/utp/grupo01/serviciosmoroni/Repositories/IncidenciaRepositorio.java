package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;

import java.util.List;

@Repository
public interface IncidenciaRepositorio extends JpaRepository<Incidencia, Integer> {
    List<Incidencia> findByProyectoId(Integer idProyecto);
}
