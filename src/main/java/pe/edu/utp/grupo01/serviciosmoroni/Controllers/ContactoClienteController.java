package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Contacto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ContactoClienteService;

@Controller
@RequestMapping("/contactoCliente")
public class ContactoClienteController {

    @Autowired
    private ContactoClienteService contactoClienteService;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    // ✅ Mostrar formulario de contacto
    @GetMapping
    public String mostrarFormulario(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("currentPage", "contactoCliente");
        model.addAttribute("contactoCliente", new Contacto());

        if (user != null) {
            Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername()).orElse(null);
            if (cliente != null) {
                model.addAttribute("cliente", cliente);
                System.out.println("👤 Cliente logeado: " + cliente.getNombreCliente());
            } else {
                System.out.println("⚠️ Usuario autenticado pero cliente no encontrado.");
            }
        } else {
            System.out.println("⚠️ No hay cliente logeado, mostrando formulario general...");
        }

        return "contactoCliente"; // HTML que te pasé antes
    }

    // ✅ Enviar formulario (desde HTML o JS)
    @PostMapping("/enviar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> enviarFormulario(
            @Valid @RequestBody Contacto contactoCliente,
            BindingResult result,
            @AuthenticationPrincipal User user) {

        Map<String, String> response = new HashMap<>();

        // 🧩 Validar campos del formulario
        if (result.hasErrors()) {
            response.put("mensaje", "❌ Algunos campos son inválidos. Revise los datos e intente nuevamente.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 🧠 Si el usuario está logueado, rellenamos automáticamente sus datos
            if (user != null) {
                Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                        .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

                contactoCliente.setNombre(cliente.getNombreCliente());
                contactoCliente.setEmail(cliente.getEmailCliente());
                contactoCliente.setTelefono(cliente.getTelefonoCliente());
            }

            // 💾 Guardar en base de datos
            contactoClienteService.guardar(contactoCliente);

            response.put("mensaje", "✅ ¡Mensaje enviado correctamente! Gracias por contactarnos.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "❌ Ocurrió un error al enviar el mensaje. Intente nuevamente.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
