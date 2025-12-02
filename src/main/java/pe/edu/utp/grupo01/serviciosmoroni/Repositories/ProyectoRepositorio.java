// Indica que este archivo pertenece al paquete especificado dentro del proyecto
package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

// Importa la interfaz JpaRepository para manejar operaciones CRUD automáticamente
import org.springframework.data.jpa.repository.JpaRepository;
// Indica que este componente será un repositorio reconocido por Spring
import org.springframework.stereotype.Repository;

// Importa la entidad Proyecto, que representa la tabla en la base de datos
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
// Importa la entidad Cliente (relación usada en las consultas)
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.List;

@Repository // Marca esta interfaz como un repositorio Spring para permitir inyección de
            // dependencias
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Integer> {

    // ===========================
    // 🔹 MÉTODO 1: Buscar proyectos por cliente
    // ===========================
    // Busca todos los proyectos asociados a un cliente específico.
    // Spring genera la consulta automáticamente basándose en el nombre del método.
    List<Proyecto> findByCliente(Cliente cliente);

    // ===========================
    // 🔹 MÉTODO 2: Buscar proyectos por ID del cliente
    // ===========================
    // Hace prácticamente lo mismo que el método anterior, pero no requiere pasar un
    // objeto Cliente.
    // Solo necesita el ID del cliente, lo que facilita consultas desde
    // controladores.
    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
}
