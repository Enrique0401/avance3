document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("solicitudForm");
    const mensajeExito = document.getElementById("mensajeExito");

    if (form) {
        form.addEventListener("submit", function (event) {
            event.preventDefault(); // Evita recarga

            mensajeExito.style.display = "block";

            setTimeout(() => {
                mensajeExito.style.display = "none";
            }, 4000);

            form.reset();
        });
    }
});
