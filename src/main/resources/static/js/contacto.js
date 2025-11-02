document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("contactoForm");
    const alerta = document.getElementById("alerta");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        alerta.style.display = "none";

        // === OBTENER Y LIMPIAR VALORES ===
        const nombre = form.nombre.value.trim();
        const empresa = form.empresa.value.trim();
        const email = form.email.value.trim();
        const telefono = form.telefono.value.trim();
        const servicio = form.servicio.value;
        const mensaje = form.mensaje.value.trim();

        // === VALIDACIONES PERSONALIZADAS ===
        if (!nombre || nombre.length === 0) return mostrarError("Por favor, ingrese su nombre completo.");
        if (!empresa || empresa.length === 0) return mostrarError("Por favor, ingrese una empresa o institución válida.");
        if (!validarEmail(email)) return mostrarError("Ingrese un correo electrónico válido.");
        if (!/^9\d{8}$/.test(telefono)) return mostrarError("El teléfono debe iniciar con 9 y tener 9 dígitos numéricos.");
        if (!servicio || servicio === "") return mostrarError("Seleccione un servicio de interés.");
        if (!mensaje || mensaje.length === 0) return mostrarError("Por favor, ingrese su mensaje.");

        // === ENVIAR DATOS ===
        const data = { nombre, empresa, email, telefono, servicio, mensaje };

        try {
            const response = await fetch("/contacto/enviar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (response.ok) {
                mostrarExito(result.mensaje);
                form.reset();

                // 🔹 Limpiar validaciones visuales
                form.classList.remove("was-validated");
                document.querySelectorAll(".is-valid, .is-invalid").forEach(campo => {
                    campo.classList.remove("is-valid", "is-invalid");
                });
            } else {
                mostrarError(result.mensaje || "Ocurrió un error al enviar el mensaje.");
            }
        } catch (error) {
            mostrarError("❌ Error al conectar con el servidor.");
            console.error("Error:", error);
        }
    });

    // === VALIDAR EMAIL ===
    function validarEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    // === MOSTRAR ERROR ===
    function mostrarError(mensaje) {
        alerta.style.display = "block";
        alerta.className = "alert alert-danger mt-3";
        alerta.innerText = mensaje;
        setTimeout(() => (alerta.style.display = "none"), 4000);
    }

    // === MOSTRAR ÉXITO ===
    function mostrarExito(mensaje) {
        alerta.style.display = "block";
        alerta.className = "alert alert-success mt-3";
        alerta.innerText = mensaje;
        setTimeout(() => (alerta.style.display = "none"), 4000);
    }

    // === SOLO NÚMEROS EN TELÉFONO ===
    const inputTel = document.getElementById("telefono");
    inputTel.addEventListener("input", (e) => {
        e.target.value = e.target.value.replace(/[^0-9]/g, "");
        if (e.target.value.length > 9) e.target.value = e.target.value.slice(0, 9);
    });

    // === VALIDACIÓN VISUAL DE CAMPOS DE TEXTO ===
    const camposTexto = ["nombre", "empresa", "mensaje"];
    camposTexto.forEach((id) => {
        const input = document.getElementById(id);
        input.addEventListener("input", () => {
            if (input.value.trim().length === 0) {
                input.classList.add("is-invalid");
                input.classList.remove("is-valid");
            } else {
                input.classList.remove("is-invalid");
                input.classList.add("is-valid");
            }
        });
    });

    // === Inicializar AOS ===
    AOS.init({ once: true });
});
