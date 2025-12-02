package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;

import java.util.List;

@Repository // Indica que esta interfaz es un componente de acceso a datos (DAO)
public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    // JpaRepository proporciona CRUD automático para Incidencia

    // 🔹 Obtiene todas las incidencias asociadas a un proyecto por su ID
    List<Incidencia> findByProyectoId(Integer proyectoId);

    // 🔹 Consulta personalizada:
    // Obtiene incidencias filtradas por el ID del cliente dueño del proyecto
    // i.proyecto.cliente.idCliente → navega por las relaciones de la entidad
    @Query("SELECT i FROM Incidencia i WHERE i.proyecto.cliente.idCliente = :clienteId")
    List<Incidencia> findByClienteId(Integer clienteId);
}
