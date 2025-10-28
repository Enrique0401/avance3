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
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;


    @GetMapping("/proyectos")
    public String listarProyectos(Model model, Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();
        List<Proyecto> proyectos = proyectoRepositorio.findByCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("currentPage", "proyectos");
        return "proyectos";
    }


    @GetMapping("/proyectos/solicitar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("currentPage", "solicitar");
        return "solicitar";
    }


    @PostMapping("/proyectos/solicitar")
    public String registrarProyecto(
            @ModelAttribute Proyecto proyecto,
            @RequestParam(value = "archivos", required = false) MultipartFile[] archivos,
            Authentication auth,
            Model model) {

        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            model.addAttribute("mensajeError", "Debe iniciar sesión para enviar una solicitud.");
            return "solicitar";
        }

        try {
            String email = userDetails.getUsername();
            Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
            if (clienteOpt.isEmpty()) {
                model.addAttribute("mensajeError", "No se encontró un cliente con el correo: " + email);
                return "solicitar";
            }

            Cliente cliente = clienteOpt.get();
            proyecto.setCliente(cliente);


            if (proyecto.getEstado() == null || proyecto.getEstado().isBlank()) {
                proyecto.setEstado("Pendiente");
            }
            if (proyecto.getProgreso() == null) {
                proyecto.setProgreso(0);
            }
            if (proyecto.getFechaEntrega() == null) {
                proyecto.setFechaEntrega(LocalDate.now().plusWeeks(2));
            }

            proyectoRepositorio.save(proyecto);

            
            if (archivos != null) {
                for (MultipartFile archivo : archivos) {
                    if (!archivo.isEmpty()) {
                        System.out.println("📎 Archivo recibido: " + archivo.getOriginalFilename());
                    }
                }
            }

            return "redirect:/proyectos?exito=true";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensajeError", "Ocurrió un error al registrar el proyecto: " + e.getMessage());
            return "solicitar";
        }
    }
}
