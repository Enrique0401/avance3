package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import java.util.List;

@Repository
public interface ProyectoRepositorio extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByCliente(Cliente cliente);

    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
}
