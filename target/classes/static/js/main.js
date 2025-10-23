// 🔹 Alternar visibilidad de la contraseña
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
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

// 🔹 Inicialización: aplicar validación al formulario de registro
document.addEventListener("DOMContentLoaded", function () {
    validarRegistro("registerForm", "contrasenaCliente", "confirmPassword");
});

//flechita
const scrollToTopBtn = document.getElementById('scrollToTopBtn');

window.onscroll = function () {
    if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
        scrollToTopBtn.classList.remove('d-none');
    } else {
        scrollToTopBtn.classList.add('d-none');
    }
};

scrollToTopBtn.addEventListener('click', function () {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

//Contacto mensaje
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('contactForm');
    const successMessage = document.getElementById('successMessage');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // evita recarga SIEMPRE

        if (!form.checkValidity()) {
            // Si el formulario es inválido -> mostrar errores Bootstrap
            event.stopPropagation();
            form.classList.add('was-validated');
        } else {
            // Si es válido -> mostrar mensaje, resetear, ocultar en 5s
            successMessage.style.display = 'block';

            setTimeout(function () {
                successMessage.style.display = 'none';
            }, 5000);

            form.reset();
            form.classList.remove('was-validated');
        }
    });
});

