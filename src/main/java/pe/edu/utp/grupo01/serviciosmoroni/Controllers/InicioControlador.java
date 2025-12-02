package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Indica que esta clase maneja rutas de la parte web
public class InicioControlador {

    // ============================================================
    // 🔹 Página de inicio ("/inicio", "/", "")
    // ============================================================
    @GetMapping({ "/inicio", "/", "" }) // Permite varias rutas para la misma vista
    public String index(Model model) {
        model.addAttribute("currentPage", "inicio"); // Marca la página activa
        return "index"; // Carga index.html
    }

    // ============================================================
    // 🔹 Página "Nosotros / Empresa"
    // ============================================================
    @GetMapping("/empresa")
    public String empresa(Model model) {
        model.addAttribute("currentPage", "empresa");
        return "empresa"; // Carga empresa.html
    }

    // ============================================================
    // 🔹 Galería de fotos
    // ============================================================
    @GetMapping("/galeria")
    public String galeria(Model model) {
        model.addAttribute("currentPage", "galeria");
        return "galeria"; // Carga galeria.html
    }

    // ============================================================
    // 🔹 Página de servicios
    // ============================================================
    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("currentPage", "servicios");
        return "servicios"; // Carga servicios.html
    }

    // ============================================================
    // 🔹 Página de inicio de sesión
    // ============================================================
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("currentPage", "login");
        return "login"; // Carga login.html
    }

}
