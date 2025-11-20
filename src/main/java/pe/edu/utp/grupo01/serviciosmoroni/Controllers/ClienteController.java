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

    // ============================================================
    // 🔹 Mostrar formulario de registro
    // ============================================================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register";
    }

    // ============================================================
    // 🔹 Registrar cliente
    // ============================================================
    @PostMapping("/register")
    public String registerCliente(
            @Valid @ModelAttribute("usuario") Cliente cliente,
            BindingResult result,
            Model model) {

        // 🟦 Validaciones automáticas
        if (result.hasErrors()) {
            return "register";
        }

        // 🟦 Validación de contraseñas
        if (!cliente.getContrasenaCliente().equals(cliente.getConfirmPassword())) {
            model.addAttribute("passwordError", "Las contraseñas no coinciden");
            return "register";
        }

        // 🟦 Validar documento único (DNI o RUC)
        if (clienteRepositorio.existsByNumDocumento(cliente.getNumDocumento())) {
            model.addAttribute("errorDocumento",
                    "El número de " + cliente.getTipoDocumento() + " ya está registrado");
            return "register";
        }

        // 🟦 Validar email único
        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente())) {
            model.addAttribute("errorEmail", "El correo ya está registrado");
            return "register";
        }

        // 🟦 Validar teléfono único
        if (clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {
            model.addAttribute("errorTelefono", "El teléfono ya está registrado");
            return "register";
        }

        // 🟦 Contraseña cifrada
        cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));

        // 🟦 Asignar rol por defecto
        if (cliente.getRol() == null) {
            cliente.setRol("ROLE_USER");
        }

        clienteRepositorio.save(cliente);
        return "redirect:/login?registrado";
    }

    // ============================================================
    // 🔹 Ver perfil del cliente
    // ============================================================
    @GetMapping("/perfil")
    public String verMiPerfil(@AuthenticationPrincipal User user, Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "perfil";
    }

    // ============================================================
    // 🔹 Ver proyectos del cliente
    // ============================================================
    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(@AuthenticationPrincipal User user, Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("proyectos",
                proyectoRepositorio.findByCliente_IdCliente(cliente.getIdCliente()));

        return "mis-proyectos";
    }

    // ============================================================
    // 🔹 Mostrar formulario de edición de perfil
    // ============================================================
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(
            @AuthenticationPrincipal User user, Model model) {

        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "editarPerfil";
    }

    // ============================================================
    // 🔹 Actualizar perfil del cliente
    // ============================================================
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            Model model) {

        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // 🟦 Actualizar solo campos editables
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // 🟦 Si el usuario cambió la contraseña
        if (clienteForm.getContrasenaCliente() != null &&
                !clienteForm.getContrasenaCliente().isBlank()) {

            clienteExistente.setContrasenaCliente(
                    passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente);

        return "redirect:/clientes/perfil?actualizado=true";
    }

}
