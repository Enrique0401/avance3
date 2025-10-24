package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.IncidenciaRepository;

import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    public List<Incidencia> listarPorProyecto(Integer idProyecto) {
        return incidenciaRepository.findByProyectoId(idProyecto);
    }

    public List<Incidencia> listarPorCliente(Integer idCliente) {
        return incidenciaRepository.findByClienteId(idCliente);
    }

    public Incidencia guardar(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public void eliminar(Integer id) {
        incidenciaRepository.deleteById(id);
    }
}
