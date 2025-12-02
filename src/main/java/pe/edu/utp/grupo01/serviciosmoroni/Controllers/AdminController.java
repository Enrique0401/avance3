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

    // Muestra el panel principal del administrador
    @GetMapping("/dashboard")
    public String mostrarPanelAdmin(Model model,
            @ModelAttribute("mensajeExito") String mensajeExito) {
        model.addAttribute("titulo", "Panel de Administración");
        model.addAttribute("mensaje", "Bienvenido al panel del administrador de Servicios Moroni S.C.R.L.");

        // Si hay mensaje de éxito lo envía a la vista
        if (mensajeExito != null && !mensajeExito.isEmpty()) {
            model.addAttribute("mensajeExito", mensajeExito);
        }
        return "admin/dashboard";
    }

    // Lista clientes con rol USER
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_USER"));
        return "admin/clientes";
    }

    // Lista supervisores con rol VISOR
    @GetMapping("/supervisores")
    public String listarSupervisores(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_VISOR"));
        return "admin/supervisores";
    }

    // Muestra los datos del perfil del admin logueado
    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Perfil");
        return "admin/editarPerfil";
    }

    // Actualiza el perfil del administrador
    @PostMapping("/perfil")
    public String editarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Obtiene el cliente actual
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // Actualiza datos básicos
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // Cambia email si es diferente y no está repetido
        String nuevoEmail = clienteForm.getEmailCliente();
        if (nuevoEmail != null && !nuevoEmail.isBlank()
                && !nuevoEmail.equals(clienteExistente.getEmailCliente())) {

            if (clienteRepositorio.existsByEmailCliente(nuevoEmail)) {
                redirectAttributes.addFlashAttribute("errorEmail", "El correo ya está en uso.");
                return "redirect:/clientes/perfil";
            }

            clienteExistente.setEmailCliente(nuevoEmail);
        }

        // Cambia contraseña si el usuario ingresó una nueva
        String nuevaPass = clienteForm.getContrasenaCliente();
        if (nuevaPass != null && !nuevaPass.isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(nuevaPass));
        }

        // Guarda cambios
        clienteRepositorio.save(clienteExistente);

        redirectAttributes.addFlashAttribute("mensajeExito", "Perfil actualizado correctamente.");
        return "redirect:/admin/dashboard";
    }

    // Lista proyectos y permite filtrar por cliente
    @GetMapping("/proyectos")
    public String listarProyectos(@RequestParam(required = false) Integer clienteId, Model model) {
        List<Cliente> clientesUser = clienteRepositorio.findByRol("ROLE_USER");
        List<Proyecto> proyectos;

        // Filtra por cliente si envían el parámetro
        if (clienteId != null) {
            proyectos = proyectoRepositorio.findByCliente_IdCliente(clienteId);
        } else {
            // Muestra todos los proyectos de clientes con rol USER
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

    // Actualiza datos principales del proyecto
    @PostMapping("/proyectos/actualizar")
    public String actualizarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttributes) {

        // Busca proyecto existente
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        // Actualiza los datos editables
        existente.setNombre(proyecto.getNombre());
        existente.setEstado(proyecto.getEstado());
        existente.setProgreso(proyecto.getProgreso());

        proyectoRepositorio.save(existente);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Proyecto actualizado correctamente.");
        return "redirect:/admin/proyectos";
    }

    // Lista todos los seguimientos
    @GetMapping("/seguimientos")
    public String listarSeguimientos(Model model) {
        model.addAttribute("titulo", "Seguimientos de Proyectos");
        model.addAttribute("currentPage", "seguimientos");
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("seguimientos", seguimientoRepositorio.findAll());
        return "admin/seguimientos";
    }

    // Guarda un nuevo seguimiento
    @PostMapping("/seguimientos")
    public String guardarSeguimiento(
            @RequestParam("proyectoId") Integer proyectoId,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("porcentajeAvance") Integer porcentajeAvance,
            RedirectAttributes redirectAttributes) {

        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        Seguimiento seguimiento = new Seguimiento();

        // Asocia datos del seguimiento
        seguimiento.setProyecto(proyecto);
        seguimiento.setDescripcion(descripcion);
        seguimiento.setPorcentajeAvance(Math.max(0, Math.min(100, porcentajeAvance)));
        seguimiento.setFechaAvance(LocalDate.now());

        seguimientoRepositorio.save(seguimiento);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Seguimiento registrado correctamente.");
        return "redirect:/admin/seguimientos";
    }

    // Lista incidencias y permite filtrar por proyecto y estado
    @GetMapping("/incidencias")
    public String listarIncidencias(
            @RequestParam(required = false) Integer proyectoId,
            @RequestParam(required = false) String estado,
            Model model) {

        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        List<Incidencia> incidencias = incidenciaRepositorio.findAll();

        // Filtra por proyecto
        if (proyectoId != null) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getProyecto() != null && i.getProyecto().getId().equals(proyectoId))
                    .collect(Collectors.toList());
        }

        // Filtra por estado
        if (estado != null && !estado.isBlank()) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getEstado() != null && i.getEstado().equals(estado))
                    .collect(Collectors.toList());
        }

        model.addAttribute("titulo", "Gestión de Incidencias");
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("incidencias", incidencias);

        model.addAttribute("proyectoSeleccionado", proyectoId);
        model.addAttribute("estadoSeleccionado", estado);

        model.addAttribute("currentPage", "incidencias");

        return "admin/incidencias";
    }

}
