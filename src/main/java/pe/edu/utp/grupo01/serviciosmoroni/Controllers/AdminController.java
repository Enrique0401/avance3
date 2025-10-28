package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import pe.edu.utp.grupo01.serviciosmoroni.Models.*;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.*;
import pe.edu.utp.grupo01.serviciosmoroni.Servicios.ProyectoService;

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
    private ProyectoService proyectoService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String mostrarPanelAdmin(Model model) {
        model.addAttribute("titulo", "Panel de Administración");
        model.addAttribute("mensaje", "Bienvenido al panel del administrador de Servicios Moroni S.C.R.L.");
        return "admin/dashboard";
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_USER"));
        return "admin/clientes";
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
    public String editarPerfil(@AuthenticationPrincipal User user,
            @Valid @ModelAttribute("cliente") Cliente clienteForm, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/editarPerfil";
        }

        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setEmailCliente(clienteForm.getEmailCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        if (clienteForm.getContrasenaCliente() != null && !clienteForm.getContrasenaCliente().isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        clienteRepositorio.save(clienteExistente);
        return "redirect:/admin/dashboard";
    }

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
    public String actualizarProyecto(@Valid @ModelAttribute Proyecto proyecto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/proyectos";
        }
        proyectoService.actualizarProyecto(proyecto);
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
    public String guardarSeguimiento(@Valid @ModelAttribute Seguimiento seguimiento, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/seguimientos";
        }

        Proyecto proyecto = proyectoRepositorio.findById(seguimiento.getProyecto().getId())
                .orElse(null);

        if (proyecto != null) {
            if (seguimiento.getPorcentajeAvance() < 0)
                seguimiento.setPorcentajeAvance(0);
            if (seguimiento.getPorcentajeAvance() > 100)
                seguimiento.setPorcentajeAvance(100);

            seguimiento.setProyecto(proyecto);
            seguimiento.setFechaAvance(LocalDate.now());
            seguimientoRepositorio.save(seguimiento);
        }

        return "redirect:/admin/seguimientos";
    }

    @GetMapping("/incidencias")
    public String listarIncidencias(@RequestParam(required = false) Integer proyectoId, Model model) {
        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        List<Incidencia> incidencias;

        if (proyectoId != null) {
            incidencias = incidenciaRepositorio.findByProyectoId(proyectoId);
        } else {
            incidencias = incidenciaRepositorio.findAll();
        }

        model.addAttribute("titulo", "Gestión de Incidencias");
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("proyectoSeleccionado", proyectoId);
        model.addAttribute("currentPage", "incidencias");
        return "admin/incidencias";
    }

    @GetMapping("/incidencias/nueva")
    public String nuevaIncidencia(Model model) {
        model.addAttribute("incidencia", new Incidencia());
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Registrar Incidencia");
        return "admin/formIncidencia";
    }

    @GetMapping("/incidencias/editar/{id}")
    public String editarIncidencia(@PathVariable Integer id, Model model) {
        Incidencia incidencia = incidenciaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalStateException("Incidencia no encontrada"));
        model.addAttribute("incidencia", incidencia);
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Editar Incidencia");
        return "admin/formIncidencia";
    }

    @PostMapping("/incidencias/guardar")
    public String guardarIncidencia(@Valid @ModelAttribute Incidencia incidencia, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/formIncidencia";
        }

        if (incidencia.getFecha() == null) {
            incidencia.setFecha(LocalDate.now());
        }
        incidenciaRepositorio.save(incidencia);
        return "redirect:/admin/incidencias";
    }
}
