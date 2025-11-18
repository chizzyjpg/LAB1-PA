// assets/js/consulta-usuario.js
// Lista de usuarios (mock - Parte 1). Reemplazá por datos reales en la Parte 2.
(() => {
  "use strict";

  const USERS = [
    { id: 1,  rol: 'Cliente',   nickname: 'maria23',  nombre: 'María',  apellido: 'Suárez',   email: 'maria@correo.com',  imagen: 'https://i.pravatar.cc/80?img=5', pass: 1235 },
    { id: 2,  rol: 'Cliente',   nickname: 'marko',    nombre: 'Marco',  apellido: 'Pérez',    email: 'marco@correo.com',  imagen: 'https://i.pravatar.cc/80?img=12', pass: "asd" },
    { id: 3,  rol: 'Aerolínea', nickname: 'air_uy',   nombre: 'AirUY',  apellido: '',         email: 'contacto@airuy.com', imagen: '', pass: '1235', aerolinea: 'AirUY' },
    { id: 4,  rol: 'Aerolínea', nickname: 'condor',   nombre: 'Cóndor', apellido: 'Air',      email: 'hola@condor.air',     imagen: '', pass: '1235', aerolinea: 'Cóndor Air' },
    { id: 5,  rol: 'Cliente',   nickname: 'lucas88',  nombre: 'Lucas',  apellido: 'Rodríguez',email: 'lucas@correo.com',   imagen: 'https://i.pravatar.cc/80?img=15', pass: 1235 },
    { id: 6,  rol: 'Cliente',   nickname: 'ani.g',    nombre: 'Ana',    apellido: 'García',   email: 'ana@correo.com',     imagen: 'https://i.pravatar.cc/80?img=32', pass: 1235 },
    { id: 7,  rol: 'Aerolínea', nickname: 'pampas',   nombre: 'Pampas', apellido: 'Air',      email: 'info@pampas.air',     imagen: '', pass: '1235', aerolinea: 'Pampas Air' },
    { id: 8,  rol: 'Cliente',   nickname: 'sofi_uy',  nombre: 'Sofía',  apellido: 'Martínez', email: 'sofia@correo.com',   imagen: 'https://i.pravatar.cc/80?img=20', pass: 1235 },
    { id: 9,  rol: 'Cliente',   nickname: 'diego_r',  nombre: 'Diego',  apellido: 'Ramos',    email: 'diego@correo.com',   imagen: 'https://i.pravatar.cc/80?img=4', pass: 1235 },
    { id: 10, rol: 'Aerolínea', nickname: 'aurora',   nombre: 'Aurora', apellido: 'Air',      email: 'contacto@aurora.air', imagen: '', pass: '1235', aerolinea: 'Aurora' },
    { id: 11, rol: 'Aerolínea', nickname: 'iberia',   nombre: 'Iberia', apellido: '',         email: 'contacto@iberia.air', imagen: '', pass: '1235', aerolinea: 'Iberia' },
    { id: 12, rol: 'Aerolínea', nickname: 'copa',     nombre: 'Copa',  apellido: 'Airlines',  email: 'contacto@copa.air', imagen: '', pass: '1235', aerolinea: 'Copa Airlines' },
    { id: 13, rol: 'Aerolínea', nickname: 'zuly',     nombre: 'Zuly',  apellido: 'Fly',        email: 'contacto@zuly.air', imagen: '', pass: '1235', aerolinea: 'ZulyFly' }
  ];

  // === Exponer buscador (Front-only / Parte 1) ===
  window.Volando = window.Volando || {};

  // Para depurar: mostrar lista de nicks en consola si querés
  window.Volando.debugListUsers = () => console.table((USERS || []).map(u => ({
    id: u.id, rol: u.rol, nickname: u.nickname, email: u.email
  })));

  // Login por nickname + pass
  window.Volando.findUser = (nickname, pass) => {
    try {
      const nick = String(nickname ?? "").trim();
      const p    = String(pass ?? "");
      if (!nick || !p) return null;

      return (USERS || []).find(u =>
        String(u.nickname) === nick && String(u.pass) === p
      ) || null;
    } catch { return null; }
  };

// (Opcional, solo lectura)
Object.defineProperty(window.Volando, 'USERS', { get: () => USERS });


  // Toma referencias del DOM
  const listEl   = document.getElementById('userList');
  const countEl  = document.getElementById('userCount');
  const formEl   = document.getElementById('userFilters');
  const qInput   = document.getElementById('filterText');
  const rolSel   = document.getElementById('filterRol');
  const modalEl  = document.getElementById('userModal');

  // Si falta algo, salimos silenciosamente
  if (!listEl || !countEl || !formEl || !qInput || !rolSel || !modalEl) {
    console.warn('[consulta-usuario] Faltan elementos requeridos en el HTML.');
    return;
  }

  const modalTitle = modalEl.querySelector('.modal-title');
  const modalBody  = modalEl.querySelector('.modal-body');
  const bsModal    = new bootstrap.Modal(modalEl); // requiere bootstrap.bundle.min.js

  // Avatar con fallback a iniciales
  function avatarFor(u, size = 64) {
    if (u.imagen) return u.imagen;
    const name = (u.nombre ? u.nombre : '') + ' ' + (u.apellido ? u.apellido : '');
    const label = (name.trim() || u.nickname || 'User').slice(0, 32);
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(label)}&background=0D6EFD&color=fff&size=${size}`;
  }

  // Render de la lista
  function render(list) {
    listEl.innerHTML = '';
    list.forEach(u => {
      const a = document.createElement('a');
      a.href = '#';
      a.className = 'list-group-item list-group-item-action d-flex gap-3 align-items-center';
      a.addEventListener('click', (e) => { e.preventDefault(); openModal(u); });

      a.innerHTML = `
        <img src="${avatarFor(u)}" alt="" width="48" height="48"
             class="rounded-circle flex-shrink-0 object-fit-cover">
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between flex-wrap">
            <strong>${(u.nombre || '')} ${(u.apellido || '')}</strong>
            <span class="badge ${u.rol === 'Aerolínea' ? 'text-bg-warning' : 'text-bg-primary'}">${u.rol}</span>
          </div>
          <div class="text-muted small">@${u.nickname} · ${u.email}</div>
        </div>
      `;
      listEl.appendChild(a);
    });

    countEl.textContent = `${list.length} usuario${list.length !== 1 ? 's' : ''} encontrado${list.length !== 1 ? 's' : ''}`;
  }

  // Aplica filtros de texto y rol
  function applyFilters() {
    const q = (qInput.value || '').trim().toLowerCase();
    const r = rolSel.value; // '' = todos
    const filtered = USERS.filter(u => {
      const okTexto = !q || [u.nombre, u.apellido, u.nickname, u.email]
        .filter(Boolean)
        .some(v => v.toLowerCase().includes(q));
      const okRol = !r || u.rol === r;
      return okTexto && okRol;
    });
    render(filtered);
  }

  // Utilidades de datos cruzados
  function getAuth() {
    try { return JSON.parse(localStorage.getItem('auth') || 'null'); } catch { return null; }
  }
  function esPropio(u) {
    const a = getAuth();
    return !!(a && a.nickname && a.nickname === u.nickname);
  }
  function reservasDelCliente(nick) {
    const rs = JSON.parse(localStorage.getItem('volando_reservas') || '{}');
    // El formato de reservas es por código de vuelo; no tiene cliente.
    // Para mock: si es propio, mostramos todas; si no, ninguna.
    return Object.values(rs);
  }
  function comprasDelCliente(nick) {
    try { return JSON.parse(localStorage.getItem(`compras_${nick}`) || '[]'); } catch { return []; }
  }
  function catalogoRutasPorAero() {
    const paqs = window.PaquetesRutasVuelo?.getAllPaquetes?.() || [];
    const map = {};
    for (const p of paqs) for (const r of p.rutas_vuelo) {
      const aero = r.aerolinea;
      (map[aero] ||= []).push(r);
    }
    return map;
  }
  function groupRutasPorEstado(rutas) {
    const g = { Confirmada: [], Ingresada: [], Rechazada: [] };
    for (const r of rutas) {
      (g[r.estado] ||= []);
      if (g[r.estado]) g[r.estado].push(r);
    }
    return g;
  }

  // Abre modal con detalle enriquecido según caso de uso
  function openModal(u) {
    modalTitle.textContent = `${(u.nombre || '')} ${(u.apellido || '')}`.trim() || u.nickname;

    const propio = esPropio(u);
    const isAir = u.rol === 'Aerolínea';
    const isCli = u.rol === 'Cliente';

    // Secciones dinámicas
    let extraHTML = '';

    if (isAir) {
      // Rutas confirmadas; si propio, también ingresadas y rechazadas
      const porAero = catalogoRutasPorAero();
      const nombreAero = u.nombre && u.apellido ? `${u.nombre} ${u.apellido}`.trim() : u.nombre || u.nickname;
      const rutasAero = porAero[nombreAero] || porAero[u.nombre] || porAero[u.nickname] || [];
      const grouped = groupRutasPorEstado(rutasAero);

      const bloqueRutas = [];
      const pintarLista = (title, arr) => arr && arr.length ? `
        <div class="mb-3">
          <h6 class="mb-2">${title} <span class="badge bg-secondary">${arr.length}</span></h6>
          <div class="list-group">
            ${arr.map(r => `
              <a href="#" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" data-kind="ruta" data-id="${r.id}">
                <span><i class="fas fa-route me-2"></i>${r.nombre}</span>
                <span class="badge ${r.estado === 'Confirmada' ? 'bg-success' : r.estado === 'Ingresada' ? 'bg-warning text-dark' : 'bg-danger'}">${r.estado}</span>
              </a>
            `).join('')}
          </div>
        </div>` : '';

      bloqueRutas.push(pintarLista('Rutas Confirmadas', grouped.Confirmada));
      if (propio) {
        bloqueRutas.push(pintarLista('Rutas Ingresadas', grouped.Ingresada));
        bloqueRutas.push(pintarLista('Rutas Rechazadas', grouped.Rechazada));
      }

      if (bloqueRutas.join('').trim()) {
        extraHTML += `<hr><h5 class="mb-3">Rutas de Vuelo</h5>${bloqueRutas.join('')}`;
      }
    }

    if (isCli && propio) {
      // Reservas del cliente y paquetes comprados
      const reservas = reservasDelCliente(u.nickname);
      const compras  = comprasDelCliente(u.nickname);

      if (reservas && reservas.length) {
        extraHTML += `
          <hr><h5 class="mb-3">Mis Reservas</h5>
          <div class="list-group mb-3">
            ${reservas.map(rv => `
              <a href="#" class="list-group-item list-group-item-action" data-kind="reserva" data-cod="${rv.codigo}">
                <div class="d-flex justify-content-between">
                  <span><i class="fas fa-ticket-alt me-2"></i>${rv.codigo} — ${rv.ruta}</span>
                  <span class="text-muted small">${new Date(rv.fecha).toLocaleString('es-UY')}</span>
                </div>
                <div class="small text-muted">Pax: ${rv.pax} · Pago: ${rv.formaPago === 'paquete' ? 'Paquete' : 'General'}${rv.usadosPkg ? ` · Usados Paquete: ${rv.usadosPkg}` : ''}</div>
              </a>
            `).join('')}
          </div>`;
      }

      if (compras && compras.length) {
        extraHTML += `
          <h5 class="mb-3">Mis Paquetes Comprados</h5>
          <div class="list-group">
            ${compras.map(cp => `
              <a href="#" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" data-kind="paquete" data-nombre="${cp.nombre_paquete}">
                <span><i class="fas fa-box-open me-2"></i>${cp.nombre_paquete}</span>
                <span class="badge ${cp.estado === 'Activa' ? 'bg-success' : 'bg-secondary'}">${cp.estado}</span>
              </a>
            `).join('')}
          </div>`;
      }
    }

    modalBody.innerHTML = `
      <div class="d-flex align-items-center gap-3 mb-3">
        <img src="${avatarFor(u, 96)}" alt="" width="72" height="72"
             class="rounded-circle object-fit-cover">
        <div>
          <div><strong>Rol:</strong> ${u.rol}</div>
          <div><strong>Nickname:</strong> @${u.nickname}</div>
          <div><strong>Email:</strong> <a href="mailto:${u.email}">${u.email}</a></div>
        </div>
      </div>
      ${extraHTML || '<p class="text-muted small mb-0">* No hay información adicional para mostrar.</p>'}
      <p class="text-muted small mb-0 mt-3">* Datos simulados.</p>
    `;

    // Delegación de clicks para abrir detalles
    modalBody.querySelectorAll('a.list-group-item').forEach(el => {
      el.addEventListener('click', (e) => {
        e.preventDefault();
        const kind = el.getAttribute('data-kind');
        if (kind === 'ruta') {
          const id = Number(el.getAttribute('data-id'));
          if (window.mostrarDetallesRuta) window.mostrarDetallesRuta(id);
        } else if (kind === 'reserva') {
          const cod = el.getAttribute('data-cod');
          mostrarDetalleReserva(cod);
        } else if (kind === 'paquete') {
          const nombre = el.getAttribute('data-nombre');
          abrirPaquetePorNombre(nombre);
        }
      });
    });

    bsModal.show();
  }

  function abrirPaquetePorNombre(nombre) {
    const paqs = window.PaquetesRutasVuelo?.getAllPaquetes?.() || [];
    const p = paqs.find(x => x.nombre.toLowerCase() === String(nombre||'').toLowerCase());
    if (p && window.mostrarDetallesPaquete) window.mostrarDetallesPaquete(p.id);
  }

  function mostrarDetalleReserva(codigo) {
    const rs = JSON.parse(localStorage.getItem('volando_reservas') || '{}');
    const r = rs[codigo];
    if (!r) { alert('Reserva no encontrada'); return; }

    // Modal sencillo para reserva
    const id = 'reservaModal';
    const prev = document.getElementById(id);
    if (prev) prev.remove();
    const html = `
      <div class="modal fade" id="${id}" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Reserva ${r.codigo}</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <div><strong>Ruta:</strong> ${r.ruta}</div>
              <div><strong>Aerolínea:</strong> ${r.aerolinea}</div>
              <div><strong>Asiento:</strong> ${r.asiento}</div>
              <div><strong>Pasajeros:</strong> ${r.pax}</div>
              <div><strong>Equipaje:</strong> ${r.equipaje}</div>
              <div><strong>Forma de pago:</strong> ${r.formaPago === 'paquete' ? 'Paquete' : 'General'}</div>
              ${r.usadosPkg ? `<div><strong>Usados de paquete:</strong> ${r.usadosPkg}</div>` : ''}
              <div class="mt-2"><strong>Total:</strong> $${r.total}</div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
            </div>
          </div>
        </div>
      </div>`;
    document.body.insertAdjacentHTML('beforeend', html);
    const m = new bootstrap.Modal(document.getElementById(id));
    m.show();
    document.getElementById(id).addEventListener('hidden.bs.modal', function(){ this.remove(); });
  }

  // Inicialización
  document.addEventListener('DOMContentLoaded', () => {
    // Arranca mostrando TODOS los usuarios
    rolSel.value = '';
    qInput.value = '';
    render(USERS);

    // Listeners para filtrar
    qInput.addEventListener('input', applyFilters);
    rolSel.addEventListener('change', applyFilters);
  });


})();
