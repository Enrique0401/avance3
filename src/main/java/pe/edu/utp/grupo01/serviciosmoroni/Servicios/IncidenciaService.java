package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.IncidenciaRepositorio;

import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepositorio incidenciaRepositorio;

    public List<Incidencia> listarTodas() {
        return incidenciaRepositorio.findAll();
    }

    public List<Incidencia> listarPorProyecto(Integer idProyecto) {
        return incidenciaRepositorio.findByProyectoId(idProyecto);
    }

    public void guardar(Incidencia incidencia) {
        incidenciaRepositorio.save(incidencia);
    }

    public void eliminar(Integer id) {
        incidenciaRepositorio.deleteById(id);
    }
}
