package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.*;
import pe.edu.utp.grupo01.serviciosmoroni.Models.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seguimiento") // Ruta base: /seguimiento
public class SeguimientoController {

    // Servicios necesarios para la lógica
    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteServicio clienteService;

    // -------------------------------------------------------------
    // MÉTODO 1: Ver todos los seguimientos del cliente logueado
    // -------------------------------------------------------------
    @GetMapping
    @Transactional(readOnly = true) // Solo lectura, mejora rendimiento
    public String verTodosLosSeguimientos(Principal principal, Model model) {

        // Si el usuario no está autenticado → redirigir al login
        if (principal == null) {
            return "redirect:/login";
        }

        // Buscar al cliente por su email (email = principal.getName())
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(principal.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();

        // Obtener todos los seguimientos asociados a ese cliente
        List<Seguimiento> seguimientos = seguimientoService.listarPorCliente(cliente.getIdCliente());

        // Logs para depurar
        System.out.println("Cliente logueado: " + cliente.getNombreCliente());
        System.out.println("Total seguimientos cargados: " + seguimientos.size());

        // Enviar datos a la vista
        model.addAttribute("cliente", cliente);
        model.addAttribute("seguimientos", seguimientos);
        model.addAttribute("currentPage", "seguimiento"); // Para resaltar menú

        return "seguimiento"; // Renderiza seguimiento.html
    }

    // -------------------------------------------------------------
    // MÉTODO 2: Ver seguimientos de un proyecto específico
    // -------------------------------------------------------------
    @GetMapping("/{idProyecto}")
    @Transactional(readOnly = true)
    public String verSeguimientoDeProyecto(@PathVariable Integer idProyecto, Model model) {

        // Buscar proyecto por su ID
        Optional<Proyecto> proyectoOpt = proyectoService.buscarPorId(idProyecto);

        // Si no existe → mostrar mensaje
        if (proyectoOpt.isEmpty()) {
            model.addAttribute("seguimientos", List.of());
            model.addAttribute("mensaje", "No se encontró el proyecto especificado.");
            return "seguimiento";
        }

        Proyecto proyecto = proyectoOpt.get();

        // Obtener los seguimientos solo de ese proyecto
        List<Seguimiento> seguimientos = seguimientoService.listarPorProyecto(idProyecto);

        // Enviar datos a la vista
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("seguimientos", seguimientos);
        model.addAttribute("currentPage", "seguimiento");

        return "seguimiento"; // Renderiza seguimiento.html
    }
}
