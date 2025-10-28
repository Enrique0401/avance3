document.addEventListener("DOMContentLoaded", function () {

    AOS.init({ duration: 1000, once: true });

    const form = document.getElementById('contactoForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!form.checkValidity()) {
            form.classList.add('was-validated');
            return;
        }

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

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("contactoForm");
    const alerta = document.getElementById("alerta");

    form.addEventListener("submit", function (e) {
        e.preventDefault();
        alerta.style.display = "none";

        let valido = true;
        const nombre = document.getElementById("nombre");
        const email = document.getElementById("email");
        const servicio = document.getElementById("servicio");
        const mensaje = document.getElementById("mensaje");
        const telefono = document.getElementById("telefono");

        const limpiar = (valor) => valor.trim();

        [nombre, email, servicio, mensaje].forEach(campo => {
            campo.value = limpiar(campo.value);
            if (!campo.value) {
                campo.classList.add("is-invalid");
                valido = false;
            } else {
                campo.classList.remove("is-invalid");
            }
        });

        const telVal = limpiar(telefono.value);
        if (telVal && !/^9\d{8}$/.test(telVal)) {
            telefono.classList.add("is-invalid");
            telefono.nextElementSibling.textContent = "El teléfono debe comenzar con 9 y tener exactamente 9 dígitos.";
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

        if (!valido) {
            alerta.style.display = "block";
            alerta.className = "alert alert-danger";
            alerta.textContent = "Por favor corrija los campos marcados antes de enviar.";
            return;
        }

        alerta.style.display = "block";
        alerta.className = "alert alert-success";
        alerta.textContent = "Mensaje enviado correctamente.";
        form.reset();
    });

    document.getElementById("telefono").addEventListener("input", function () {
        this.value = this.value.replace(/\D/g, "");
        if (this.value.length > 9) {
            this.value = this.value.slice(0, 9);
        }
    });
});