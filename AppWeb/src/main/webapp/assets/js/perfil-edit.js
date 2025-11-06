(() => {
  const $ = s => document.querySelector(s);

  // Estado temporal de imagen
  let pendingAvatarObjectURL = null; // para revocar luego

  function toggleEdit(on) {
    $("#profileView")?.classList.toggle("d-none", on);
    $("#profileForm")?.classList.toggle("d-none", !on);
    $("#btnEditProfile")?.classList.toggle("d-none", on);
  }

  // --- Preview de imagen (sin tocar el envío del form)
  function bindAvatarInputs() {
    const file = $("#avatarFile");
    const preview = $("#avatarPreview");
    const btnClear = $("#btnClearPhoto");
    const clearFlag = $("#clearPhoto");

    file?.addEventListener("change", () => {
      const f = file.files?.[0];
      if (!f) return;

      // liberar URL anterior
      if (pendingAvatarObjectURL) URL.revokeObjectURL(pendingAvatarObjectURL);

      pendingAvatarObjectURL = URL.createObjectURL(f);
      preview.src = pendingAvatarObjectURL;
      preview.alt = "Avatar de usuario";
      clearFlag.value = "0"; // si elige archivo, no estamos borrando
    });

    btnClear?.addEventListener("click", () => {
      if (pendingAvatarObjectURL) {
        URL.revokeObjectURL(pendingAvatarObjectURL);
        pendingAvatarObjectURL = null;
      }
      if (file) file.value = "";
      preview.src = "";
      clearFlag.value = "1"; // el servidor debe borrar la foto
    });
  }

  // --- Validaciones de formulario (solo de forma, no tocan envío)
  function bindForm() {
    const form   = $("#profileForm");
    const cancel = $("#btnCancelEdit");
    const btnEdit = $("#btnEditProfile");

    btnEdit?.addEventListener("click", (e) => {
      e.preventDefault();
      toggleEdit(true);
    });

    cancel?.addEventListener("click", () => {
      toggleEdit(false);
      // limpiar estados de imagen
      if (pendingAvatarObjectURL) {
        URL.revokeObjectURL(pendingAvatarObjectURL);
        pendingAvatarObjectURL = null;
      }
      const file = $("#avatarFile");
      if (file) file.value = "";
      $("#clearPhoto").value = "0";
    });

    // IMPORTANTE: no hacemos preventDefault en submit; dejamos que POSTEE
    form?.addEventListener("submit", (e) => {
      // Reglas mínimas de forma
      const nombre = $("#inpNombre")?.value?.trim() || "";
      if (!nombre) {
        e.preventDefault();
        alert("El nombre es obligatorio.");
        return;
      }

      // Password: validar consistencia; la verificación real va en el servlet
      const curr = ($("#pwdCurrent")?.value || "").trim();
      const n1   = ($("#pwdNew")?.value || "").trim();
      const n2   = ($("#pwdNew2")?.value || "").trim();

      const quiereCambio = curr || n1 || n2;

      if (quiereCambio) {
        if (!curr) {
          e.preventDefault();
          alert("Ingresá tu contraseña actual.");
          return;
        }
        if (n1.length < 3) {
          e.preventDefault();
          alert("La nueva contraseña debe tener al menos 3 caracteres.");
          return;
        }
        if (n1 !== n2) {
          e.preventDefault();
          alert("La nueva contraseña no coincide en ambos campos.");
          return;
        }
        if (n1 === curr) {
          e.preventDefault();
          alert("La nueva contraseña debe ser diferente a la actual.");
          return;
        }
      }

      // si todo OK, no hacemos nada: el form se envía al servlet
    });
  }

  document.addEventListener("DOMContentLoaded", () => {
    // Ejecutar sólo si existe el form (evita correr en otras páginas)
    if (!document.getElementById("profileForm")) return;
    bindAvatarInputs();
    bindForm();
  });
})();