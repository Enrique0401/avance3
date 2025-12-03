// --- Mostrar/Ocultar contraseña ---
function togglePassword(inputId, iconId) {
  const input = document.getElementById(inputId);
  const icon = document.getElementById(iconId);
  if (!input || !icon) return;

  if (input.type === "password") {
    input.type = "text";
    icon.classList.replace("bi-eye", "bi-eye-slash");
  } else {
    input.type = "password";
    icon.classList.replace("bi-eye-slash", "bi-eye");
  }
}

// --- Todo el código bajo un único DOMContentLoaded ---
document.addEventListener("DOMContentLoaded", () => {
  const tipoDocSelect = document.getElementById("tipoDocumento");
  const tipoDocLabel = document.getElementById("tipoDocLabel");

  const actualizarLabelDocumento = () => {
    const tipo = tipoDocSelect.value.toUpperCase();
    tipoDocLabel.textContent = tipo;
  };

  tipoDocSelect.addEventListener("change", actualizarLabelDocumento);
  actualizarLabelDocumento(); // Inicializa al cargar

  /* =======================================================
       VALIDACIÓN FORMULARIO REGISTRO (RUC / DNI)
    ======================================================= */

  const form = document.querySelector('form[action*="clientes/register"]');
  if (!form) return;

  const campos = {
    nombre: document.getElementById("nombreCliente"),
    doc: document.getElementById("docCliente"),
    telefono: document.getElementById("telefonoCliente"),
    email: document.getElementById("emailCliente"),
    direccion: document.getElementById("direccionCliente"),
    pass: document.getElementById("contrasenaCliente"),
    confirm: document.getElementById("confirmPassword"),
    tipoDoc: document.getElementById("tipoDocumento"),
  };

  // --- Utilidades ---
  const soloNumeros = (input, max) => {
    if (!input) return;
    input.addEventListener("input", () => {
      input.value = input.value.replace(/\D/g, "").slice(0, max);
    });
  };

  const marcarError = (input, valido) => {
    if (!input) return;
    input.classList.toggle("is-invalid", !valido);
    input.classList.toggle("is-valid", valido);
  };

  // --- Actualizar placeholder y validaciones dinámicamente ---
  const actualizarValidacionDocumento = () => {
    const doc = campos.doc;
    const tipo = campos.tipoDoc.value.toLowerCase(); // ✔ Normalizamos

    if (tipo === "ruc") {
      doc.maxLength = 11;
      doc.pattern = "[0-9]{11}";
      doc.placeholder = "Ingrese su RUC";
    } else {
      doc.maxLength = 8;
      doc.pattern = "[0-9]{8}";
      doc.placeholder = "Ingrese su DNI";
    }

    doc.value = "";
    doc.classList.remove("is-valid", "is-invalid");
  };

  campos.tipoDoc?.addEventListener("change", actualizarValidacionDocumento);
  actualizarValidacionDocumento(); // ✔ Ejecutar al cargar

  soloNumeros(campos.doc, 11);
  soloNumeros(campos.telefono, 9);

  // --- Validación al enviar ---
  form.addEventListener("submit", (e) => {
    let valido = true;

    // Trim
    Object.values(campos).forEach((campo) => {
      if (campo?.tagName === "INPUT") campo.value = campo.value.trim();
    });

    // Nombre y dirección
    ["nombre", "direccion"].forEach((id) => {
      const ok = campos[id].value.length > 0;
      marcarError(campos[id], ok);
      if (!ok) valido = false;
    });

    // Validación DNI/RUC
    const patrones = {
      ruc: /^\d{11}$/,
      dni: /^\d{8}$/,
    };

    const tipo = campos.tipoDoc.value.toLowerCase(); // ✔ Normalizado

    if (!patrones[tipo].test(campos.doc.value)) {
      marcarError(campos.doc, false);
      valido = false;
    } else marcarError(campos.doc, true);

    // Teléfono
    const telOK = /^9\d{8}$/.test(campos.telefono.value);
    marcarError(campos.telefono, telOK);
    if (!telOK) valido = false;

    // Email
    const emailOK = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(campos.email.value);
    marcarError(campos.email, emailOK);
    if (!emailOK) valido = false;

    // Contraseña
    const passOK = campos.pass.value.length >= 6;
    marcarError(campos.pass, passOK);
    if (!passOK) valido = false;

    // Confirmación
    const confirmOK = campos.pass.value === campos.confirm.value;
    marcarError(campos.confirm, confirmOK);
    campos.confirm.setCustomValidity(
      confirmOK ? "" : "Las contraseñas no coinciden"
    );
    if (!confirmOK) valido = false;

    if (!valido) {
      e.preventDefault();
      e.stopPropagation();
    }
  });
});

