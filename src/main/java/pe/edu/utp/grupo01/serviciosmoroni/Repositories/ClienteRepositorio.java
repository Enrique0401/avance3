package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.Optional;
import java.util.*;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByEmailCliente(String emailCliente);

    Optional<Cliente> findByEmailClienteAndContrasenaCliente(String emailCliente, String contrasenaCliente);

    boolean existsByEmailCliente(String emailCliente);

    boolean existsByRucCliente(String rucCliente);

    boolean existsByTelefonoCliente(String telefonoCliente);

    List<Cliente> findByRol(String rol);
}
