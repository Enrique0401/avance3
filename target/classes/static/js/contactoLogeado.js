// 🧠 Ejecutar cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", () => {

    // 🌀 Inicializar animaciones AOS
    AOS.init({
        once: true,        // 👈 Se ejecutan solo la primera vez
        duration: 1000,    // (opcional) duración en ms
        easing: "ease-out" // (opcional) transición suave
    });

    // 🧩 Validación con Bootstrap
    (() => {
        'use strict';
        const forms = document.querySelectorAll('.needs-validation');
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();

    // 📬 Envío del formulario
    const form = document.getElementById("contactoForm");
    const alerta = document.getElementById("alerta");
    const telefonoInput = form.telefono;
    const empresaInput = form.empresa;
    const mensajeInput = form.mensaje;

    // 🔢 Validación en tiempo real: solo dígitos y hasta 9 caracteres
    telefonoInput.addEventListener("input", () => {
        telefonoInput.value = telefonoInput.value.replace(/\D/g, ""); // Solo números
        if (telefonoInput.value.length > 9) {
            telefonoInput.value = telefonoInput.value.slice(0, 9);
        }
    });

    // 🏢 Validación para Empresa / Institución
    empresaInput.addEventListener("input", () => {
        if (empresaInput.value.trim() === "") {
            empresaInput.setCustomValidity("Por favor ingrese el nombre de su empresa o institución.");
        } else {
            empresaInput.setCustomValidity("");
        }
    });

    // 💬 Validación para Mensaje
    mensajeInput.addEventListener("input", () => {
        if (mensajeInput.value.trim() === "") {
            mensajeInput.setCustomValidity("Por favor ingrese un mensaje válido.");
        } else {
            mensajeInput.setCustomValidity("");
        }
    });

    // 🚀 Envío con validaciones adicionales
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        // 📞 Validación personalizada del teléfono
        const telefonoVal = telefonoInput.value.trim();
        const telefonoValido = /^9\d{8}$/.test(telefonoVal); // Debe iniciar con 9 y tener 9 dígitos

        if (!telefonoValido) {
            telefonoInput.setCustomValidity("El número debe iniciar con 9 y tener exactamente 9 dígitos.");
        } else {
            telefonoInput.setCustomValidity("");
        }

        // Validaciones manuales para empresa y mensaje al enviar
        if (empresaInput.value.trim() === "") {
            empresaInput.setCustomValidity("Por favor ingrese el nombre de su empresa o institución.");
        } else {
            empresaInput.setCustomValidity("");
        }

        if (mensajeInput.value.trim() === "") {
            mensajeInput.setCustomValidity("Por favor ingrese un mensaje válido.");
        } else {
            mensajeInput.setCustomValidity("");
        }

        // ❌ Si hay errores, mostrar y detener envío
        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        // 📦 Recolectar datos
        const datos = {
            nombre: form.nombre.value.trim(),
            empresa: form.empresa.value.trim(),
            email: form.email.value.trim(),
            telefono: form.telefono.value.trim(),
            servicio: form.servicio.value.trim(),
            mensaje: form.mensaje.value.trim()
        };

        try {
            // 🚀 Enviar datos al backend
            const response = await fetch("/contactoCliente/enviar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(datos)
            });

            const result = await response.json();

            // 📢 Mostrar mensaje
            alerta.style.display = "block";
            alerta.className = response.ok
                ? "alert alert-success mt-3"
                : "alert alert-danger mt-3";

            alerta.textContent = result.mensaje;

            // ✅ Limpiar si se envió correctamente
            if (response.ok) {
                form.reset();
                form.classList.remove('was-validated');
            }

        } catch (error) {
            console.error("❌ Error al enviar:", error);
            alerta.style.display = "block";
            alerta.className = "alert alert-danger mt-3";
            alerta.textContent = "❌ Error al conectar con el servidor.";
        }
    });
});
