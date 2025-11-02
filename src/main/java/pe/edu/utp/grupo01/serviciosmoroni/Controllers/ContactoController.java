package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ContactoService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    // 🔹 Mostrar formulario
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("currentPage", "contacto");
        model.addAttribute("contacto", new Contacto());
        model.addAttribute("logoPath", "/img/logo.png");
        model.addAttribute("siteName", "Servicios Moroni SCRL");
        return "contacto";
    }

    // 🔹 Recibir el mensaje
    @PostMapping("/enviar")
    @ResponseBody
    public ResponseEntity<?> enviarFormulario(@Valid @RequestBody Contacto contacto, BindingResult result) {
        if (result.hasErrors()) {
            // 🔹 Retorna el primer error de validación detectado
            String mensajeError = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body("{\"mensaje\": \"" + mensajeError + "\"}");
        }

        try {
            contactoService.guardar(contacto);
            return ResponseEntity.ok().body("{\"mensaje\": \"¡Mensaje enviado correctamente!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("{\"mensaje\": \"Ocurrió un error al enviar el mensaje.\"}");
        }
    }
}
