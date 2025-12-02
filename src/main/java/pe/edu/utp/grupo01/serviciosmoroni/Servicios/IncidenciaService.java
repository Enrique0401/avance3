package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

// Importaciones necesarias para usar Spring y la entidad Incidencia
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.IncidenciaRepository;

import java.util.List;

@Service // Marca esta clase como un servicio de Spring (lógica de negocio)
public class IncidenciaService {

    @Autowired // Inyección automática del repositorio de incidencias
    private IncidenciaRepository incidenciaRepository;

    /**
     * Lista todas las incidencias pertenecientes a un proyecto específico.
     * Usa el método personalizado del repositorio.
     */
    public List<Incidencia> listarPorProyecto(Integer idProyecto) {
        return incidenciaRepository.findByProyectoId(idProyecto);
    }

    /**
     * Lista todas las incidencias asociadas a un cliente (por medio de sus
     * proyectos).
     * Usa una consulta personalizada definida en el repositorio.
     */
    public List<Incidencia> listarPorCliente(Integer idCliente) {
        return incidenciaRepository.findByClienteId(idCliente);
    }

    /**
     * Guarda o actualiza una incidencia en la base de datos.
     */
    public Incidencia guardar(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    /**
     * Elimina una incidencia por su ID.
     */
    public void eliminar(Integer id) {
        incidenciaRepository.deleteById(id);
    }
}
