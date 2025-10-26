package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {

    List<Incidencia> findByProyectoId(Integer proyectoId);

    @Query("SELECT i FROM Incidencia i WHERE i.proyecto.cliente.idCliente = :clienteId")
    List<Incidencia> findByClienteId(Integer clienteId);
}
