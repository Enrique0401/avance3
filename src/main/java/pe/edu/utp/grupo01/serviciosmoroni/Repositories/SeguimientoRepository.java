package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Integer> {

    List<Seguimiento> findByProyectoId(Integer proyectoId);

    @Query("SELECT s FROM Seguimiento s WHERE s.proyecto.cliente.idCliente = :clienteId")
    List<Seguimiento> findByClienteId(Integer clienteId);
}
