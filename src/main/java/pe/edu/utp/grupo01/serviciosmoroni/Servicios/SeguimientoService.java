package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.SeguimientoRepository;

import java.util.List;

@Service
public class SeguimientoService {

    @Autowired
    private SeguimientoRepository seguimientoRepository;

    public List<Seguimiento> listarPorProyecto(Integer idProyecto) {
        return seguimientoRepository.findByProyectoId(idProyecto);
    }

    public List<Seguimiento> listarPorCliente(Integer idCliente) {
        return seguimientoRepository.findByClienteId(idCliente);
    }
}
