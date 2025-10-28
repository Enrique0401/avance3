
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

document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector('form[th\\:action="@{/clientes/register}"]') || document.querySelector("form");
    if (!form) return;

    const nombre = document.getElementById("nombreCliente");
    const ruc = document.getElementById("rucCliente");
    const telefono = document.getElementById("telefonoCliente");
    const email = document.getElementById("emailCliente");
    const direccion = document.getElementById("direccionCliente");
    const pass = document.getElementById("contrasenaCliente");
    const confirm = document.getElementById("confirmPassword");

    form.addEventListener("submit", function (e) {
        let valido = true;

        const limpiar = (v) => v.trim();

        [nombre, ruc, telefono, email, direccion, pass, confirm].forEach(campo => {
            campo.value = limpiar(campo.value);
            if (!campo.value) {
                campo.classList.add("is-invalid");
                valido = false;
            } else {
                campo.classList.remove("is-invalid");
            }
        });

        if (!/^\d{11}$/.test(ruc.value)) {
            ruc.classList.add("is-invalid");
            valido = false;
        } else {
            ruc.classList.remove("is-invalid");
        }

        if (!/^9\d{8}$/.test(telefono.value)) {
            telefono.classList.add("is-invalid");
            valido = false;
        } else {
            telefono.classList.remove("is-invalid");
        }

        const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value);
        if (!emailValido) {
            email.classList.add("is-invalid");
            valido = false;
        } else {
            email.classList.remove("is-invalid");
        }

        if (pass.value !== confirm.value) {
            confirm.classList.add("is-invalid");
            confirm.setCustomValidity("Las contraseñas no coinciden");
            valido = false;
        } else {
            confirm.classList.remove("is-invalid");
            confirm.setCustomValidity("");
        }

        if (!valido) {
            e.preventDefault();
            e.stopPropagation();
        }
    });

    [ruc, telefono].forEach(input => {
        input.addEventListener("input", function () {
            this.value = this.value.replace(/\D/g, ""); // Solo números
            if (this.id === "telefonoCliente" && this.value.length > 9)
                this.value = this.value.slice(0, 9);
            if (this.id === "rucCliente" && this.value.length > 11)
                this.value = this.value.slice(0, 11);
        });
    });
});


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


        actualizarEstado(input.value);


        input.addEventListener("input", e => actualizarEstado(e.target.value));
    });
});


document.addEventListener("DOMContentLoaded", () => {
    const barras = document.querySelectorAll(".progress-bar");

    barras.forEach(barra => {
        const porcentaje = parseInt(barra.textContent) || 0;
        barra.classList.remove("bg-danger", "bg-warning", "bg-info", "bg-success");

        if (porcentaje === 100) {
            barra.classList.add("bg-success");
        } else if (porcentaje >= 70) {
            barra.classList.add("bg-info");
        } else if (porcentaje >= 40) {
            barra.classList.add("bg-warning");
        } else {
            barra.classList.add("bg-danger");
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll("select").forEach(sel => sel.classList.add("shadow-sm"));


    window.confirmarEliminacion = function () {
        return confirm("¿Estás seguro de que deseas eliminar esta incidencia?");
    };


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
});


document.addEventListener("DOMContentLoaded", function () {
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");

    if (!scrollToTopBtn) return;

    window.addEventListener("scroll", function () {
        if (window.scrollY > 200) {
            scrollToTopBtn.classList.remove("d-none");
        } else {
            scrollToTopBtn.classList.add("d-none");
        }
    });

    scrollToTopBtn.addEventListener("click", function () {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });
});
