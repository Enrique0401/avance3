document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("contactoForm");
    form.addEventListener("submit", function (e) {
        e.preventDefault(); // evita recarga
        if (!form.checkValidity()) {
            form.classList.add("was-validated");
            return;
        }

        // Preparar datos
        const data = {
            nombre: form.nombre.value,
            empresa: form.empresa.value,
            email: form.email.value,
            telefono: form.telefono.value,
            servicio: form.servicio.value,
            mensaje: form.mensaje.value
        };

        fetch("/contacto/enviar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(res => res.json())
            .then(res => {
                const alerta = document.getElementById("alerta");
                alerta.style.display = "block";
                alerta.innerHTML = `<div class="alert alert-success">${res.mensaje}</div>`;
                form.reset();
                form.classList.remove("was-validated");
                refrescarAOS(); // actualiza animaciones
            })
            .catch(err => {
                const alerta = document.getElementById("alerta");
                alerta.style.display = "block";
                alerta.innerHTML = `<div class="alert alert-danger">Ocurrió un error, intente nuevamente.</div>`;
            });
    });
});
