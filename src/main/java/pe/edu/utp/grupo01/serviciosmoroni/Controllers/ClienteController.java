package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 🔹 Mostrar formulario de registro
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register";
    }

    // 🔹 Procesar registro
    @PostMapping("/register")
    public String registerCliente(@Valid @ModelAttribute("usuario") Cliente cliente,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (!cliente.getContrasenaCliente().equals(cliente.getConfirmPassword())) {
            model.addAttribute("passwordError", "Las contraseñas no coinciden");
            return "register";
        }

        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente())
                || clienteRepositorio.existsByRucCliente(cliente.getRucCliente())
                || clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {
            model.addAttribute("error", true);
            return "register";
        }

        cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));
        clienteRepositorio.save(cliente);

        return "redirect:/login?registrado";
    }

    // 🔹 Ver perfil del cliente logueado
    @GetMapping("/perfil")
    public String verMiPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "perfil";
    }

    // 🔹 Ver proyectos del cliente
    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("proyectos", proyectoRepositorio.findByCliente_IdCliente(cliente.getIdCliente()));
        return "mis-proyectos";
    }

    // 🔹 Mostrar formulario para editar perfil
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "editarPerfil";
    }

    // 🔹 Procesar actualización de perfil
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(@AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm, Model model) {

        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // ✅ Actualizar solo campos editables
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // ✅ Si el usuario ingresó una nueva contraseña, la encriptamos
        if (clienteForm.getContrasenaCliente() != null && !clienteForm.getContrasenaCliente().isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente);

        // ✅ Redirige al perfil mostrando el mensaje de éxito
        return "redirect:/clientes/perfil?actualizado=true";
    }

}
