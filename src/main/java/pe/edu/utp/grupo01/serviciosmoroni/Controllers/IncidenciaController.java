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

@Controller // Controlador MVC
@RequestMapping("/incidencias") // Ruta base para todas las incidencias
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService; // Servicio que maneja incidencias

    @Autowired
    private ProyectoService proyectoService; // Servicio para proyectos

    @Autowired
    private ClienteServicio clienteService; // Servicio para clientes

    // ============================================================
    // 🔹 Mostrar todas las incidencias del cliente logueado
    // ============================================================
    @GetMapping
    @Transactional(readOnly = true) // Solo lectura para optimizar consultas
    public String verIncidencias(Principal principal, Model model) {

        // Si el usuario no está logueado → redirigir al login
        if (principal == null) {
            return "redirect:/login";
        }

        // Buscar cliente por correo (obtenido del usuario logueado)
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(principal.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();

        // Obtener todas las incidencias del cliente
        List<Incidencia> incidencias = incidenciaService.listarPorCliente(cliente.getIdCliente());

        // Enviar datos a la vista
        model.addAttribute("cliente", cliente);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("currentPage", "incidencias"); // Para resaltar la página actual

        return "incidencias"; // Retorna vista incidencias.html
    }

    // ============================================================
    // 🔹 Ver incidencias filtradas por proyecto específico
    // ============================================================
    @GetMapping("/{idProyecto}")
    @Transactional(readOnly = true)
    public String verIncidenciasPorProyecto(
            @PathVariable Integer idProyecto, // ID obtenido de la URL
            Model model) {

        // Buscar proyecto por ID
        Optional<Proyecto> proyectoOpt = proyectoService.buscarPorId(idProyecto);

        // Si no existe, mostrar mensaje
        if (proyectoOpt.isEmpty()) {
            model.addAttribute("incidencias", List.of());
            model.addAttribute("mensaje", "No se encontró el proyecto especificado.");
            return "incidencias";
        }

        Proyecto proyecto = proyectoOpt.get();

        // Lista de incidencias del proyecto solicitado
        List<Incidencia> incidencias = incidenciaService.listarPorProyecto(idProyecto);

        // Enviar datos a la vista
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("currentPage", "incidencias");

        return "incidencias"; // Muestra todas las incidencias del proyecto
    }
}
