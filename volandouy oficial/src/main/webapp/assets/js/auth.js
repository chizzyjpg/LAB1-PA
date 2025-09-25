(() => {
  "use strict";

  // === Persistencia local (se mantiene al cerrar el navegador) ===
  const Session = {
    get() {
      try { return JSON.parse(localStorage.getItem("auth") || "null"); }
      catch { return null; }
    },
    set(auth) { localStorage.setItem("auth", JSON.stringify(auth)); },
    clear()    { localStorage.removeItem("auth"); }
  };

  // === Render según estado de sesión ===
  function renderAuthUI() {
    const auth = Session.get();
    const isLogged = !!auth;

    const guestArea = document.getElementById("guestArea");
    const userArea  = document.getElementById("userArea");
    const nickSpan  = document.getElementById("nicknameSpan");

    if (guestArea) guestArea.classList.toggle("d-none", isLogged);
    if (userArea)  userArea.classList.toggle("d-none", !isLogged);
    if (nickSpan && isLogged) nickSpan.textContent = auth.nickname || "usuario";

    // Si querés ocultar bloques según rol en páginas futuras:
    // Usa data-roles="Cliente,Aerolínea" en el bloque
    document.querySelectorAll("[data-roles]").forEach(el => {
      const allowed = el.getAttribute("data-roles").split(",").map(s => s.trim());
      const can = isLogged && allowed.includes(auth.rol);
      el.style.display = can ? "" : "none";
    });
  }

  // === Login desde modal ===
  function bindLogin() {
    const form = document.getElementById("loginForm");
    const error = document.getElementById("loginError");
    if (!form) return;

    form.addEventListener("submit", (e) => {
      e.preventDefault();
      const fd   = new FormData(form);
      const nick = (fd.get("nickname") || "").toString().trim();
      const pass = (fd.get("pass") || "").toString();

      const user = window.Volando?.findUser?.(nick, pass);
      if (!user) {
        if (error) error.classList.remove("d-none");
        return;
      }
      if (error) error.classList.add("d-none");

      // Guardar sesión
      Session.set({
        id: user.id,
        rol: user.rol,           // 'Cliente' o 'Aerolínea' (tal como están en tu mock)
        nickname: user.nickname,
        nombre: user.nombre,
        email: user.email
      });

      // Cerrar modal y refrescar UI
      try {
        const modalEl = document.getElementById("loginModal");
        const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modal.hide();
        form.reset();
      } catch {}
      renderAuthUI();
    });
  }

  // === Logout ===
  function bindLogout() {
    const btnLogout = document.getElementById("btnLogout");
    if (!btnLogout) return;
    btnLogout.addEventListener("click", () => {
      Session.clear();
      renderAuthUI();
    });
  }

  document.addEventListener("DOMContentLoaded", () => {
    renderAuthUI();
    bindLogin();
    bindLogout();
  });
})();
