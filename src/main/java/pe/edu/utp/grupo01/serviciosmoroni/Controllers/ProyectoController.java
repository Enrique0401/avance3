package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ProyectoController {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio; // Acceso a proyectos

    @Autowired
    private ClienteRepositorio clienteRepositorio; // Acceso a clientes

    // ============================================================
    // 🔹 Listar los proyectos del cliente logueado
    // ============================================================
    @GetMapping("/proyectos")
    public String listarProyectos(Model model, Authentication auth) {

        // Verifica si el usuario está logueado
        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            return "redirect:/login";
        }

        // Obtiene el email del usuario autenticado
        String email = userDetails.getUsername();

        // Busca al cliente en la base de datos
        Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();

        // Obtiene todos los proyectos de ese cliente
        List<Proyecto> proyectos = proyectoRepositorio.findByCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("currentPage", "proyectos"); // Para resaltar en menú
        return "proyectos"; // Carga la vista proyectos.html
    }

    // ============================================================
    // 🔹 Mostrar formulario para solicitar un nuevo proyecto
    // ============================================================
    @GetMapping("/proyectos/solicitar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proyecto", new Proyecto()); // Objeto vacío para el form
        model.addAttribute("currentPage", "solicitar");
        return "solicitar"; // Vista del formulario
    }

    // ============================================================
    // 🔹 Registrar un proyecto solicitado por el cliente
    // ============================================================
    @PostMapping("/proyectos/solicitar")
    public String registrarProyecto(
            @ModelAttribute Proyecto proyecto,
            @RequestParam(value = "archivos", required = false) MultipartFile[] archivos,
            Authentication auth,
            Model model) {

        // Validar si hay cliente logueado
        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            model.addAttribute("mensajeError", "Debe iniciar sesión para enviar una solicitud.");
            return "solicitar";
        }

        try {
            // Obtener email del usuario autenticado
            String email = userDetails.getUsername();

            // Buscar cliente
            Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
            if (clienteOpt.isEmpty()) {
                model.addAttribute("mensajeError", "No se encontró un cliente con el correo: " + email);
                return "solicitar";
            }

            Cliente cliente = clienteOpt.get();

            // Asociar cliente al proyecto
            proyecto.setCliente(cliente);

            // Valores por defecto
            if (proyecto.getEstado() == null || proyecto.getEstado().isBlank()) {
                proyecto.setEstado("Pendiente");
            }
            if (proyecto.getProgreso() == null) {
                proyecto.setProgreso(0);
            }
            if (proyecto.getFechaEntrega() == null) {
                proyecto.setFechaEntrega(LocalDate.now().plusWeeks(2));
            }

            // Guardar en BD
            proyectoRepositorio.save(proyecto);

            // Mostrar archivos recibidos (si existen)
            if (archivos != null) {
                for (MultipartFile archivo : archivos) {
                    if (!archivo.isEmpty()) {
                        System.out.println("📎 Archivo recibido: " + archivo.getOriginalFilename());
                    }
                }
            }

            return "redirect:/proyectos?exito=true"; // Redirección con mensaje de éxito

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensajeError", "Ocurrió un error al registrar el proyecto: " + e.getMessage());
            return "solicitar";
        }
    }
}
