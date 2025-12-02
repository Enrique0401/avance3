package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

@Controller // Indica que esta clase maneja rutas web
public class PerfilController {

    @Autowired
    private ClienteRepositorio clienteRepositorio; // Acceso a la tabla clientes

    // ============================================================
    // 🔹 Mostrar página de perfil del cliente logueado
    // ============================================================
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication) {

        // Obtiene el email del usuario actualmente autenticado
        String email = authentication.getName();

        // Busca al cliente por su correo en la base de datos
        Cliente cliente = clienteRepositorio.findByEmailCliente(email).orElse(null);

        // Envía el cliente encontrado a la vista
        model.addAttribute("cliente", cliente);

        // Marca esta página como la actual (para menús, estilos, etc.)
        model.addAttribute("currentPage", "perfil");

        return "perfil"; // Carga perfil.html
    }
}
