document.addEventListener("DOMContentLoaded", function () {
    const fechaInput = document.getElementById("fecha");
    const form = document.querySelector("form");

    if (!fechaInput || !form) return;

    const hoy = new Date();
    hoy.setDate(hoy.getDate() - 1);
    const fechaMinima = hoy.toISOString().split("T")[0];

    fechaInput.setAttribute("min", fechaMinima);

    form.addEventListener("submit", function (event) {
        const fechaSeleccionada = new Date(fechaInput.value);
        const fechaMinimaPermitida = new Date(fechaMinima);

        if (fechaSeleccionada < fechaMinimaPermitida) {
            event.preventDefault();
            alert("⚠️ La fecha no puede ser anterior a ayer.");
            fechaInput.focus();
        }
    });
});
