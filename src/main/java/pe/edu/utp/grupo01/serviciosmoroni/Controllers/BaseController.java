package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class BaseController {

    // Atributos globales para todas las vistas
    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        // Current page por defecto vacío
        model.addAttribute("currentPage", "");
        // Puedes agregar otros atributos globales aquí, ejemplo:
        model.addAttribute("siteName", "Servicios Moroni SCRL");
        model.addAttribute("logoPath", "/img/logo.png");
    }
}
