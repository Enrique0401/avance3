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

@Controller // Marca la clase como un controlador MVC
@RequestMapping("/contactoCliente") // Ruta base para todos los endpoints del controlador
public class ContactoClienteController {

    @Autowired
    private ContactoClienteService contactoClienteService; // Servicio que gestiona contacto de clientes

    @Autowired
    private ClienteRepositorio clienteRepositorio; // Repositorio para consultar datos del cliente

    // ===============================
    // ✔️ MOSTRAR FORMULARIO
    // ===============================
    @GetMapping
    public String mostrarFormulario(Model model, @AuthenticationPrincipal User user) {
        // Guarda qué página está activa
        model.addAttribute("currentPage", "contactoCliente");

        // Agrega un objeto vacío para llenar el formulario
        model.addAttribute("contactoCliente", new Contacto());

        // Si el usuario está autenticado, buscamos datos del cliente
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

        // Carga la vista contactoCliente.html
        return "contactoCliente";
    }

    // ===============================
    // ✔️ RECIBIR Y PROCESAR FORMULARIO
    // ===============================
    @PostMapping("/enviar")
    @ResponseBody // Devuelve JSON en vez de recargar la vista
    public ResponseEntity<Map<String, String>> enviarFormulario(
            @Valid @RequestBody Contacto contactoCliente, // Recibe los datos en JSON
            BindingResult result, // Resultado de validaciones
            @AuthenticationPrincipal User user) { // Usuario autenticado

        Map<String, String> response = new HashMap<>();

        // ❗ Si hay errores en la validación del formulario
        if (result.hasErrors()) {
            response.put("mensaje", "❌ Algunos campos son inválidos. Revise los datos e intente nuevamente.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Si el usuario está logueado, completamos automáticamente sus datos
            if (user != null) {
                Cliente cliente = clienteRepositorio
                        .findByEmailCliente(user.getUsername())
                        .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

                contactoCliente.setNombre(cliente.getNombreCliente());
                contactoCliente.setEmail(cliente.getEmailCliente());
                contactoCliente.setTelefono(cliente.getTelefonoCliente());
            }

            // 💾 Guardamos el mensaje en la base de datos
            contactoClienteService.guardar(contactoCliente);

            // Respuesta exitosa en JSON
            response.put("mensaje", "✅ ¡Mensaje enviado correctamente! Gracias por contactarnos.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();

            // Si algo explota, respondemos error 500
            response.put("mensaje", "❌ Ocurrió un error al enviar el mensaje. Intente nuevamente.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
