package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    public List<Proyecto> listarPorCliente(Integer idCliente) {
        return proyectoRepositorio.findByCliente_IdCliente(idCliente);
    }

    public Optional<Proyecto> buscarPorId(Integer id) {
        return proyectoRepositorio.findById(id);
    }

    public List<Proyecto> listarTodos() {
        return proyectoRepositorio.findAll();
    }

    public Proyecto guardarProyecto(Proyecto proyecto) {
        if (proyecto.getEstado() == null || proyecto.getEstado().isBlank()) {
            proyecto.setEstado("Pendiente");
        }
        if (proyecto.getProgreso() == null) {
            proyecto.setProgreso(0);
        }
        return proyectoRepositorio.save(proyecto);
    }

    public Proyecto actualizarProyecto(Proyecto proyecto) {
        Optional<Proyecto> opt = proyectoRepositorio.findById(proyecto.getId());

        if (opt.isPresent()) {
            Proyecto existente = opt.get();

            existente.setNombre(proyecto.getNombre());
            existente.setProgreso(proyecto.getProgreso());

            // 🟢 Mantener los campos no editados
            existente.setDescripcion(existente.getDescripcion());
            existente.setCategoria(existente.getCategoria());
            existente.setCliente(existente.getCliente());
            existente.setFechaEntrega(existente.getFechaEntrega());

            // 🟩 Actualiza el estado automáticamente
            if (proyecto.getProgreso() == 100) {
                existente.setEstado("Finalizado");
            } else if (proyecto.getProgreso() > 0) {
                existente.setEstado("En progreso");
            } else {
                existente.setEstado("Pendiente");
            }

            return proyectoRepositorio.save(existente);
        } else {
            throw new RuntimeException("Proyecto no encontrado con ID: " + proyecto.getId());
        }
    }

    public void eliminarProyecto(Integer id) {
        if (proyectoRepositorio.existsById(id)) {
            proyectoRepositorio.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar: proyecto no encontrado con ID " + id);
        }
    }
}
