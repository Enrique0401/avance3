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
@RequestMapping("/supervisor") // Ruta base del módulo supervisor
public class SupervisorController {

    // Inyecciones de dependencias (Repositorios y utilidades)
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

    // ============================================================
    // PANEL PRINCIPAL
    // ============================================================
    @GetMapping("/dashboard")
    public String mostrarPanelAdmin(Model model,
            @ModelAttribute("mensajeExito") String mensajeExito) {

        // Título y mensaje de bienvenida
        model.addAttribute("titulo", "Panel de Administración");
        model.addAttribute("mensaje", "Bienvenido al panel del administrador de Servicios Moroni S.C.R.L.");

        // Si hay mensaje de éxito desde un redirect, mostrarlo
        if (mensajeExito != null && !mensajeExito.isEmpty()) {
            model.addAttribute("mensajeExito", mensajeExito);
        }

        return "supervisor/dashboard";
    }

    // ============================================================
    // LISTAR CLIENTES
    // ============================================================
    @GetMapping("/clientes")
    public String listarClientes(Model model) {

        // Se muestran solo los clientes con rol "ROLE_USER"
        model.addAttribute("titulo", "Gestión de Clientes");
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteRepositorio.findByRol("ROLE_USER"));

        return "supervisor/clientes";
    }

    // ============================================================
    // MOSTRAR PERFIL DEL SUPERVISOR
    // ============================================================
    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal User user, Model model) {

        // Buscar supervisor por email del usuario autenticado
        Cliente cliente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        model.addAttribute("cliente", cliente);
        model.addAttribute("titulo", "Editar Perfil");

        return "supervisor/editarPerfil";
    }

    // ============================================================
    // EDITAR PERFIL DEL SUPERVISOR
    // ============================================================
    @PostMapping("/perfil")
    public String editarPerfil(
            @AuthenticationPrincipal User user,
            @ModelAttribute("cliente") Cliente clienteForm,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Obtener supervisor actual mediante su correo
        Cliente clienteExistente = clienteRepositorio.findByEmailCliente(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cliente no encontrado"));

        // --- 1. Actualizar datos básicos ---
        clienteExistente.setNombreCliente(clienteForm.getNombreCliente());
        clienteExistente.setTelefonoCliente(clienteForm.getTelefonoCliente());
        clienteExistente.setDireccionCliente(clienteForm.getDireccionCliente());

        // --- 2. Validar y cambiar email ---
        String nuevoEmail = clienteForm.getEmailCliente();

        if (nuevoEmail != null && !nuevoEmail.isBlank()
                && !nuevoEmail.equals(clienteExistente.getEmailCliente())) {

            if (clienteRepositorio.existsByEmailCliente(nuevoEmail)) {
                redirectAttributes.addFlashAttribute("errorEmail", "❌ El correo ya está en uso.");
                return "redirect:/supervisor/perfil";
            }

            clienteExistente.setEmailCliente(nuevoEmail);
        }

        // --- 3. Cambiar contraseña solo si se ingresó ---
        if (clienteForm.getContrasenaCliente() != null
                && !clienteForm.getContrasenaCliente().isBlank()) {

            clienteExistente.setContrasenaCliente(
                    passwordEncoder.encode(clienteForm.getContrasenaCliente()));
        }

        // --- 4. Asegurar que el rol no se elimine ---
        if (clienteExistente.getRol() == null) {
            clienteExistente.setRol("ROLE_VISOR");
        }

        // --- 5. Guardar cambios ---
        clienteRepositorio.save(clienteExistente);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Perfil actualizado correctamente.");
        return "redirect:/supervisor/dashboard";
    }

    // ============================================================
    // LISTAR PROYECTOS
    // ============================================================
    @GetMapping("/proyectos")
    public String listarProyectos(@RequestParam(required = false) Integer clienteId, Model model) {

        // Lista de clientes regulares
        List<Cliente> clientesUser = clienteRepositorio.findByRol("ROLE_USER");

        List<Proyecto> proyectos;

        // Si se filtra por cliente
        if (clienteId != null) {
            proyectos = proyectoRepositorio.findByCliente_IdCliente(clienteId);

        } else {
            // Mostrar todos los proyectos de clientes ROLE_USER
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

    // ============================================================
    // ACTUALIZAR PROYECTO (nombre, estado, progreso)
    // ============================================================
    @PostMapping("/proyectos/actualizar")
    public String actualizarProyecto(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttributes) {

        // Buscar proyecto existente
        Proyecto existente = proyectoRepositorio.findById(proyecto.getId())
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        // Actualizar datos editables
        existente.setNombre(proyecto.getNombre());
        existente.setEstado(proyecto.getEstado());
        existente.setProgreso(proyecto.getProgreso());

        proyectoRepositorio.save(existente);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Proyecto actualizado correctamente.");
        return "redirect:/supervisor/proyectos";
    }

    // ============================================================
    // LISTAR TODOS LOS SEGUIMIENTOS
    // ============================================================
    @GetMapping("/seguimientos")
    public String listarSeguimientos(Model model) {

        model.addAttribute("titulo", "Seguimientos de Proyectos");
        model.addAttribute("currentPage", "seguimientos");

        // Enviar todos los proyectos y seguimientos
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("seguimientos", seguimientoRepositorio.findAll());

        return "supervisor/seguimientos";
    }

    // ============================================================
    // GUARDAR SEGUIMIENTO NUEVO
    // ============================================================
    @PostMapping("/seguimientos")
    public String guardarSeguimiento(
            @RequestParam("proyectoId") Integer proyectoId,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("porcentajeAvance") Integer porcentajeAvance,
            RedirectAttributes redirectAttributes) {

        // Buscar proyecto asociado
        Proyecto proyecto = proyectoRepositorio.findById(proyectoId)
                .orElseThrow(() -> new IllegalStateException("Proyecto no encontrado"));

        // Crear objeto seguimiento
        Seguimiento seguimiento = new Seguimiento();
        seguimiento.setProyecto(proyecto);
        seguimiento.setDescripcion(descripcion);

        // asegurar valor entre 0 y 100
        seguimiento.setPorcentajeAvance(Math.max(0, Math.min(100, porcentajeAvance)));
        seguimiento.setFechaAvance(LocalDate.now());

        seguimientoRepositorio.save(seguimiento);

        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Seguimiento registrado correctamente.");
        return "redirect:/supervisor/seguimientos";
    }

    // ============================================================
    // LISTAR INCIDENCIAS CON FILTROS
    // ============================================================
    @GetMapping("/incidencias")
    public String listarIncidencias(
            @RequestParam(required = false) Integer proyectoId,
            @RequestParam(required = false) String estado,
            Model model) {

        List<Proyecto> proyectos = proyectoRepositorio.findAll();
        List<Incidencia> incidencias = incidenciaRepositorio.findAll();

        // Filtro por proyecto
        if (proyectoId != null) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getProyecto() != null && i.getProyecto().getId().equals(proyectoId))
                    .collect(Collectors.toList());
        }

        // Filtro por estado
        if (estado != null && !estado.isBlank()) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getEstado() != null && i.getEstado().equals(estado))
                    .collect(Collectors.toList());
        }

        model.addAttribute("titulo", "Gestión de Incidencias");
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("incidencias", incidencias);

        // Para mantener filtros en la vista
        model.addAttribute("proyectoSeleccionado", proyectoId);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("currentPage", "incidencias");

        return "supervisor/incidencias";
    }

    // ============================================================
    // FORMULARIO NUEVA INCIDENCIA
    // ============================================================
    @GetMapping("/incidencias/nueva")
    public String nuevaIncidencia(Model model) {

        model.addAttribute("incidencia", new Incidencia());
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Registrar Incidencia");

        return "supervisor/formIncidencia";
    }

    // ============================================================
    // FORMULARIO EDITAR INCIDENCIA
    // ============================================================
    @GetMapping("/incidencias/editar/{id}")
    public String editarIncidencia(@PathVariable Integer id, Model model) {

        // Buscar incidencia
        Incidencia incidencia = incidenciaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalStateException("Incidencia no encontrada"));

        // Enviar fecha formateada por si se usa en el input date
        model.addAttribute("fechaFormateada", incidencia.getFecha().toString());

        model.addAttribute("incidencia", incidencia);
        model.addAttribute("proyectos", proyectoRepositorio.findAll());
        model.addAttribute("titulo", "Editar Incidencia");

        return "supervisor/formIncidencia";
    }

    // ============================================================
    // GUARDAR INCIDENCIA (NUEVA O EDITADA)
    // ============================================================
    @PostMapping("/incidencias/guardar")
    public String guardarIncidencia(@Valid @ModelAttribute Incidencia incidencia, BindingResult result) {

        // Validaciones de campos
        if (result.hasErrors()) {
            return "supervisor/formIncidencia";
        }

        // Si no se envía fecha, colocar fecha actual
        if (incidencia.getFecha() == null) {
            incidencia.setFecha(LocalDate.now());
        }

        incidenciaRepositorio.save(incidencia);

        return "redirect:/supervisor/incidencias";
    }
}
