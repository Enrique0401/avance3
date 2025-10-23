package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;

import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoRepositorio proyectoRepositorio;

    public ProyectoController(ProyectoRepositorio proyectoRepositorio) {
        this.proyectoRepositorio = proyectoRepositorio;
    }

    // Lista los proyectos del cliente logueado
    @GetMapping("")
    public String verProyectos(@AuthenticationPrincipal Cliente cliente, Model model) {
        // Usamos findByCliente que recibe el objeto Cliente directamente
        List<Proyecto> proyectos = proyectoRepositorio.findByCliente(cliente);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("currentPage", "proyectos"); // para el header
        return "proyectos";
    }
}