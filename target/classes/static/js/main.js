// 🔹 Alternar visibilidad de la contraseña
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    if (!input || !icon) return;

    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("bi-eye");
        icon.classList.add("bi-eye-slash");
    } else {
        input.type = "password";
        icon.classList.remove("bi-eye-slash");
        icon.classList.add("bi-eye");
    }
}

// 🔹 Función opcional para validar registro de contraseñas
function validarRegistro(formId, passId, confirmId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function (event) {
        const password = document.getElementById(passId);
        const confirm = document.getElementById(confirmId);

        if (password && confirm && password.value !== confirm.value) {
            confirm.setCustomValidity("Las contraseñas no coinciden");
        } else if (confirm) {
            confirm.setCustomValidity("");
        }
    });
}

// 🟩 Actualiza el estado del proyecto según el progreso
document.addEventListener("DOMContentLoaded", () => {
    const progresoInputs = document.querySelectorAll(".progreso-input");

    progresoInputs.forEach(input => {
        const fila = input.closest("tr");
        const estadoInput = fila.querySelector(".estado-input");

        const actualizarEstado = (valor) => {
            const progreso = parseInt(valor) || 0;
            estadoInput.classList.remove("text-success", "text-warning", "text-info");

            if (progreso === 100) {
                estadoInput.value = "Finalizado";
                estadoInput.classList.add("text-success");
            } else if (progreso > 0 && progreso < 100) {
                estadoInput.value = "En progreso";
                estadoInput.classList.add("text-warning");
            } else {
                estadoInput.value = "Pendiente";
                estadoInput.classList.add("text-info");
            }
        };

        // 🟢 Aplica el estado inicial al cargar la página
        actualizarEstado(input.value);

        // 🟡 Cambia dinámicamente cuando el usuario edita el progreso
        input.addEventListener("input", e => actualizarEstado(e.target.value));
    });
});


document.addEventListener("DOMContentLoaded", () => {
    const barras = document.querySelectorAll(".progress-bar");

    barras.forEach(barra => {
        const porcentaje = parseInt(barra.textContent) || 0;
        barra.classList.remove("bg-danger", "bg-warning", "bg-info", "bg-success");

        if (porcentaje === 100) {
            barra.classList.add("bg-success"); // verde
        } else if (porcentaje >= 70) {
            barra.classList.add("bg-info"); // celeste
        } else if (porcentaje >= 40) {
            barra.classList.add("bg-warning"); // amarillo
        } else {
            barra.classList.add("bg-danger"); // rojo
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    // Añadir sombra a los select
    document.querySelectorAll("select").forEach(sel => sel.classList.add("shadow-sm"));

    // Confirmación de eliminación
    window.confirmarEliminacion = function () {
        return confirm("¿Estás seguro de que deseas eliminar esta incidencia?");
    };

    // Configurar sidebar móvil
    const mobileSidebar = document.getElementById("mobileSidebar");
    if (mobileSidebar) {
        const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(mobileSidebar, {
            backdrop: true,
            scroll: false,
            keyboard: true
        });

        // Cerrar menú al hacer clic en enlace
        mobileSidebar.querySelectorAll(".nav-link").forEach(link => {
            link.addEventListener("click", () => offcanvasInstance.hide());
        });
    }
});
