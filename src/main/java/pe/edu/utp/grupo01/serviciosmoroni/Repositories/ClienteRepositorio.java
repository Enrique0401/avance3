package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.Optional;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    // Buscar cliente por correo electrónico (devuelve Optional)
    Optional<Cliente> findByEmailCliente(String emailCliente);

    // Buscar cliente por correo electrónico directamente (devuelve Cliente o null)
    Cliente findByEmailClienteAndContrasenaCliente(String emailCliente, String contrasenaCliente);

    // Validar si existe un cliente con el mismo correo electrónico
    boolean existsByEmailCliente(String emailCliente);

    // Validar si existe un cliente con el mismo RUC
    boolean existsByRucCliente(String rucCliente);

    // Validar si existe un cliente con el mismo teléfono
    boolean existsByTelefonoCliente(String telefonoCliente);
}
