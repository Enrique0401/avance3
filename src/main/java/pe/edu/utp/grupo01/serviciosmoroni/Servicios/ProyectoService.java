package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un servicio dentro de Spring
public class ProyectoService {

    @Autowired // Inyección automática del repositorio
    private ProyectoRepositorio proyectoRepositorio;

    // ============================
    // 🔹 LISTAR PROYECTOS POR CLIENTE
    // ============================
    public List<Proyecto> listarPorCliente(Integer idCliente) {
        // Devuelve todos los proyectos asociados al ID de un cliente
        return proyectoRepositorio.findByCliente_IdCliente(idCliente);
    }

    // ============================
    // 🔹 BUSCAR PROYECTO POR ID
    // ============================
    public Optional<Proyecto> buscarPorId(Integer id) {
        // Busca un proyecto por su ID de forma segura (Optional)
        return proyectoRepositorio.findById(id);
    }

    // ============================
    // 🔹 LISTAR TODOS LOS PROYECTOS
    // ============================
    public List<Proyecto> listarTodos() {
        // Obtiene todos los registros de la tabla proyecto
        return proyectoRepositorio.findAll();
    }

    // ============================
    // 🔹 GUARDAR NUEVO PROYECTO
    // ============================
    public Proyecto guardarProyecto(Proyecto proyecto) {

        // Si el estado es nulo, le asigna uno por defecto
        if (proyecto.getEstado() == null || proyecto.getEstado().isBlank()) {
            proyecto.setEstado("Pendiente");
        }

        // Si no tiene progreso, lo inicia en 0
        if (proyecto.getProgreso() == null) {
            proyecto.setProgreso(0);
        }

        // Guarda el proyecto en la base de datos
        return proyectoRepositorio.save(proyecto);
    }

    // ============================
    // 🔹 ACTUALIZAR PROYECTO EXISTENTE
    // ============================
    public Proyecto actualizarProyecto(Proyecto proyecto) {

        // Busca el proyecto en BD
        Optional<Proyecto> opt = proyectoRepositorio.findById(proyecto.getId());

        if (opt.isPresent()) {
            // Si existe, obtenemos el objeto original
            Proyecto existente = opt.get();

            // Actualizamos SOLO los campos editables desde la vista
            existente.setNombre(proyecto.getNombre());
            existente.setProgreso(proyecto.getProgreso());

            // Estos campos NO deben modificarse para evitar errores
            existente.setDescripcion(existente.getDescripcion());
            existente.setCategoria(existente.getCategoria());
            existente.setCliente(existente.getCliente());
            existente.setFechaEntrega(existente.getFechaEntrega());

            // Lógica automática del estado según el progreso
            if (proyecto.getProgreso() == 100) {
                existente.setEstado("Finalizado");
            } else if (proyecto.getProgreso() > 0) {
                existente.setEstado("En progreso");
            } else {
                existente.setEstado("Pendiente");
            }

            // Guarda los cambios actualizados
            return proyectoRepositorio.save(existente);

        } else {
            // Si no se encuentra el ID, se lanza un error
            throw new RuntimeException("Proyecto no encontrado con ID: " + proyecto.getId());
        }
    }

    // ============================
    // 🔹 ELIMINAR UN PROYECTO
    // ============================
    public void eliminarProyecto(Integer id) {

        // Verifica si existe antes de eliminar
        if (proyectoRepositorio.existsById(id)) {
            proyectoRepositorio.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar: proyecto no encontrado con ID " + id);
        }
    }
}
