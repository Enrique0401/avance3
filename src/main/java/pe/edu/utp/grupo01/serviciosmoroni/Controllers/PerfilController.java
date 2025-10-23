package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

@Controller
public class PerfilController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication authentication) {
        String email = authentication.getName(); // correo del usuario logueado

        Cliente cliente = clienteRepositorio.findByEmailCliente(email)
                .orElse(null);

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");

        return "perfil";
    }
}
