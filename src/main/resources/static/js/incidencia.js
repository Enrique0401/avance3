document.addEventListener("DOMContentLoaded", function () {
  const fechaInput = document.getElementById("fecha");
  const form = document.querySelector("form");

  if (!fechaInput || !form) return;

  const hoy = new Date();

  // Fecha máxima: hoy
  const fechaMax = hoy.toISOString().split("T")[0];
  fechaInput.setAttribute("max", fechaMax);

  // Fecha mínima: hace 7 días
  const haceUnaSemana = new Date();
  haceUnaSemana.setDate(hoy.getDate() - 7);
  const fechaMin = haceUnaSemana.toISOString().split("T")[0];
  fechaInput.setAttribute("min", fechaMin);

  // Opcional: si quieres validar también con JavaScript (por si alguien deshabilita los atributos)
  form.addEventListener("submit", function (event) {
    const valor = fechaInput.value;
    if (!valor) {
      event.preventDefault();
      alert("Por favor selecciona una fecha.");
      fechaInput.focus();
      return;
    }

    const seleccionada = new Date(valor);
    const minDate = new Date(fechaMin);
    const maxDate = new Date(fechaMax);

    if (seleccionada < minDate || seleccionada > maxDate) {
      event.preventDefault();
      alert("La fecha debe estar entre hace 7 días y hoy.");
      fechaInput.focus();
    }
  });
});
