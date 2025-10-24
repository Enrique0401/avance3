package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import java.util.List;

@Repository
public interface SeguimientoRepositorio extends JpaRepository<Seguimiento, Integer> {
    List<Seguimiento> findByProyecto_Id(Integer idProyecto);
}
