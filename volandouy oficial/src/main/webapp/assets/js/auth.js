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
    const avatarWrapper = document.getElementById('avatarWrapper');
    const avatarImg = document.getElementById('avatarImg');
    const avatarFallback = document.getElementById('avatarFallback');

    if (guestArea) guestArea.classList.toggle("d-none", isLogged);
    if (userArea)  userArea.classList.toggle("d-none", !isLogged);
    if (nickSpan && isLogged) nickSpan.textContent = auth.nickname || "usuario";

    // Render avatar: si hay URL válida, mostrar <img>, sino fallback con inicial
    if (avatarWrapper) {
      if (isLogged && auth.avatar) {
        if (avatarImg) {
          avatarImg.src = auth.avatar;
          avatarImg.alt = (auth.nickname || 'usuario') + " avatar";
          avatarImg.classList.remove('d-none');
        }
        if (avatarFallback) avatarFallback.classList.add('d-none');
      } else if (isLogged) {
        // generar iniciales
        if (avatarImg) avatarImg.classList.add('d-none');
        if (avatarFallback) {
          avatarFallback.classList.remove('d-none');
          avatarFallback.textContent = (auth.nickname || 'U').slice(0,1).toUpperCase();
        }
      } else {
        // no logueado: ocultar ambos
        if (avatarImg) avatarImg.classList.add('d-none');
        if (avatarFallback) avatarFallback.classList.add('d-none');
      }
    }

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
        email: user.email,
        avatar: user.avatar || null
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

  // === Renderizar perfil ===
  function renderProfile() {
    const auth = Session.get();
    if (!auth) return;

    // Actualizar elementos del perfil
    const nameEl = document.getElementById("profileName");
    const emailEl = document.getElementById("profileEmail");
    const avatarEl = document.getElementById("profileAvatar");

    if (nameEl) nameEl.textContent = auth.nombre || "Nombre no disponible";
    if (emailEl) emailEl.textContent = auth.email || "Email no disponible";
    if (avatarEl && auth.avatar) {
      avatarEl.src = auth.avatar;
      avatarEl.alt = `${auth.nombre || "Usuario"} avatar`;
    }
  }

  document.addEventListener("DOMContentLoaded", () => {
    renderAuthUI();
    bindLogin();
    bindLogout();

    // Renderizar perfil si estamos en la página de perfil
    if (window.location.pathname.includes("perfil.html")) {
      renderProfile();
    }
  });
})();
