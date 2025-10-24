package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.IncidenciaService;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;

import java.util.List;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @GetMapping
    public String listarIncidencias(Model model) {
        List<Incidencia> incidencias = incidenciaService.listarTodas();
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("currentPage", "incidencias");
        return "incidencias"; // apunta al HTML incidencias.html
    }
}
