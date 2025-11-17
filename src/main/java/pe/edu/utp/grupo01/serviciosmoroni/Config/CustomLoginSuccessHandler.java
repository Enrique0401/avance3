package pe.edu.utp.grupo01.serviciosmoroni.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component // Marca esta clase como un componente que Spring detectará automáticamente
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    // Repositorio para buscar los datos del cliente
    private final ClienteRepositorio clienteRepositorio;

    // Constructor: Spring inyecta el repositorio aquí
    public CustomLoginSuccessHandler(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    // Este método se ejecuta cuando el login es exitoso
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        // Obtiene el email del usuario autenticado
        String email = authentication.getName();

        // Busca al cliente en la base de datos usando su email
        Optional<Cliente> optionalCliente = clienteRepositorio.findByEmailCliente(email);

        // Verifica si el cliente existe
        if (optionalCliente.isPresent()) {

            // Obtiene los roles del usuario (por ejemplo: ROLE_USER o ROLE_ADMIN)
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            // Si el usuario tiene el rol ADMIN
            if (roles.contains("ROLE_ADMIN")) {
                // Lo redirige al panel de administración
                response.sendRedirect("/admin/dashboard");
            } else if (roles.contains("ROLE_VISOR")) {
                response.sendRedirect("/supervisor/dashboard");
            } else {
                // Si no es admin, lo lleva a su perfil
                response.sendRedirect("/clientes/perfil");
            }

        } else {
            // Si no se encuentra el cliente, lo redirige a la página de inicio
            response.sendRedirect("/inicio");
        }
    }
}
