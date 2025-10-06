(() => {
  const $ = s => document.querySelector(s);

  // Solo archivo local
  let pendingAvatarDataURL = null;   // dataURL de la imagen elegida (temporal)
  let clearAvatar = false;           // flag: usuario eligió "Quitar foto"

  // --- Pinta la vista (solo lectura)
  function fillView(auth) {
    $("#viewNombre").textContent   = auth.nombre   || "—";
    $("#viewApellido").textContent = auth.apellido || "—";
    $("#viewNickname").textContent = auth.nickname || "—";
    $("#viewEmail").textContent    = auth.email    || "—";
    $("#viewRol").textContent      = auth.rol      || "—";
  }

  // --- Precarga el formulario
  function fillForm(auth) {
    $("#inpNombre").value   = auth.nombre   || "";
    $("#inpApellido").value = auth.apellido || "";
    $("#inpNickname").value = auth.nickname || "";
    $("#inpEmail").value    = auth.email    || "";

    // Preview actual (puede ser dataURL o una URL remota ya guardada)
    const preview = $("#avatarPreview");
    preview.src = auth.avatar || "";
    preview.alt = (auth.nombre || "Usuario") + " avatar";

    // resetea estados temporales
    $("#avatarFile").value = "";
    pendingAvatarDataURL = null;
    clearAvatar = false;
  }

  function toggleEdit(on) {
    $("#profileView")?.classList.toggle("d-none", on);
    $("#profileForm")?.classList.toggle("d-none", !on);
    $("#btnEditProfile")?.classList.toggle("d-none", on);
  }

  // --- (Opcional) Comprimir/reescalar para no llenar localStorage
  async function fileToDataURLResized(file, maxSize = 256, quality = 0.85) {
    // si el archivo ya es pequeño, devolvemos directo
    const raw = await file.arrayBuffer();
    const blobURL = URL.createObjectURL(new Blob([raw]));
    const img = new Image();
    img.src = blobURL;
    await img.decode();

    const w = img.naturalWidth, h = img.naturalHeight;
    const scale = Math.min(1, maxSize / Math.max(w, h));
    const targetW = Math.max(1, Math.round(w * scale));
    const targetH = Math.max(1, Math.round(h * scale));

    const canvas = document.createElement("canvas");
    canvas.width = targetW;
    canvas.height = targetH;
    const ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, targetW, targetH);

    // Exportar a JPEG (reduce bastante tamaño)
    const dataURL = canvas.toDataURL("image/jpeg", quality);
    URL.revokeObjectURL(blobURL);
    return dataURL;
  }

  // --- Bind inputs de imagen
  function bindAvatarInputs() {
    // Archivo: lee y muestra preview (redimensiona para no explotar localStorage)
    $("#avatarFile")?.addEventListener("change", async () => {
      const f = $("#avatarFile").files?.[0];
      if (!f) return;
      try {
        pendingAvatarDataURL = await fileToDataURLResized(f, 256, 0.85);
      } catch {
        // fallback simple si el navegador no permite decode
        const reader = new FileReader();
        reader.onload = () => { pendingAvatarDataURL = String(reader.result); };
        reader.readAsDataURL(f);
      }
      $("#avatarPreview").src = pendingAvatarDataURL || "";
      clearAvatar = false; // si eligió archivo, ya no está "borrando"
    });

    // Quitar foto
    $("#btnClearPhoto")?.addEventListener("click", () => {
      $("#avatarFile").value = "";
      pendingAvatarDataURL = null;
      clearAvatar = true;
      $("#avatarPreview").src = "";
    });
  }

  // --- Password override (modo demo, solo local)
  function getPwOverrides() {
    try { return JSON.parse(localStorage.getItem("pwOverrides") || "{}"); }
    catch { return {}; }
  }
  function setPwOverrides(map) {
    localStorage.setItem("pwOverrides", JSON.stringify(map || {}));
  }

  function verifyCurrentPassword(nickname, currentPass) {
  const curr = String((currentPass ?? "")).trim();
  const nick = String((nickname ?? "")).trim();
  if (!nick) return false;

  // 1) Preferimos el login real del mock: respeta overrides si patchFindUser los aplicó
  try {
    const fn = window.Volando?.findUser;
    if (typeof fn === "function") {
      const u = fn(nick, curr);
      if (u) return true;
    }
  } catch {}

  // 2) Fallback manual: acepta override O la del mock original
  try {
    const overrides = getPwOverrides();
    if (overrides[nick] != null && String(overrides[nick]) === curr) return true;

    const USERS = window.Volando?.USERS || [];
    const u = USERS.find(x => String(x.nickname) === nick);
    if (u && String(u.pass) === curr) return true;
  } catch {}

  return false;
}

  function patchFindUser() {
    const originalFind = window.Volando?.findUser;
    if (!originalFind || originalFind.__patched) return;
    const USERS = window.Volando?.USERS || [];
    const patched = (nickname, pass) => {
      const nick = String(nickname ?? "").trim();
      const pw   = String(pass ?? "");
      const overrides = getPwOverrides();
      if (overrides[nick] != null) {
        const u = (USERS || []).find(x => String(x.nickname) === nick) || null;
        return u && String(overrides[nick]) === pw ? u : null;
      }
      return originalFind(nickname, pass);
    };
    patched.__patched = true;
    window.Volando.findUser = patched;
  }

  // --- Bind formulario general
  function bindForm() {
    const form   = $("#profileForm");
    const cancel = $("#btnCancelEdit");
    const btnEdit = $("#btnEditProfile");

    btnEdit?.addEventListener("click", (e) => {
      e.preventDefault();
      const auth = JSON.parse(localStorage.getItem("auth") || "null");
      if (!auth) { alert("Tenés que iniciar sesión para editar tu perfil."); return; }
      fillForm(auth);
      toggleEdit(true);
    });

    cancel?.addEventListener("click", () => {
      toggleEdit(false);
      $("#avatarFile").value = "";
      pendingAvatarDataURL = null;
      clearAvatar = false;
    });

    form?.addEventListener("submit", (e) => {
      e.preventDefault();
      try {
        const a = JSON.parse(localStorage.getItem("auth") || "null");
        if (!a) { alert("Tenés que iniciar sesión para editar tu perfil."); return; }

        // Validaciones mínimas
        const nombre   = $("#inpNombre").value.trim();
        const apellido = $("#inpApellido").value.trim();
        if (!nombre) { alert("El nombre es obligatorio."); return; }

        // Resolver avatar: prioridad archivo → borrar → mantener
        let newAvatar = a.avatar || null;
        if (pendingAvatarDataURL) {
          newAvatar = pendingAvatarDataURL;      // usá el archivo elegido
        } else if (clearAvatar) {
          newAvatar = null;                      // el usuario pidió sin foto
        } // si no tocó nada, queda como estaba

        // Cambio de contraseña (demo)
        const curr = ($("#pwdCurrent").value || "").trim();
        const n1   = ($("#pwdNew").value || "").trim();
        const n2   = ($("#pwdNew2").value || "").trim();

        if (n1 || n2 || curr) {
          if (!curr) { alert("Ingresá tu contraseña actual."); return; }
          if (!verifyCurrentPassword(a.nickname, curr)) {
            alert("La contraseña actual no es correcta.");
            return;
          }
          if (n1.length < 3) {
            alert("La nueva contraseña debe tener al menos 3 caracteres (demo).");
            return;
          }
          if (n1 !== n2) {
            alert("Las nueva contraseña no coincide en ambos campos.");
            return;
          }
          if(n1 === curr) {
            alert("La nueva contraseña debe ser diferente a la actual.");
            return;
          }

          // Guarda/actualiza override
          const overrides = getPwOverrides();
          overrides[a.nickname] = String(n1);
          setPwOverrides(overrides);
        }

        const updated = { ...a, nombre, apellido, avatar: newAvatar };
        localStorage.setItem("auth", JSON.stringify(updated));

        // refrescar UI (navbar/perfil)
        try { window.dispatchEvent(new Event("storage")); } catch {}
        try { document.dispatchEvent(new Event("DOMContentLoaded")); } catch {}

        // actualizar vista local
        fillView(updated);
        const bigAvatar = $("#profileAvatar");
        if (bigAvatar) {
          bigAvatar.src = newAvatar || "";
          bigAvatar.alt = `${updated.nombre || "Usuario"} avatar`;
        }

        // salir de edición y limpiar temporales
        toggleEdit(false);
        $("#avatarFile").value = "";
        pendingAvatarDataURL = null;
        clearAvatar = false;

        // toast
        const toast = document.createElement("div");
        toast.className = "position-fixed bottom-0 end-0 p-3";
        toast.innerHTML = `<div class="toast show" role="alert"><div class="toast-body">Perfil actualizado.</div></div>`;
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 2000);
      } catch (err) {
        console.error("[perfil-edit] Error al guardar:", err);
        alert("Ocurrió un error al guardar. Mirá la consola para más detalles.");
      }
    });
  }

  document.addEventListener("DOMContentLoaded", () => {
    if (!window.location.pathname.includes("perfil.html")) return;

    const auth = JSON.parse(localStorage.getItem("auth") || "null");
    if (auth) {
      fillView(auth);
      $("#avatarPreview").src = auth.avatar || "";
    }

    bindAvatarInputs();
    bindForm();
    patchFindUser();
  });
})();
