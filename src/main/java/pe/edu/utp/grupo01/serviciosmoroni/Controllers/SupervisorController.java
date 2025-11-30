package pe.edu.utp.grupo01.serviciosmoroni.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Cliente;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Incidencia;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Proyecto;
import pe.edu.utp.grupo01.serviciosmoroni.Models.Seguimiento;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ClienteRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.IncidenciaRepository;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.ProyectoRepositorio;
import pe.edu.utp.grupo01.serviciosmoroni.Repositories.SeguimientoRepository;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {
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
        return "supervisor/dashboard";
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_USER"));
        return "supervisor/clientes";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal User user, Model model) {
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Perfil");
        return "supervisor/editarPerfil";
    }

    @PostMapping("/perfil")
    public String editarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Obtener el cliente autenticado según su email actual
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // ===============================
        // 🔹 1. Actualizar datos editables
        // ===============================

        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // ===============================
        // 🔹 2. Validación y cambio de email
        // ===============================
        String nuevoEmail = clienteForm.getEmailCliente();

        if (nuevoEmail != null && !nuevoEmail.isBlank()
                && !nuevoEmail.equals(clienteExistente.getEmailCliente())) {

            if (clienteRepositorio.existsByEmailCliente(nuevoEmail)) {
                redirectAttributes.addFlashAttribute("errorEmail", "❌ El correo ya está en uso.");
                return "redirect:/supervisor/perfil";
            }

            clienteExistente.setEmailCliente(nuevoEmail);
        }

        // ===============================
        // 🔹 3. Cambiar contraseña solo si se ingresó una nueva
        // ===============================
        if (clienteForm.getContrasenaCliente() != null && !clienteForm.getContrasenaCliente().isBlank()) {
            clienteExistente.setContrasenaCliente(passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        // ===============================
        // 🔹 4. Mantener campos internos que no deben cambiar
        // ===============================
        if (clienteExistente.getRol() == null) {
            clienteExistente.setRol("ROLE_VISOR"); // asegura que no quede en null
        }

        // Ya NO usas RUC, así que eliminamos esa parte

        // ===============================
        // 🔹 5. Guardar cambios
        // ===============================
        clienteRepositorio.save(clienteExistente);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Perfil actualizado correctamente.");
        return "redirect:/supervisor/dashboard";
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
        model.addAttribute("currentPage", "supervisorProyectos");

        return "supervisor/proyectos";
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
        return "redirect:/supervisor/proyectos";
    }

    @GetMapping("/seguimientos")
    public String listarSeguimientos(Model model) {
        model.addAttribute("titulo", "Seguimientos de Proyectos");
        model.addAttribute("currentPage", "seguimientos");
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("seguimientos", seguimientoRepositorio.findAll());
        return "supervisor/seguimientos";
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
        return "redirect:/supervisor/seguimientos";
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

        return "supervisor/incidencias";
    }

    @GetMapping("/incidencias/nueva")
    public String nuevaIncidencia(Model model) {
        model.addAttribute("incidencia", new Incidencia());
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Registrar Incidencia");
        return "supervisor/formIncidencia";
    }

    @GetMapping("/incidencias/editar/{id}")
    public String editarIncidencia(@PathVariable Integer id, Model model) {
        Incidencia incidencia = incidenciaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalStateException("Incidencia no encontrada"));

        // 🔹 Formatea la fecha (por si necesitas asegurar formato)
        // aunque realmente no es necesario si usas LocalDate
        model.addAttribute("fechaFormateada", incidencia.getFecha().toString());

        model.addAttribute("incidencia", incidencia);
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Editar Incidencia");

        return "supervisor/formIncidencia";
    }

    @PostMapping("/incidencias/guardar")
    public String guardarIncidencia(@Valid @ModelAttribute Incidencia incidencia, BindingResult result) {
        if (result.hasErrors()) {
            return "supervisor/formIncidencia";
        }

        if (incidencia.getFecha() == null) {
            incidencia.setFecha(LocalDate.now());
        }
        incidenciaRepositorio.save(incidencia);
        return "redirect:/supervisor/incidencias";
    }
}