/* =======================================================
   ESTADO DE PROGRESO EN TABLAS CON COLOR DINÁMICO
======================================================= */
document.querySelectorAll(".progreso-input").forEach((input) => {
  const fila = input.closest("tr");
  const estadoInput = fila.querySelector(".estado-input");
  const progressBar = fila.querySelector(".progress-bar");

  if (!estadoInput || !progressBar) return;

  const actualizarEstado = () => {
    const progreso = parseInt(input.value) || 0;

    // Resetear clases
    estadoInput.className =
      "form-control form-control-sm text-center estado-input";
    progressBar.className = "progress-bar";

    // Estado y color dinámico
    if (progreso === 100) {
      estadoInput.value = "Finalizado";
      estadoInput.classList.add("text-success");
      progressBar.classList.add("bg-success");
    } else if (progreso > 0) {
      estadoInput.value = "En progreso";
      estadoInput.classList.add("text-warning");
      progressBar.classList.add("bg-warning");
    } else {
      estadoInput.value = "Pendiente";
      estadoInput.classList.add("text-info");
      progressBar.classList.add("bg-danger");
    }

    // Actualizar ancho barra
    progressBar.style.width = progreso + "%";
  };

  // Inicializar
  actualizarEstado();

  // Cambiar al escribir
  input.addEventListener("input", actualizarEstado);
});

/* =======================================================
       ESTADO DE PROGRESO EN TABLAS (SOLO INFORMATIVO)
======================================================= */
document.querySelectorAll(".progreso-input").forEach((input) => {
  const fila = input.closest("tr");
  const estadoSpan = fila?.querySelector(".estado-input");
  const progressBar = fila?.querySelector(".progress-bar");

  if (!estadoSpan || !progressBar) return;

  const actualizarEstado = (valor) => {
    const progreso = parseInt(valor) || 0;

    // Quitar estilos previos
    estadoSpan.classList.remove("text-success", "text-warning", "text-info");
    progressBar.classList.remove(
      "bg-danger",
      "bg-warning",
      "bg-success",
      "bg-secondary"
    );

    if (progreso === 100) {
      estadoSpan.textContent = "Finalizado";
      estadoSpan.classList.add("text-success");
      progressBar.classList.add("bg-success");
    } else if (progreso > 0 && progreso < 100) {
      estadoSpan.textContent = "En progreso";
      estadoSpan.classList.add("text-warning");
      progressBar.classList.add("bg-warning");
    } else {
      estadoSpan.textContent = "Pendiente";
      estadoSpan.classList.add("text-info");
      progressBar.classList.add("bg-danger");
    }

    progressBar.style.width = progreso + "%";
  };

  actualizarEstado(input.value);
});

/* =======================================================
   COLORES DINÁMICOS EN BARRAS DE PROGRESO
======================================================= */
document.querySelectorAll(".progress-bar").forEach((barra) => {
  const porcentaje = parseInt(barra.textContent) || 0;
  barra.classList.remove("bg-danger", "bg-warning", "bg-info", "bg-success");

  if (porcentaje === 100) barra.classList.add("bg-success");
  else if (porcentaje >= 70) barra.classList.add("bg-info");
  else if (porcentaje >= 40) barra.classList.add("bg-warning");
  else barra.classList.add("bg-danger");
});

/* =======================================================
   SIDEBAR MÓVIL Y SELECTS
======================================================= */
document
  .querySelectorAll("select")
  .forEach((sel) => sel.classList.add("shadow-sm"));

window.confirmarEliminacion = () =>
  confirm("¿Estás seguro de que deseas eliminar esta incidencia?");

const mobileSidebar = document.getElementById("mobileSidebar");
if (mobileSidebar) {
  const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(
    mobileSidebar,
    {
      backdrop: true,
      scroll: false,
      keyboard: true,
    }
  );
  mobileSidebar.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", () => offcanvasInstance.hide());
  });
}

// Inicializar tooltip de Bootstrap
document.addEventListener("DOMContentLoaded", () => {
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.forEach((el) => {
    // Solo inicializa si Bootstrap está disponible
    if (typeof bootstrap !== "undefined" && bootstrap.Tooltip) {
      new bootstrap.Tooltip(el);
    }
  });
});

/* =======================================================
   BOTÓN "VOLVER ARRIBA"
======================================================= */
const scrollToTopBtn = document.getElementById("scrollToTopBtn");
if (scrollToTopBtn) {
  window.addEventListener("scroll", () => {
    scrollToTopBtn.classList.toggle("d-none", window.scrollY <= 200);
  });

  scrollToTopBtn.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}
