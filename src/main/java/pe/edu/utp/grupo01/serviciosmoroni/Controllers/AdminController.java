package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pe.edu.utp.grupo01.serviciosmoroni.Models.*;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.*;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    @Autowired
    private SeguimientoRepository seguimientoRepositorio;

    @Autowired
    private IncidenciaRepository incidenciaRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String mostrarPanelAdmin(Model model,
            @ModelAttribute("mensajeExito") String mensajeExito) {
        model.addAttribute("titulo", "Panel de Administración");
        model.addAttribute("mensaje", "Bienvenido al panel del administrador de Servicios Moroni S.C.R.L.");
        if (mensajeExito != null && !mensajeExito.isEmpty()) {
            model.addAttribute("mensajeExito", mensajeExito);
        }
        return "admin/dashboard";
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_USER"));
        return "admin/clientes";
    }

    @GetMapping("/supervisores")
    public String listarSupervisores(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_VISOR"));
        return "admin/supervisores";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Perfil");
        return "admin/editarPerfil";
    }

    @PostMapping("/perfil")
    public String editarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Obtener cliente existente desde la base
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // Actualizar campos permitidos del perfil
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // Permitir cambiar email solo si es distinto y no está ocupado
        String nuevoEmail = clienteForm.getEmailCliente();
        if (nuevoEmail != null && !nuevoEmail.isBlank()
                && !nuevoEmail.equals(clienteExistente.getEmailCliente())) {
            if (clienteRepositorio.existsByEmailCliente(nuevoEmail)) {
                redirectAttributes.addFlashAttribute("errorEmail", "El correo ya está en uso.");
                return "redirect:/clientes/perfil"; // o la ruta correcta según tu vista
            }
            clienteExistente.setEmailCliente(nuevoEmail);
        }

        // Si el usuario ingresa una nueva contraseña, la encriptamos
        String nuevaPass = clienteForm.getContrasenaCliente();
        if (nuevaPass != null && !nuevaPass.isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(nuevaPass));
        }

        // No permitimos cambiar el tipoDocumento o el numDocumento desde este
        // formulario
        // (asumiendo que solo se registra una vez y no se puede modificar después)
        // clienteExistente.setTipoDocumento(...); // NO lo hagas aquí
        // clienteExistente.setNumDocumento(...); // NO lo hagas aquí

        // Guardar cambios
        clienteRepositorio.save(clienteExistente);

        redirectAttributes.addFlashAttribute("mensajeExito", "Perfil actualizado correctamente.");
        return "redirect:/admin/dashboard"; // ajustar según la página a la que quieras volver
    }

    // ==========================
    // Resto de métodos sin cambios
    // ==========================

    @GetMapping("/proyectos")
    public String listarProyectos(@RequestParam(required = false) Integer clienteId, Model model) {
        List<Cliente> clientesUser = clienteRepositorio.findByRol("ROLE_USER");
        List<Proyecto> proyectos;

        if (clienteId != null) {
            proyectos = proyectoRepositorio.findByCliente_IdCliente(clienteId);
        } else {
            proyectos = proyectoRepositorio.findAll()
                    .stream()
                    .filter(p -> p.getCliente() != null && "ROLE_USER".equals(p.getCliente().getRol()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("titulo", "Gestión de Proyectos");
        model.addAttribute("clientesUser", clientesUser);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("selectedClienteId", clienteId);
        model.addAttribute("currentPage", "adminProyectos");

        return "admin/proyectos";
    }

    @PostMapping("/proyectos/actualizar")
    public String actualizarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttributes) {
        // Buscar el proyecto existente
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        // Actualizar solo los campos editables desde la vista
        existente.setNombre(proyecto.getNombre());
        existente.setEstado(proyecto.getEstado());
        existente.setProgreso(proyecto.getProgreso());

        proyectoRepositorio.save(existente);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Proyecto actualizado correctamente.");
        return "redirect:/admin/proyectos";
    }

    @GetMapping("/seguimientos")
    public String listarSeguimientos(Model model) {
        model.addAttribute("titulo", "Seguimientos de Proyectos");
        model.addAttribute("currentPage", "seguimientos");
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("seguimientos", seguimientoRepositorio.findAll());
        return "admin/seguimientos";
    }

    @PostMapping("/seguimientos")
    public String guardarSeguimiento(
            @RequestParam("proyectoId") Integer proyectoId,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("porcentajeAvance") Integer porcentajeAvance,
            RedirectAttributes redirectAttributes) {

        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setProyecto(proyecto);
        seguimiento.setDescripcion(descripcion);
        seguimiento.setPorcentajeAvance(Math.max(0, Math.min(100, porcentajeAvance)));
        seguimiento.setFechaAvance(LocalDate.now());

        seguimientoRepositorio.save(seguimiento);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Seguimiento registrado correctamente.");
        return "redirect:/admin/seguimientos";
    }

    @GetMapping("/incidencias")
    public String listarIncidencias(
            @RequestParam(required = false) Integer proyectoId,
            @RequestParam(required = false) String estado,
            Model model) {

        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        List<Incidencia> incidencias = incidenciaRepositorio.findAll();

        // Filtrar por proyecto
        if (proyectoId != null) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getProyecto() != null && i.getProyecto().getId().equals(proyectoId))
                    .collect(Collectors.toList());
        }

        // Filtrar por estado
        if (estado != null && !estado.isBlank()) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getEstado() != null && i.getEstado().equals(estado))
                    .collect(Collectors.toList());
        }

        model.addAttribute("titulo", "Gestión de Incidencias");
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("incidencias", incidencias);

        // Para que Thymeleaf marque la opción seleccionada
        model.addAttribute("proyectoSeleccionado", proyectoId);
        model.addAttribute("estadoSeleccionado", estado);

        model.addAttribute("currentPage", "incidencias");

        return "admin/incidencias";
    }

}
