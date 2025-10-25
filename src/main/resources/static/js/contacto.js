document.addEventListener("DOMContentLoaded", function () {
    // Inicializa AOS
    AOS.init({ duration: 1000, once: true });

    // Formulario
    const form = document.getElementById('contactoForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

        // Recolecta datos del formulario
        const data = {
            nombre: form.nombre.value,
            empresa: form.empresa.value,
            email: form.email.value,
            telefono: form.telefono.value,
            servicio: form.servicio.value,
            mensaje: form.mensaje.value
        };

        fetch('/contacto/enviar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(res => res.json())
            .then(res => {
                const alerta = document.getElementById('alerta');
                alerta.style.display = 'block';
                alerta.innerHTML = `<div class="alert alert-success">${res.mensaje}</div>`;
                form.reset();
                form.classList.remove('was-validated');
                AOS.refresh();
            })
            .catch(err => {
                const alerta = document.getElementById('alerta');
                alerta.style.display = 'block';
                alerta.innerHTML = `<div class="alert alert-danger">Ocurrió un error, intente nuevamente.</div>`;
            });
    });
});
