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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new Cliente());
        return "register";
    }

    @PostMapping("/register")
    public String registerCliente(@ModelAttribute("usuario") Cliente cliente, Model model) {
        if (clienteRepositorio.existsByEmailCliente(cliente.getEmailCliente())
                || clienteRepositorio.existsByRucCliente(cliente.getRucCliente())
                || clienteRepositorio.existsByTelefonoCliente(cliente.getTelefonoCliente())) {

            model.addAttribute("error", true);
            return "register";
        }

        cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));
        cliente.setRol("ROLE_USER");

        clienteRepositorio.save(cliente);

        return "redirect:/login?registrado";
    }

    @GetMapping("/perfil")
    public String verMiPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "perfil";
    }

    @GetMapping("/mis-proyectos")
    public String mostrarMisProyectos(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("proyectos", proyectoRepositorio.findByCliente_IdCliente(cliente.getIdCliente()));
        return "mis-proyectos";
    }


    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditarPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("currentPage", "perfil");
        return "editarPerfil";
    }

    @PostMapping("/editarPerfil")
    public String actualizarPerfil(@AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm) {
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));


        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setEmailCliente(clienteForm.getEmailCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        if (clienteForm.getContrasenaCliente() != null && !clienteForm.getContrasenaCliente().isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente);
        return "redirect:/clientes/perfil";
    }
}
