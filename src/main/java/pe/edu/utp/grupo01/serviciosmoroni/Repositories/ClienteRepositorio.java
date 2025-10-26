package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.Optional;
import java.util.*;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    // Buscar cliente por correo (usado en login y perfil)
    Optional<Cliente> findByEmailCliente(String emailCliente);

    // Buscar cliente por correo y contraseña (si no se usa Spring Security)
    Optional<Cliente> findByEmailClienteAndContrasenaCliente(String emailCliente, String contrasenaCliente);

    // Validaciones de unicidad
    boolean existsByEmailCliente(String emailCliente);

    boolean existsByRucCliente(String rucCliente);

    boolean existsByTelefonoCliente(String telefonoCliente);

    // 🔹 Nuevo método: listar clientes según su rol
    List<Cliente> findByRol(String rol);
}
