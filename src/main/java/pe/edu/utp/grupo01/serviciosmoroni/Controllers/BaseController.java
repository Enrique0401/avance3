package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice // Permite agregar datos globales a todas las vistas
public class BaseController {

    @ModelAttribute // Se ejecuta antes de cargar cualquier vista
    public void addGlobalAttributes(Model model) {
        model.addAttribute("currentPage", ""); // Página actual (vacía por defecto)
        model.addAttribute("siteName", "Servicios Moroni SCRL"); // Nombre del sitio
        model.addAttribute("logoPath", "/img/logo.png"); // Ruta del logo para todas las vistas
    }
}
