package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ================= Registro de Cliente =================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String registerCliente(@ModelAttribute("usuario") Cliente cliente, Model model) {

        // Validar unicidad de email, RUC y teléfono
        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente()) ||
                clienteRepositorio.existsByRucCliente(cliente.getRucCliente()) ||
                clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {
            model.addAttribute("error", true);
            return "register";
        }

        // Codificar la contraseña
        cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));

        // Guardar cliente en BD
        clienteRepositorio.save(cliente);

        return "redirect:/login?registrado";
    }

    // ================= Perfil del Cliente =================
    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Integer id, Model model) {
        Cliente cliente = clienteRepositorio.findById(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes/register";
        }
        model.addAttribute("cliente", cliente);
        return "perfil"; // templates/perfil.html
    }

    // ================= Mis Proyectos =================
    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(@AuthenticationPrincipal User user, Model model) {

        // Buscar cliente logueado por email
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // Pasar lista de proyectos al modelo
        model.addAttribute("proyectos", cliente.getProyectos());

        return "mis-proyectos"; // templates/mis-proyectos.html
    }
}
