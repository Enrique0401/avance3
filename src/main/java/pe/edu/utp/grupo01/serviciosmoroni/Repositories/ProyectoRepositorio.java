package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.List;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Integer> {

    // 🔹 Buscar todos los proyectos asociados a un cliente
    List<Proyecto> findByCliente(Cliente cliente);

    // 🔹 Buscar por el ID del cliente (usando el nombre real de la FK)
    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
}
