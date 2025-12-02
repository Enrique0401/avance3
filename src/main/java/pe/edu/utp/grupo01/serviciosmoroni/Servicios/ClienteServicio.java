package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

// Importaciones necesarias para la lógica del servicio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.*;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.*;

import java.util.List;
import java.util.Optional;

@Service // Indica que esta clase es un servicio de Spring (capa lógica)
public class ClienteServicio {

    @Autowired // Inyección del repositorio para acceder a la BD
    private ClienteRepositorio clienteRepositorio;

    // Método para obtener todos los clientes registrados
    public List<Cliente> listarTodos() {
        return clienteRepositorio.findAll();
    }

    // Método para buscar un cliente por su ID (devuelve Optional)
    public Optional<Cliente> obtenerPorId(Integer id) {
        return clienteRepositorio.findById(id);
    }

    // Método para guardar o actualizar un cliente
    public Cliente guardar(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    // Método para eliminar un cliente por ID
    public void eliminar(Integer id) {
        clienteRepositorio.deleteById(id);
    }

    // Método para buscar un cliente según su email
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepositorio.findByEmailCliente(email);
    }
}
