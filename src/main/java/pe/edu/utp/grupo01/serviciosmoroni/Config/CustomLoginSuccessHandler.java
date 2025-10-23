package pe.edu.utp.grupo01.serviciosmoroni.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ClienteRepositorio clienteRepositorio;

    public CustomLoginSuccessHandler(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();

        Optional<Cliente> optionalCliente = clienteRepositorio.findByEmailCliente(email);
        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            response.sendRedirect("/clientes/perfil/" + cliente.getIdCliente());
        } else {
            // Si no se encuentra el cliente, redirigir a inicio
            response.sendRedirect("/inicio");
        }
    }
}
