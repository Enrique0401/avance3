document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("solicitudForm");
    const mensajeExito = document.getElementById("mensajeExito");

    if (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            mensajeExito.style.display = "block";

            setTimeout(() => {
                mensajeExito.style.display = "none";
            }, 4000);

            form.reset();
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const fechaInput = document.getElementById("fechaEntrega");
    const form = document.querySelector("form");

    if (!fechaInput || !form) return;

    const hoy = new Date();
    hoy.setDate(hoy.getDate() + 14);
    const fechaMinima = hoy.toISOString().split("T")[0];
    fechaInput.setAttribute("min", fechaMinima);

    form.addEventListener("submit", function (event) {
        const fechaSeleccionada = new Date(fechaInput.value);
        const fechaMinimaPermitida = new Date(fechaMinima);

        if (fechaSeleccionada < fechaMinimaPermitida) {
            event.preventDefault();
            alert("⚠️ La fecha de entrega debe ser al menos dentro de 2 semanas desde hoy.");
            fechaInput.focus();
        }
    });
});

