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
    // Muestra el formulario de registro
    // ------------------------------------------------------------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente()); // Objeto vacío para el form
        return "register";
    }

    // ------------------------------------------------------------
    // Registrar cliente nuevo
    // ------------------------------------------------------------
    @PostMapping("/register")
    public String registerCliente(
            @Valid @ModelAttribute("usuario") Cliente cliente,
            BindingResult result,
            Model model) {

        // Si hay errores en el formulario
        if (result.hasErrors()) {
            return "register";
        }

        // Validar que las contraseñas coincidan
        if (!cliente.getContrasenaCliente().equals(cliente.getConfirmPassword())) {
            model.addAttribute("passwordError", "Las contraseñas no coinciden");
            return "register";
        }

        // Documento único
        if (clienteRepositorio.existsByNumDocumento(cliente.getNumDocumento())) {
            model.addAttribute("errorDocumento",
                    "El " + cliente.getTipoDocumento() + " ya está registrado");
            return "register";
        }

        // Email único
        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente())) {
            model.addAttribute("errorEmail", "El correo ya está registrado");
            return "register";
        }

        // Teléfono único
        if (clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {
            model.addAttribute("errorTelefono", "El teléfono ya está registrado");
            return "register";
        }

        // Encriptar contraseña
        cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));

        // Asignar rol por defecto
        if (cliente.getRol() == null) {
            cliente.setRol("ROLE_USER");
        }

        clienteRepositorio.save(cliente); // Guardar cliente

        return "redirect:/login?registrado"; // Redirigir al login
    }

    // ------------------------------------------------------------
    // Mostrar perfil del cliente logueado
    // ------------------------------------------------------------
    @GetMapping("/perfil")
    public String verMiPerfil(@AuthenticationPrincipal User user, Model model) {

        // Buscar cliente por email
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "perfil";
    }

    // ------------------------------------------------------------
    // Mostrar lista de proyectos del cliente
    // ------------------------------------------------------------
    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(@AuthenticationPrincipal User user, Model model) {

        // Buscar cliente actual
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // Obtener proyectos del cliente
        model.addAttribute("proyectos",
                proyectoRepositorio.findByCliente_IdCliente(cliente.getIdCliente()));

        return "mis-proyectos";
    }

    // ------------------------------------------------------------
    // Mostrar formulario para editar perfil
    // ------------------------------------------------------------
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(
            @AuthenticationPrincipal User user, Model model) {

        // Obtener cliente logueado
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");

        return "editarPerfil";
    }

    // ------------------------------------------------------------
    // Actualizar perfil del cliente
    // ------------------------------------------------------------
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            Model model) {

        // Obtener datos actuales del cliente
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // Actualizar campos básicos
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // Si el usuario ingresó una nueva contraseña
        if (clienteForm.getContrasenaCliente() != null &&
                !clienteForm.getContrasenaCliente().isBlank()) {

            clienteExistente.setContrasenaCliente(
                    passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente); // Guardar cambios

        return "redirect:/clientes/perfil?actualizado=true";
    }

}
