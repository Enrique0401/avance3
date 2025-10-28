package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class BaseController {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("currentPage", "");
        model.addAttribute("siteName", "Servicios Moroni SCRL");
        model.addAttribute("logoPath", "/img/logo.png");
    }
}
