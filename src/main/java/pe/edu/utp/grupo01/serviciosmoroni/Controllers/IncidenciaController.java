package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ClienteServicio;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.IncidenciaService;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ProyectoService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteServicio clienteService;

    /**
     * 📋 Muestra las incidencias de los proyectos del cliente logueado
     */
    @GetMapping
    @Transactional(readOnly = true)
    public String verIncidencias(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Buscar cliente por email
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(principal.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();

        // ✅ Filtrar incidencias por el cliente logueado
        List<Incidencia> incidencias = incidenciaService.listarPorCliente(cliente.getIdCliente());

        model.addAttribute("cliente", cliente);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("currentPage", "incidencias");

        return "incidencias";
    }

    /**
     * 🔍 Muestra las incidencias de un proyecto específico
     */
    @GetMapping("/{idProyecto}")
    @Transactional(readOnly = true)
    public String verIncidenciasPorProyecto(@PathVariable Integer idProyecto, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoService.buscarPorId(idProyecto);

        if (proyectoOpt.isEmpty()) {
            model.addAttribute("incidencias", List.of());
            model.addAttribute("mensaje", "No se encontró el proyecto especificado.");
            return "incidencias";
        }

        Proyecto proyecto = proyectoOpt.get();
        List<Incidencia> incidencias = incidenciaService.listarPorProyecto(idProyecto);

        model.addAttribute("proyecto", proyecto);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("currentPage", "incidencias");

        return "incidencias";
    }
}
