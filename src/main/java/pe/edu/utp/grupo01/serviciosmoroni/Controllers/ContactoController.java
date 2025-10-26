package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ContactoService;

@Controller
@RequestMapping("/contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("currentPage", "contacto");
        model.addAttribute("contacto", new Contacto());
        return "contacto";
    }

    @PostMapping("/enviar")
    @ResponseBody
    public ResponseEntity<?> enviarFormulario(@RequestBody Contacto contacto) {
        try {
            contactoService.guardar(contacto);
            return ResponseEntity.ok().body("{\"mensaje\":\"¡Mensaje enviado correctamente!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("{\"mensaje\":\"Ocurrió un error, intente nuevamente.\"}");
        }
    }
}
