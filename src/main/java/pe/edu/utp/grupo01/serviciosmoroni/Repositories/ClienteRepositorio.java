package pe.edu.utp.grupo01.serviciosmoroni.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {

    // 🔍 Buscar cliente por correo
    Optional<Cliente> findByEmailCliente(String emailCliente);

    // 🔐 Buscar por correo y contraseña (solo si no usas BCrypt)
    Optional<Cliente> findByEmailClienteAndContrasenaCliente(String emailCliente, String contrasenaCliente);

    // ⚙️ Validaciones de duplicados
    boolean existsByEmailCliente(String emailCliente);

    // 🔹 Cliente tiene numDocumento (DNI o RUC), no existe rucCliente
    boolean existsByNumDocumento(String numDocumento);

    boolean existsByTelefonoCliente(String telefonoCliente);

    // 👥 Buscar por rol
    List<Cliente> findByRol(String rol);
}
