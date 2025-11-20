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
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteServicio clienteService;


    @GetMapping
    @Transactional(readOnly = true)
    public String verTodosLosSeguimientos(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        
        Optional<Cliente> clienteOpt = clienteService.buscarPorEmail(principal.getName());
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();


        List<Seguimiento> seguimientos = seguimientoService.listarPorCliente(cliente.getIdCliente());


        System.out.println("Cliente logueado: " + cliente.getNombreCliente());
        System.out.println("Total seguimientos cargados: " + seguimientos.size());

        model.addAttribute("cliente", cliente);
        model.addAttribute("seguimientos", seguimientos);
        model.addAttribute("currentPage", "seguimiento");

        return "seguimiento";
    }


    @GetMapping("/{idProyecto}")
    @Transactional(readOnly = true)
    public String verSeguimientoDeProyecto(@PathVariable Integer idProyecto, Model model) {
        Optional<Proyecto> proyectoOpt = proyectoService.buscarPorId(idProyecto);

        if (proyectoOpt.isEmpty()) {
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
}
