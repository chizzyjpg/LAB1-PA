(function () {
  const NS = window.Volando || window.volando || (window.Volando = {});
  if (!window.volando) window.volando = NS;

  // Asegura que exista NS.norm (por si este archivo carga antes que auth.js)
  NS.norm = NS.norm || function (s) {
    return String(s || "")
      .normalize("NFD").replace(/[\u0300-\u036f]/g, "")
      .toLowerCase().trim();
  };

  // Muestra/oculta elementos según rol usando data-roles / data-roles-not
  //   data-roles="aerolinea"                -> solo aerolinea
  //   data-roles="cliente,aerolinea"        -> cualquiera de esos
  //   data-roles="guest"                    -> solo no logueados
  //   data-roles="any"                      -> visible para todos
  //   data-roles-not="cliente"              -> ocultar si es cliente
  //
  NS.applyRoleVisibility = NS.applyRoleVisibility || function (root = document) {
    const role = NS.norm(NS.currentRole());

    root.querySelectorAll("[data-roles], [data-roles-not]").forEach(el => {
      const allowAttr = el.getAttribute("data-roles");
      const denyAttr  = el.getAttribute("data-roles-not");

      let allowed = true;

      if (allowAttr) {
        const allowList = allowAttr.split(",").map(NS.norm);
        allowed = allowList.includes("any") || allowList.includes(role);
      }

      if (denyAttr) {
        const denyList = denyAttr.split(",").map(NS.norm);
        if (denyList.includes(role)) allowed = false;
      }

      el.classList.toggle("d-none", !allowed);
    });
  };

  // Aplica al cargar y cuando cambia la sesión (login/logout)
  document.addEventListener("DOMContentLoaded", () => NS.applyRoleVisibility());
  window.addEventListener("auth:changed", () => NS.applyRoleVisibility());

  // Re-aplica en nodos inyectados dinámicamente
  const mo = new MutationObserver(muts => {
    for (const m of muts) {
      if (m.addedNodes && m.addedNodes.length) {
        m.addedNodes.forEach(n => {
          if (n.nodeType === 1) NS.applyRoleVisibility(n);
        });
      }
    }
  });
  document.addEventListener("DOMContentLoaded", () => {
    mo.observe(document.documentElement, { childList: true, subtree: true });
  });

  // Atajo para otros scripts: suscribirse a cambios de sesión
  NS.onAuthChanged = NS.onAuthChanged || function (cb) {
    if (typeof cb !== "function") return;
    window.addEventListener("auth:changed", e => cb(e.detail?.auth ?? null));
  };

})();
