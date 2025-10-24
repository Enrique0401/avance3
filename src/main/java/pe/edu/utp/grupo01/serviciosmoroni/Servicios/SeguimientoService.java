package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.SeguimientoRepositorio;

import java.util.List;

@Service
public class SeguimientoService {

    @Autowired
    private SeguimientoRepositorio seguimientoRepositorio;

    public List<Seguimiento> listarPorProyecto(Integer idProyecto) {
        return seguimientoRepositorio.findByProyecto_Id(idProyecto);
    }

    public Seguimiento guardar(Seguimiento seguimiento) {
        return seguimientoRepositorio.save(seguimiento);
    }

    public void eliminar(Integer id) {
        seguimientoRepositorio.deleteById(id);
    }
}
