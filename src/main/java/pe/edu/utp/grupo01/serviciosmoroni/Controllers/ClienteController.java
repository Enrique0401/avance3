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

    // ------------------------------------------------------------
    // Mostrar formulario de registro
    // ------------------------------------------------------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register";
    }

    // ------------------------------------------------------------
    // Registrar cliente
    // ------------------------------------------------------------
    @PostMapping("/register")
    public String registerCliente(
            @Valid @ModelAttribute("usuario") Cliente cliente,
            BindingResult result,
            Model model) {

        // 1) Validación de contraseña manual
        if (!cliente.getContrasenaCliente().equals(cliente.getConfirmPassword())) {
            result.rejectValue("confirmPassword",
                    "password.no.match",
                    "Las contraseñas no coinciden");
        }

        // 2) Validaciones automáticas de @Valid
        if (result.hasErrors()) {
            return "register";
        }

        // 3) Validación de unicidad
        if (clienteRepositorio.existsByNumDocumento(cliente.getNumDocumento())) {
            result.rejectValue("numDocumento", "document.exists",
                    "Este número de documento ya está registrado");
            return "register";
        }

        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente())) {
            result.rejectValue("emailCliente", "email.exists",
                    "Este correo ya está registrado");
            return "register";
        }

        if (clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {
            result.rejectValue("telefonoCliente", "phone.exists",
                    "Este teléfono ya está registrado");
            return "register";
        }

        // 4) Encriptar contraseña
        cliente.setContrasenaCliente(
                passwordEncoder.encode(cliente.getContrasenaCliente()));

        // 5) Rol por defecto
        if (cliente.getRol() == null) {
            cliente.setRol("ROLE_USER");
        }

        // 6) Guardar usuario
        clienteRepositorio.save(cliente);

        return "redirect:/login?registrado";
    }

    // ------------------------------------------------------------
    // Perfil del usuario logueado
    // ------------------------------------------------------------
    @GetMapping("/perfil")
    public String verMiPerfil(@AuthenticationPrincipal User user, Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");

        return "perfil";
    }

    // ------------------------------------------------------------
    // Proyectos del cliente
    // ------------------------------------------------------------
    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(
            @AuthenticationPrincipal User user,
            Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("proyectos",
                proyectoRepositorio.findByCliente_IdCliente(cliente.getIdCliente()));

        return "mis-proyectos";
    }

    // ------------------------------------------------------------
    // Mostrar formulario para editar
    // ------------------------------------------------------------
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(
            @AuthenticationPrincipal User user,
            Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");

        return "editarPerfil";
    }

    // ------------------------------------------------------------
    // Actualizar perfil
    // ------------------------------------------------------------
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm) {

        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // Si el usuario ingresó una nueva contraseña
        if (clienteForm.getContrasenaCliente() != null &&
                !clienteForm.getContrasenaCliente().isBlank()) {

            clienteExistente.setContrasenaCliente(
                    passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente);

        return "redirect:/clientes/perfil?actualizado=true";
    }
}
