// --- Mostrar/Ocultar contraseña ---
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (!input || !icon) return;

    if (input.type === "password") {
        input.type = "text";
        icon.classList.replace("bi-eye", "bi-eye-slash");
    } else {
        input.type = "password";
        icon.classList.replace("bi-eye-slash", "bi-eye");
    }
}

// --- Todo el código bajo un único DOMContentLoaded ---
document.addEventListener("DOMContentLoaded", () => {

    /* =======================================================
       VALIDACIÓN DE FORMULARIO DE REGISTRO (CON RUC)
    ======================================================= */
    const form = document.querySelector('form[action*="clientes/register"]');
    if (form) {
        const nombre = document.getElementById("nombreCliente");
        const ruc = document.getElementById("rucCliente");
        const telefono = document.getElementById("telefonoCliente");
        const email = document.getElementById("emailCliente");
        const direccion = document.getElementById("direccionCliente");
        const pass = document.getElementById("contrasenaCliente");
        const confirm = document.getElementById("confirmPassword");

        // Solo números para RUC y Teléfono
        const soloNumeros = (input, maxLength) => {
            if (!input) return;
            input.addEventListener("input", function () {
                this.value = this.value.replace(/\D/g, "").slice(0, maxLength);
            });
        };
        soloNumeros(ruc, 11);
        soloNumeros(telefono, 9);

        // Validación al enviar formulario
        form.addEventListener("submit", (e) => {
            let valido = true;
            const limpiar = (v) => v.trim();

            // Validar campos vacíos
            [nombre, ruc, telefono, email, direccion, pass, confirm].forEach(campo => {
                if (!campo) return;
                campo.value = limpiar(campo.value);
                if (!campo.value) {
                    campo.classList.add("is-invalid");
                    valido = false;
                } else {
                    campo.classList.remove("is-invalid");
                }
            });

            // Validar RUC: 11 dígitos
            if (ruc && !/^\d{11}$/.test(ruc.value)) {
                ruc.classList.add("is-invalid");
                valido = false;
            } else if (ruc) {
                ruc.classList.remove("is-invalid");
            }

            // Validar teléfono: 9 dígitos y empieza con 9
            if (telefono && !/^9\d{8}$/.test(telefono.value)) {
                telefono.classList.add("is-invalid");
                valido = false;
            } else if (telefono) {
                telefono.classList.remove("is-invalid");
            }

            // Validar email
            if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
                email.classList.add("is-invalid");
                valido = false;
            } else if (email) {
                email.classList.remove("is-invalid");
            }

            // Validar contraseñas
            if (pass && confirm) {
                if (pass.value !== confirm.value) {
                    confirm.classList.add("is-invalid");
                    confirm.setCustomValidity("Las contraseñas no coinciden");
                    valido = false;
                } else {
                    confirm.classList.remove("is-invalid");
                    confirm.setCustomValidity("");
                }
            }

            if (!valido) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    }

    /* =======================================================
           ESTADO DE PROGRESO EN TABLAS CON COLOR DINÁMICO
    ======================================================= */
    document.querySelectorAll(".progreso-input").forEach(input => {
        const fila = input.closest("tr");
        const estadoInput = fila?.querySelector(".estado-input");
        const progressBar = fila?.querySelector(".progress-bar");

        if (!estadoInput || !progressBar) return;

        const actualizarEstado = (valor) => {
            const progreso = parseInt(valor) || 0;
            estadoInput.classList.remove("text-success", "text-warning", "text-info");
            progressBar.classList.remove("bg-danger", "bg-warning", "bg-success", "bg-secondary");

            if (progreso === 100) {
                estadoInput.value = "Finalizado";
                estadoInput.classList.add("text-success");
                progressBar.classList.add("bg-success");
            } else if (progreso > 0 && progreso < 100) {
                estadoInput.value = "En progreso";
                estadoInput.classList.add("text-warning");
                progressBar.classList.add("bg-warning");
            } else {
                estadoInput.value = "Pendiente";
                estadoInput.classList.add("text-info");
                progressBar.classList.add("bg-danger");
            }

            progressBar.style.width = progreso + "%";
        };

        actualizarEstado(input.value);
        input.addEventListener("input", e => actualizarEstado(e.target.value));
    });

    /* =======================================================
       COLORES DINÁMICOS EN BARRAS DE PROGRESO
    ======================================================= */
    document.querySelectorAll(".progress-bar").forEach(barra => {
        const porcentaje = parseInt(barra.textContent) || 0;
        barra.classList.remove("bg-danger", "bg-warning", "bg-info", "bg-success");

        if (porcentaje === 100) barra.classList.add("bg-success");
        else if (porcentaje >= 70) barra.classList.add("bg-info");
        else if (porcentaje >= 40) barra.classList.add("bg-warning");
        else barra.classList.add("bg-danger");
    });

    /* =======================================================
       SIDEBAR MÓVIL Y SELECTS
    ======================================================= */
    document.querySelectorAll("select").forEach(sel => sel.classList.add("shadow-sm"));

    window.confirmarEliminacion = () => confirm("¿Estás seguro de que deseas eliminar esta incidencia?");

    const mobileSidebar = document.getElementById("mobileSidebar");
    if (mobileSidebar) {
        const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(mobileSidebar, {
            backdrop: true,
            scroll: false,
            keyboard: true
        });
        mobileSidebar.querySelectorAll(".nav-link").forEach(link => {
            link.addEventListener("click", () => offcanvasInstance.hide());
        });
    }

    /* =======================================================
       BOTÓN "VOLVER ARRIBA"
    ======================================================= */
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");
    if (scrollToTopBtn) {
        window.addEventListener("scroll", () => {
            scrollToTopBtn.classList.toggle("d-none", window.scrollY <= 200);
        });

        scrollToTopBtn.addEventListener("click", () => {
            window.scrollTo({ top: 0, behavior: "smooth" });
        });
    }
});
