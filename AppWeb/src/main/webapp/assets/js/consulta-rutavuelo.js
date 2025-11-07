// assets/js/consulta-rutavuelo.js
(() => {
  "use strict";

  // --- Datos mock (reemplazá con fetch al backend más adelante) ---
  const AIRLINES = [
    { id: "airuy",  nombre: "AirUY" },
    { id: "condor", nombre: "Cóndor Air" },
    { id: "aurora", nombre: "Aurora Air" },
  ];

  const ROUTES = [
    {
      id: 101,
      origen: "MVD",
      destino: "EZE",
      nombre: "Montevideo → Buenos Aires",
      aerolineaId: "airuy",
      categoria: "Regional",
      estado: "Confirmada",
      imagen: "https://picsum.photos/seed/route1/960/420",
      descripcion: "Ruta regional Río de la Plata.",
      vuelos: [
        { id: 1001, codigo: "UY123", fecha: "2025-03-10", salida: "14:30", llegada: "15:35", estado: "Programado", origen: "MVD", destino: "EZE" },
        { id: 1002, codigo: "UY124", fecha: "2025-03-12", salida: "09:00", llegada: "10:05", estado: "Programado", origen: "MVD", destino: "EZE" },
      ],
    },
    {
      id: 102,
      origen: "MVD",
      destino: "GRU",
      nombre: "Montevideo → São Paulo",
      aerolineaId: "condor",
      categoria: "Internacional",
      estado: "Confirmada",
      imagen: "https://picsum.photos/seed/route2/960/420",
      descripcion: "Conexión con hub de Brasil.",
      vuelos: [
        { id: 2001, codigo: "CD401", fecha: "2025-03-08", salida: "07:10", llegada: "10:05", estado: "Programado", origen: "MVD", destino: "GRU" },
      ],
    },
    {
      id: 103,
      origen: "EZE",
      destino: "MVD",
      nombre: "Buenos Aires → Montevideo",
      aerolineaId: "aurora",
      categoria: "Regional",
      estado: "Confirmada",
      imagen: "https://picsum.photos/seed/route3/960/420",
      descripcion: "Regreso a Uruguay.",
      vuelos: [
        { id: 3001, codigo: "AR221", fecha: "2025-03-09", salida: "18:45", llegada: "19:40", estado: "Programado", origen: "EZE", destino: "MVD" },
        { id: 3002, codigo: "AR222", fecha: "2025-03-15", salida: "12:10", llegada: "13:05", estado: "Programado", origen: "EZE", destino: "MVD" },
      ],
    },
    
  ];

  // --- Helpers ---
  const $ = (sel) => document.querySelector(sel);
  const $$ = (sel) => document.querySelectorAll(sel);
  const byId = (id) => document.getElementById(id);

  function airlineName(id) {
    const a = AIRLINES.find(x => x.id === id);
    return a ? a.nombre : id;
    }

  function distinct(arr) { return [...new Set(arr)]; }

  function routeMatchesText(rt, q) {
    if (!q) return true;
    q = q.toLowerCase();
    return (
      (rt.nombre && rt.nombre.toLowerCase().includes(q)) ||
      (rt.origen && rt.origen.toLowerCase().includes(q)) ||
      (rt.destino && rt.destino.toLowerCase().includes(q)) ||
      (rt.descripcion && rt.descripcion.toLowerCase().includes(q)) ||
      (airlineName(rt.aerolineaId).toLowerCase().includes(q))
    );
  }

  // --- DOM refs ---
  const listEl   = byId('routeList');
  const countEl  = byId('routeCount');
  const qInput   = byId('filterTextRutas');
  const aerSel   = byId('filterAerolinea');
  const catSel   = byId('filterCategoria');

  const routeModalEl  = byId('routeModal');
  const routeTitleEl  = routeModalEl.querySelector('.modal-title');
  const routeBodyEl   = routeModalEl.querySelector('.modal-body');
  const routeModal    = new bootstrap.Modal(routeModalEl);

  const flightModalEl = byId('flightModal');
  const flightTitleEl = flightModalEl.querySelector('.modal-title');
  const flightBodyEl  = flightModalEl.querySelector('.modal-body');
  const flightModal   = new bootstrap.Modal(flightModalEl);

  if (!listEl || !routeModalEl) {
    console.warn('[consulta-rutavuelo] Faltan nodos en el HTML');
    return;
  }

  // --- Render de lista ---
  function renderList(routes) {
    listEl.innerHTML = '';
    routes.forEach(rt => {
      const a = document.createElement('a');
      a.href = '#';
      a.className = 'list-group-item list-group-item-action';
      a.innerHTML = `
        <div class="d-flex w-100 justify-content-between align-items-center">
          <h6 class="mb-1">${rt.nombre}</h6>
          <span class="badge text-bg-success">${rt.estado}</span>
        </div>
        <div class="small text-muted mb-1">
          Aerolínea: <strong>${airlineName(rt.aerolineaId)}</strong>
          · Categoría: <strong>${rt.categoria}</strong>
          · Vuelos: <strong>${rt.vuelos.length}</strong>
        </div>
        <div class="small text-muted">${rt.origen} → ${rt.destino}</div>
      `;
      a.addEventListener('click', (e) => { e.preventDefault(); openRoute(rt); });
      listEl.appendChild(a);
    });
    countEl.textContent = `${routes.length} ruta${routes.length !== 1 ? 's' : ''} encontrada${routes.length !== 1 ? 's' : ''}`;
  }

  // --- Filtros ---
  function applyFilters() {
    const q   = (qInput.value || '').trim().toLowerCase();
    const aer = aerSel.value;   // '' = todas
    const cat = catSel.value;   // '' = todas

    const confirmed = ROUTES.filter(r => r.estado === 'Confirmada');
    const filtered = confirmed.filter(rt =>
      routeMatchesText(rt, q) &&
      (!aer || rt.aerolineaId === aer) &&
      (!cat || rt.categoria === cat)
    );
    renderList(filtered);
  }

  // --- Modal de Ruta ---
  function openRoute(rt) {
    routeTitleEl.textContent = rt.nombre;
    const img = rt.imagen
      ? `<img src="${rt.imagen}" alt="" class="img-fluid rounded mb-3">`
      : '';

    const vuelosRows = rt.vuelos.map(v => `
      <tr class="hover-row" data-flight-id="${v.id}">
        <td><strong>${v.codigo}</strong></td>
        <td>${v.fecha}</td>
        <td>${v.origen} → ${v.destino}</td>
        <td>${v.salida} — ${v.llegada}</td>
        <td><span class="badge ${v.estado === 'Programado' ? 'text-bg-primary' : 'text-bg-secondary'}">${v.estado}</span></td>
      </tr>
    `).join('');

    routeBodyEl.innerHTML = `
      ${img}
      <div class="mb-2 small text-muted">
        Aerolínea: <strong>${airlineName(rt.aerolineaId)}</strong> ·
        Categoría: <strong>${rt.categoria}</strong> ·
        Estado: <strong>${rt.estado}</strong>
      </div>
      <p class="mb-3">${rt.descripcion || ''}</p>

      <h6 class="mt-3">Vuelos asociados</h6>
      <div class="table-responsive">
        <table class="table table-sm align-middle">
          <thead>
            <tr>
              <th>Código</th><th>Fecha</th><th>Tramo</th><th>Horario</th><th>Estado</th>
            </tr>
          </thead>
          <tbody id="flightsBody">${vuelosRows || '<tr><td colspan="5" class="text-muted">Sin vuelos</td></tr>'}</tbody>
        </table>
      </div>
      <p class="text-muted small mb-0">* Datos simulados.</p>
    `;

    // Click en fila → abrir modal de vuelo
    const rows = routeBodyEl.querySelectorAll('#flightsBody tr.hover-row');
    rows.forEach(row => {
      row.style.cursor = 'pointer';
      row.addEventListener('click', () => {
        const id = Number(row.getAttribute('data-flight-id'));
        const vuelo = rt.vuelos.find(v => v.id === id);
        if (vuelo) openFlight(rt, vuelo);
      });
    });

    routeModal.show();
  }

  // --- Modal de Vuelo ---
  function openFlight(rt, v) {
    flightTitleEl.textContent = `${v.codigo} — ${rt.origen} → ${rt.destino}`;
    flightBodyEl.innerHTML = `
      <div class="mb-2 small text-muted">
        Aerolínea: <strong>${airlineName(rt.aerolineaId)}</strong> ·
        Ruta: <strong>${rt.nombre}</strong>
      </div>
      <ul class="list-unstyled mb-0">
        <li><strong>Fecha:</strong> ${v.fecha}</li>
        <li><strong>Tramo:</strong> ${v.origen} → ${v.destino}</li>
        <li><strong>Horario:</strong> ${v.salida} — ${v.llegada}</li>
        <li><strong>Estado:</strong> ${v.estado}</li>
      </ul>
    `;
    flightModal.show();
  }

  // --- Init ---
  document.addEventListener('DOMContentLoaded', () => {
    // Cargar opciones de Aerolínea y Categoría
    // Aerolíneas
    AIRLINES.forEach(a => {
      const opt = document.createElement('option');
      opt.value = a.id; opt.textContent = a.nombre;
      aerSel.appendChild(opt);
    });
    // Categorías (distintas presentes en ROUTES Confirmadas)
    distinct(ROUTES.filter(r => r.estado === 'Confirmada').map(r => r.categoria))
      .forEach(c => {
        const opt = document.createElement('option');
        opt.value = c; opt.textContent = c;
        catSel.appendChild(opt);
      });

    // Mostrar todas las confirmadas al inicio
    qInput.value = '';
    aerSel.value = '';
    catSel.value = '';
    applyFilters();

    // Listeners
    qInput.addEventListener('input', applyFilters);
    aerSel.addEventListener('change', applyFilters);
    catSel.addEventListener('change', applyFilters);
  });
})();
