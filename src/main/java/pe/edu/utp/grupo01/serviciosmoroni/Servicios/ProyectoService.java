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
}
