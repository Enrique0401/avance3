document.addEventListener("DOMContentLoaded", function () {
    AOS.init({
        duration: 1000,
        once: true
    });
});

// Si actualizas el DOM dinámicamente, refresca AOS
function refrescarAOS() {
    AOS.refresh();
}
