package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    // 🔍 Buscar cliente por correo electrónico
    Optional<Cliente> findByEmailCliente(String emailCliente);

    // 🔐 Buscar cliente por correo y contraseña (solo si no usas BCrypt
    // directamente)
    Optional<Cliente> findByEmailClienteAndContrasenaCliente(String emailCliente, String contrasenaCliente);

    // ⚙️ Verificar si ya existe un cliente con el correo indicado
    boolean existsByEmailCliente(String emailCliente);

    // ⚙️ Verificar si ya existe un cliente con el RUC indicado
    boolean existsByRucCliente(String rucCliente);

    // ⚙️ Verificar si ya existe un cliente con el teléfono indicado
    boolean existsByTelefonoCliente(String telefonoCliente);

    // 👥 Buscar todos los clientes que tengan un rol específico (por ejemplo: USER
    // o ADMIN)
    List<Cliente> findByRol(String rol);
}
