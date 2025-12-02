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

@Controller // Indica que esta clase es un controlador Spring MVC
@RequestMapping("/contacto") // Ruta base para este módulo (/contacto)
public class ContactoController {

    @Autowired
    private ContactoService contactoService; // Servicio para guardar mensajes de contacto

    // ========================================
    // 🔹 MOSTRAR FORMULARIO DE CONTACTO (GET)
    // ========================================
    @GetMapping
    public String mostrarFormulario(Model model) {

        // Nombre de la página actual (útil para resaltar en el menú)
        model.addAttribute("currentPage", "contacto");

        // Se envía un objeto vacío para que el formulario lo llene
        model.addAttribute("contacto", new Contacto());

        // Atributos globales para mostrar logo y nombre del sitio
        model.addAttribute("logoPath", "/img/logo.png");
        model.addAttribute("siteName", "Servicios Moroni SCRL");

        // Devuelve la vista contacto.html
        return "contacto";
    }

    // ========================================
    // 🔹 RECIBIR MENSAJE DEL FORMULARIO (POST)
    // ========================================
    @PostMapping("/enviar")
    @ResponseBody // La respuesta será JSON, no una vista
    public ResponseEntity<?> enviarFormulario(
            @Valid @RequestBody Contacto contacto, // Recibe los datos validados desde JSON
            BindingResult result) { // Verifica si hay errores de validación

        // ✔️ Validación: si algún campo falla, se devuelve error 400
        if (result.hasErrors()) {
            // Obtiene el primer error de validación encontrado
            String mensajeError = result.getFieldError().getDefaultMessage();

            // Retorna un JSON con el mensaje de error
            return ResponseEntity
                    .badRequest()
                    .body("{\"mensaje\": \"" + mensajeError + "\"}");
        }

        try {
            // ✔️ Si todo está OK, guarda el mensaje en la BD
            contactoService.guardar(contacto);

            // Respuesta exitosa en JSON
            return ResponseEntity
                    .ok()
                    .body("{\"mensaje\": \"¡Mensaje enviado correctamente!\"}");

        } catch (Exception e) {

            e.printStackTrace(); // Muestra error en consola para diagnóstico

            // ❌ Error inesperado → HTTP 500
            return ResponseEntity
                    .status(500)
                    .body("{\"mensaje\": \"Ocurrió un error al enviar el mensaje.\"}");
        }
    }
}
