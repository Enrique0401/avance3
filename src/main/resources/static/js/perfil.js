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

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("editarPerfilForm");
    if (!form) return;

    const telefonoInput = form.querySelector('[name="telefonoCliente"]');
    const passInput = form.querySelector('[name="contrasenaCliente"]');
    const emailInput = form.querySelector('[name="emailCliente"]');
    const toggleIcon = document.getElementById("toggleContrasena");

    // --- Toggle de contraseña ---
    if (passInput && toggleIcon) {
        toggleIcon.addEventListener("click", () => togglePassword("contrasenaCliente", "toggleContrasena"));
    }

    // --- Solo números para teléfono (máx 9) ---
    if (telefonoInput) {
        telefonoInput.addEventListener("input", () => {
            telefonoInput.value = telefonoInput.value.replace(/\D/g, "").slice(0, 9);
        });
    }

    // --- Validación del formulario ---
    form.addEventListener("submit", event => {
        let valido = true;

        // Validar contraseña (mínimo 8 si no está vacía)
        if (passInput && passInput.value && passInput.value.length < 8) {
            passInput.setCustomValidity("La contraseña debe tener al menos 8 caracteres.");
            valido = false;
        } else {
            passInput.setCustomValidity("");
        }

        // Validar correo
        const correoValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailInput.value);
        if (!correoValido) {
            emailInput.setCustomValidity("Correo electrónico inválido.");
            valido = false;
        } else {
            emailInput.setCustomValidity("");
        }

        // Validar teléfono
        const telefonoValido = /^9\d{8}$/.test(telefonoInput.value);
        if (!telefonoValido) {
            telefonoInput.setCustomValidity("El teléfono debe iniciar con 9 y tener 9 dígitos.");
            valido = false;
        } else {
            telefonoInput.setCustomValidity("");
        }

        if (!valido || !form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }

        form.classList.add("was-validated");
    });
});
