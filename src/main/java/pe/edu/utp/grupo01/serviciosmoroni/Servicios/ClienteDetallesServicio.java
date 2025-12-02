package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

// Importaciones necesarias para trabajar con Spring Security y cargar usuarios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

@Service // Indica que esta clase es un servicio manejado por Spring
public class ClienteDetallesServicio implements UserDetailsService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    // Repositorio para buscar al cliente por su correo

    // Método obligatorio al implementar UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Buscar en la BD un cliente cuyo correo coincida con el ingresado
        Cliente cliente = clienteRepositorio.findByEmailCliente(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        // Si no existe, lanza una excepción que Spring Security toma como error de
        // login

        // Construimos un usuario compatible con Spring Security
        return User.builder()
                .username(cliente.getEmailCliente()) // Establece el username como el email registrado
                .password(cliente.getContrasenaCliente())// Carga la contraseña encriptada guardada en BD
                .roles(cliente.getRol().replace("ROLE_", ""))
                // Spring agrega automáticamente "ROLE_", por eso lo eliminamos
                .build();
    }
}
