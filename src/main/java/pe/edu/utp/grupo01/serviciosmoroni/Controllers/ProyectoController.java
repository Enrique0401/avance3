package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;


    // 📋 Mostrar lista de proyectos del cliente autenticado
    @GetMapping
    public String listarProyectos(Model model, Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
        if (clienteOpt.isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();
        List<Proyecto> proyectos = proyectoRepositorio.findByCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("currentPage", "proyectos");
        return "proyectos"; // → templates/proyectos.html
    }

    // 🧾 Mostrar formulario de solicitud
    @GetMapping("/solicitar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("currentPage", "solicitar");
        return "solicitar"; // → templates/solicitar.html
    }

    // 🚀 Procesar formulario
    @PostMapping("/solicitar")
    public String registrarProyecto(
            @ModelAttribute Proyecto proyecto,
            @RequestParam(value = "archivos", required = false) MultipartFile[] archivos,
            Authentication auth,
            Model model) {

        if (auth == null || !(auth.getPrincipal() instanceof User userDetails)) {
            model.addAttribute("mensajeError", "Debe iniciar sesión para enviar una solicitud.");
            return "solicitar";
        }

        try {
            String email = userDetails.getUsername();
            Optional<Cliente> clienteOpt = clienteRepositorio.findByEmailCliente(email);
            if (clienteOpt.isEmpty()) {
                model.addAttribute("mensajeError", "No se encontró un cliente con el correo: " + email);
                return "solicitar";
            }

            Cliente cliente = clienteOpt.get();
            proyecto.setCliente(cliente);

            // Valores por defecto
            if (proyecto.getEstado() == null || proyecto.getEstado().isEmpty()) {
                proyecto.setEstado("Pendiente");
            }
            if (proyecto.getProgreso() == null) {
                proyecto.setProgreso(0);
            }
            if (proyecto.getFechaEntrega() == null) {
                proyecto.setFechaEntrega(LocalDate.now().plusWeeks(2));
            }

            // Guardar proyecto
            Proyecto proyectoGuardado = proyectoRepositorio.save(proyecto);

            // Archivos subidos (solo log)
            if (archivos != null) {
                for (MultipartFile archivo : archivos) {
                    if (!archivo.isEmpty()) {
                        System.out.println("📎 Archivo recibido: " + archivo.getOriginalFilename());
                    }
                }
            }

            // ✅ Generar PDF (solo se descarga si el usuario entra al enlace)
            generarPdf(proyectoGuardado.getId());

            // Redirigir a la lista de proyectos
            return "redirect:/proyectos?exito=true";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensajeError", "Ocurrió un error al registrar el proyecto: " + e.getMessage());
            return "solicitar";
        }
    }

    // 🧾 Generar PDF del proyecto
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Integer id) {
        Optional<Proyecto> proyectoOpt = proyectoRepositorio.findById(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proyecto proyecto = proyectoOpt.get();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font subFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font textoFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("SERVICIOS MORONI S.C.R.L.", tituloFont));
            document.add(new Paragraph("Comprobante de Solicitud de Proyecto", subFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Datos del Proyecto", subFont));
            document.add(new Paragraph("ID: " + proyecto.getId(), textoFont));
            document.add(new Paragraph("Nombre: " + proyecto.getNombre(), textoFont));
            document.add(new Paragraph("Descripción: " + proyecto.getDescripcion(), textoFont));
            document.add(new Paragraph("Categoría: " + proyecto.getCategoria(), textoFont));
            document.add(new Paragraph("Estado: " + proyecto.getEstado(), textoFont));
            document.add(new Paragraph("Progreso: " + proyecto.getProgreso() + "%", textoFont));
            document.add(new Paragraph("Fecha de Entrega: " + proyecto.getFechaEntrega(), textoFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Datos del Cliente", subFont));
            document.add(new Paragraph("Nombre: " + proyecto.getCliente().getNombreCliente(), textoFont));
            document.add(new Paragraph("RUC: " + proyecto.getCliente().getRucCliente(), textoFont));
            document.add(new Paragraph("Correo: " + proyecto.getCliente().getEmailCliente(), textoFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por confiar en Servicios Moroni.", textoFont));

            document.close();

            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "solicitud_proyecto_" + proyecto.getId() + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
