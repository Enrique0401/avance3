package pe.edu.utp.grupo01.serviciosmoroni.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.utp.grupo01.serviciosmoroni.Models.*;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.*;

@Service
public class ClienteDetallesServicio implements UserDetailsService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepositorio.findByEmailCliente(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return User.builder()
                .username(cliente.getEmailCliente())
                .password(cliente.getContrasenaCliente())
                .roles(cliente.getRol().replace("ROLE_", ""))
                .build();
    }
}
