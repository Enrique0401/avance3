package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.*;
import pe.edu.utp.grupo01.serviciosmoroni.Models.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteServicio clienteService;

    /**
     * Redirige al seguimiento del primer proyecto del cliente logueado
     */
    @GetMapping
    public String redirigirPrimerProyecto(Principal principal) {
        if (principal == null) {
            // Si no hay usuario logueado, redirigimos al login
            return "redirect:/login";
        }

        // Obtenemos el cliente logueado a partir del email
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(principal.getName());

        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();

        // Si el cliente no tiene proyectos aún, enviamos a la página de proyectos
        if (cliente.getProyectos().isEmpty()) {
            return "redirect:/proyectos";
        }

        // Tomamos el primer proyecto (o podrías aplicar otra lógica)
        Integer idProyecto = cliente.getProyectos().get(0).getId();

        // Redirigimos al seguimiento de ese proyecto
        return "redirect:/seguimiento/" + idProyecto;
    }

    /**
     * Muestra el seguimiento de un proyecto específico
     */
    @GetMapping("/{idProyecto}")
    public String verSeguimiento(@PathVariable Integer idProyecto, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoService.buscarPorId(idProyecto);

        if (proyectoOpt.isEmpty()) {
            model.addAttribute("proyecto", null);
            model.addAttribute("seguimientos", List.of());
            model.addAttribute("mensaje", "No se encontró el proyecto especificado.");
            return "seguimiento";
        }

        Proyecto proyecto = proyectoOpt.get();
        List<Seguimiento> seguimientos = seguimientoService.listarPorProyecto(idProyecto);

        model.addAttribute("proyecto", proyecto);
        model.addAttribute("seguimientos", seguimientos);
        model.addAttribute("currentPage", "seguimiento");

        return "seguimiento";
    }

    /**
     * Guarda un nuevo seguimiento asociado a un proyecto
     */
    @PostMapping("/guardar")
    public String guardarSeguimiento(@ModelAttribute Seguimiento seguimiento,
            @RequestParam Integer idProyecto) {

        Proyecto proyecto = new Proyecto();
        proyecto.setId(idProyecto);
        seguimiento.setProyecto(proyecto);

        seguimientoService.guardar(seguimiento);
        return "redirect:/seguimiento/" + idProyecto;
    }

    /**
     * Elimina un seguimiento por ID
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarSeguimiento(@PathVariable Integer id) {
        seguimientoService.eliminar(id);
        return "redirect:/proyectos";
    }
}
