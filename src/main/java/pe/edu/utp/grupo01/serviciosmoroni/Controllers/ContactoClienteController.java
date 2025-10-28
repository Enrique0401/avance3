package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ContactoClienteService;

@Controller
@RequestMapping("/contactoCliente")
public class ContactoClienteController {

    @Autowired
    private ContactoClienteService contactoClienteService;

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("currentPage", "contactoCliente");
        model.addAttribute("contactoCliente", new Contacto());
        return "contactoCliente";
    }

    @PostMapping("/enviar")
    @ResponseBody
    public ResponseEntity<?> enviarFormulario(@RequestBody Contacto contactoCliente) {
        try {
            contactoClienteService.guardar(contactoCliente);
            return ResponseEntity.ok().body("{\"mensaje\":\"¡Mensaje enviado correctamente!\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("{\"mensaje\":\"Ocurrió un error, intente nuevamente.\"}");
        }
    }
}
