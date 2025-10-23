package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/solicitar")
public class SolicitarProyectoController {

    private final ProyectoRepositorio proyectoRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    public SolicitarProyectoController(ProyectoRepositorio proyectoRepositorio,
            ClienteRepositorio clienteRepositorio) {
        this.proyectoRepositorio = proyectoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("currentPage", "solicitar");
        return "solicitar"; // tu vista HTML
    }

    @PostMapping
    public void enviarSolicitud(@ModelAttribute Proyecto proyecto,
            @AuthenticationPrincipal User user,
            HttpServletResponse response) throws Exception {

        // Buscar el cliente logueado usando Optional
        Optional<Cliente> optionalCliente = clienteRepositorio.findByEmailCliente(user.getUsername());
        Cliente cliente = optionalCliente.orElseThrow(
                () -> new IllegalStateException("Cliente no encontrado para el usuario autenticado"));

        // Asociar proyecto al cliente
        proyecto.setCliente(cliente);

        // Inicializar campos por defecto si es necesario
        if (proyecto.getEstado() == null) {
            proyecto.setEstado("Pendiente");
        }
        if (proyecto.getProgreso() == null) {
            proyecto.setProgreso(0);
        }

        // Guardar proyecto en la base de datos
        proyectoRepositorio.save(proyecto);

        // Configurar el response para descargar PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=solicitud_proyecto.pdf");

        // Generar PDF
        try (OutputStream out = response.getOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Solicitud de Proyecto", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // Datos del proyecto
            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Nombre del Proyecto: " + proyecto.getNombre(), bold));

            String descripcion = (proyecto.getDescripcion() != null) ? proyecto.getDescripcion() : "No proporcionada";
            document.add(new Paragraph("Descripción: " + descripcion, bold));

            String fechaEntrega = (proyecto.getFechaEntrega() != null)
                    ? proyecto.getFechaEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "No definida";
            document.add(new Paragraph("Fecha de Entrega: " + fechaEntrega, bold));

            String categoria = (proyecto.getCategoria() != null) ? proyecto.getCategoria() : "No especificada";
            document.add(new Paragraph("Categoría: " + categoria, bold));

            document.add(new Paragraph("Cliente: " + cliente.getNombreCliente(), bold));

            document.close();
        }
    }
}
