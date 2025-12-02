package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

// Importa JpaRepository para manejar operaciones CRUD automáticas
import org.springframework.data.jpa.repository.JpaRepository;
// Importa @Query para crear consultas personalizadas en JPQL
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Modelo asociado al repositorio
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;

import java.util.List;

@Repository // Indica que esta interfaz es un repositorio de acceso a datos
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Integer> {

    // ============================================
    // 🔹 Buscar seguimientos por el ID del proyecto
    // ============================================
    // Spring generará automáticamente la consulta:
    // SELECT * FROM seguimiento WHERE proyecto_id = :proyectoId
    List<Seguimiento> findByProyectoId(Integer proyectoId);

    // ============================================
    // 🔹 Buscar seguimientos por el ID del cliente
    // ============================================
    // Consulta personalizada en JPQL.
    // Busca seguimientos donde el proyecto pertenece a un cliente específico.
    @Query("SELECT s FROM Seguimiento s WHERE s.proyecto.cliente.idCliente = :clienteId")
    List<Seguimiento> findByClienteId(Integer clienteId);
}
