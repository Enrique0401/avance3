package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.SeguimientoRepository;

import java.util.List;

@Service // Indica que esta clase es un servicio dentro de Spring
public class SeguimientoService {

    @Autowired
    private SeguimientoRepository seguimientoRepository;
    // Inyección automática del repositorio para acceder a la base de datos

    /**
     * Lista todos los seguimientos asociados a un proyecto específico.
     * 
     * @param idProyecto ID del proyecto
     * @return Lista de seguimientos del proyecto
     */
    public List<Seguimiento> listarPorProyecto(Integer idProyecto) {
        return seguimientoRepository.findByProyectoId(idProyecto);
    }

    /**
     * Lista todos los seguimientos que pertenecen a los proyectos de un cliente.
     * 
     * @param idCliente ID del cliente
     * @return Lista de seguimientos relacionados con el cliente
     */
    public List<Seguimiento> listarPorCliente(Integer idCliente) {
        return seguimientoRepository.findByClienteId(idCliente);
    }
}
