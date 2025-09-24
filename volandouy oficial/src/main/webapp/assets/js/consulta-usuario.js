// assets/js/consulta-usuario.js
// Lista de usuarios (mock - Parte 1). Reemplazá por datos reales en la Parte 2.
(() => {
  "use strict";

  const USERS = [
    { id: 1,  rol: 'Cliente',   nickname: 'maria23',  nombre: 'María',  apellido: 'Suárez',   email: 'maria@correo.com',  imagen: 'https://i.pravatar.cc/80?img=5' },
    { id: 2,  rol: 'Cliente',   nickname: 'marko',    nombre: 'Marco',  apellido: 'Pérez',    email: 'marco@correo.com',  imagen: 'https://i.pravatar.cc/80?img=12' },
    { id: 3,  rol: 'Aerolínea', nickname: 'air_uy',   nombre: 'AirUY',  apellido: '',         email: 'contacto@airuy.com', imagen: '' },
    { id: 4,  rol: 'Aerolínea', nickname: 'condor',   nombre: 'Cóndor', apellido: 'Air',      email: 'hola@condor.air',     imagen: '' },
    { id: 5,  rol: 'Cliente',   nickname: 'lucas88',  nombre: 'Lucas',  apellido: 'Rodríguez',email: 'lucas@correo.com',   imagen: 'https://i.pravatar.cc/80?img=20' },
    { id: 6,  rol: 'Cliente',   nickname: 'ani.g',    nombre: 'Ana',    apellido: 'García',   email: 'ana@correo.com',     imagen: 'https://i.pravatar.cc/80?img=32' },
    { id: 7,  rol: 'Aerolínea', nickname: 'pampas',   nombre: 'Pampas', apellido: 'Air',      email: 'info@pampas.air',     imagen: '' },
    { id: 8,  rol: 'Cliente',   nickname: 'sofi_uy',  nombre: 'Sofía',  apellido: 'Martínez', email: 'sofia@correo.com',   imagen: 'https://i.pravatar.cc/80?img=15' },
    { id: 9,  rol: 'Cliente',   nickname: 'diego_r',  nombre: 'Diego',  apellido: 'Ramos',    email: 'diego@correo.com',   imagen: 'https://i.pravatar.cc/80?img=41' },
    { id: 10, rol: 'Aerolínea', nickname: 'aurora',   nombre: 'Aurora', apellido: 'Air',      email: 'contacto@aurora.air', imagen: '' },
  ];

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

  // Abre modal con detalle
  function openModal(u) {
    modalTitle.textContent = `${(u.nombre || '')} ${(u.apellido || '')}`.trim() || u.nickname;
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
      <p class="text-muted small mb-0">* Datos simulados (Parte 1).</p>
    `;
    bsModal.show();
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
