(() => {
  "use strict";

  // ===== Namespace global =====
  const NS = window.Volando || window.volando || (window.Volando = {});
  if (!window.volando) window.volando = NS;

  // ===== Normalizador de strings (quita tildes, a minús, trim) =====
  //   "Aerolínea" -> "aerolinea"
  NS.norm = NS.norm || function (s) {
    return String(s || "")
      .normalize("NFD").replace(/[\u0300-\u036f]/g, "")
      .toLowerCase()
      .trim();
  };

  // ===== Persistencia de sesión =====
  const KEY = "auth";

  NS.session = NS.session || {
    get() {
      try { return JSON.parse(localStorage.getItem(KEY) || "null"); }
      catch { return null; }
    },
    set(auth) {
      localStorage.setItem(KEY, JSON.stringify(auth));
      // Notificar a toda la app que cambió la sesión
      const detail = { auth: NS.session.get() };
      window.dispatchEvent(new CustomEvent("auth:changed", { detail }));
    },
    clear() {
      localStorage.removeItem(KEY);
      const detail = { auth: null };
      window.dispatchEvent(new CustomEvent("auth:changed", { detail }));
    }
  };

  // ===== Rol actual (normalizado) =====
  // Devuelve "aerolinea" | "cliente" | "guest" | ...
  NS.currentRole = NS.currentRole || function () {
    const auth = NS.session.get();
    return auth && auth.rol ? NS.norm(auth.rol) : "guest";
  };

  NS.hasRole = NS.hasRole || function (roles) {
    const role = NS.currentRole();
    const list = Array.isArray(roles) ? roles : String(roles).split(",");
    return list.map(NS.norm).includes(role);
  };

  NS.requireRole = NS.requireRole || function (roles, { redirect, onDeny } = {}) {
    if (NS.hasRole(roles)) return true;
    if (typeof onDeny === "function") onDeny(NS.currentRole());
    if (redirect) window.location.href = redirect;
    return false;
  };

  // ===== Render según estado de sesión  =====
  function renderAuthUI() {
    const auth = NS.session.get();
    const isLogged = !!auth;

    const guestArea = document.getElementById("guestArea");
    const userArea  = document.getElementById("userArea");
    const nickSpan  = document.getElementById("nicknameSpan");
    const avatarWrapper  = document.getElementById('avatarWrapper');
    const avatarImg      = document.getElementById('avatarImg');
    const avatarFallback = document.getElementById('avatarFallback');

    if (guestArea) guestArea.classList.toggle("d-none", isLogged);
    if (userArea)  userArea.classList.toggle("d-none", !isLogged);
    if (nickSpan && isLogged) nickSpan.textContent = auth.nickname || "usuario";

    // Avatar
    if (avatarWrapper) {
      if (isLogged && auth.avatar) {
        if (avatarImg) {
          avatarImg.src = auth.avatar;
          avatarImg.alt = (auth.nickname || 'usuario') + " avatar";
          avatarImg.classList.remove('d-none');
        }
        if (avatarFallback) avatarFallback.classList.add('d-none');
      } else if (isLogged) {
        if (avatarImg) avatarImg.classList.add('d-none');
        if (avatarFallback) {
          avatarFallback.classList.remove('d-none');
          avatarFallback.textContent = (auth.nickname || 'U').slice(0,1).toUpperCase();
        }
      } else {
        if (avatarImg) avatarImg.classList.add('d-none');
        if (avatarFallback) avatarFallback.classList.add('d-none');
      }
    }

    // IMPORTANTE:
    // Ya NO ocultamos por roles acá. Eso lo hace roles.js centralizado.
  }

  // ===== Login programático (para usar desde páginas de login) =====
  NS.loginUser = (user) => {
    if (!user) return false;
    NS.session.set({
      id: user.id,
      rol: user.rol,                 // "Cliente" o "Aerolínea", como venga
      nickname: user.nickname,
      nombre: user.nombre,
      apellido: user.apellido,
      email: user.email,
      avatar: user.imagen || null    // tu campo 'imagen'
    });
    renderAuthUI(); // refresca áreas de usuario/invitado
    return true;
  };

  // ===== Login desde modal =====
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

      NS.session.set({
        id: user.id,
        rol: user.rol,               // "Cliente" | "Aerolínea"
        nickname: user.nickname,
        nombre: user.nombre,
        apellido: user.apellido,
        email: user.email,
        avatar: user.imagen || null
      });

      // Cerrar modal y refrescar
      try {
        const modalEl = document.getElementById("loginModal");
        const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
        modal.hide();
        form.reset();
      } catch {}
      renderAuthUI();
    });
  }

  // ===== Logout =====
  function bindLogout() {
    const btnLogout = document.getElementById("btnLogout");
    if (!btnLogout) return;
    btnLogout.addEventListener("click", () => {
      NS.session.clear();
      renderAuthUI();
    });
  }

  // ===== Renderizar perfil =====
  function renderProfile() {
    const auth = NS.session.get();
    if (!auth) return;

    const nameEl = document.getElementById("profileName");
    const emailEl = document.getElementById("profileEmail");
    const avatarEl = document.getElementById("profileAvatar");

    if (nameEl) {
      const fullName = [auth.nombre, auth.apellido].filter(Boolean).join(" ");
      nameEl.textContent = fullName || "Nombre no disponible";
    }
    if (emailEl) emailEl.textContent = auth.email || "Email no disponible";
    if (avatarEl && auth.avatar) {
      avatarEl.src = auth.avatar;
      avatarEl.alt = `${auth.nombre || "Usuario"} avatar`;
    }
  }

  // ===== Arranque =====
  document.addEventListener("DOMContentLoaded", () => {
    renderAuthUI();
    bindLogin();
    bindLogout();

    if (window.location.pathname.includes("perfil.html")) {
      renderProfile();
    }
  });

})();
