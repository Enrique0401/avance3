// static/js/validacion.js
(() => {
    'use strict'

    const forms = document.querySelectorAll('.needs-validation')

    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                // Si NO es válido, se bloquea el envío
                event.preventDefault()
                event.stopPropagation()
            } else {
                // Si es válido, mostramos mensaje y prevenimos envío real
                event.preventDefault()
                const success = document.getElementById("successMessage");
                success.style.display = "block";

                // Ocultar después de 5s
                setTimeout(() => {
                    success.style.display = "none";
                }, 5000);

                form.reset()
                form.classList.remove('was-validated');
            }

            form.classList.add('was-validated')
        }, false)
    })
})()
