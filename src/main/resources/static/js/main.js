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
