// assets/js/consulta-vuelo.js
(() => {
  "use strict";

  // --- 1) Datos (mock). Reusá los de rutas si ya los tenés ---
  // Podés copiar AIRLINES y ROUTES del caso anterior.
  // Por ahora dejo un ejemplo mínimo:

  const AIRLINES = [
    { id: "airuy",  nombre: "AirUY" },
    { id: "condor", nombre: "Cóndor Air" },
  ];

  // Rutas con estado + vuelos. (Reusá tu ROUTES real si ya existe)
  const ROUTES = [
    {
      id: 101,
      aerolineaId: "airuy",
      nombre: "Montevideo → Buenos Aires",
      origen: "MVD", destino: "EZE",
      estado: "Confirmada",
      vuelos: [
        { id: 1001, codigo: "UY123", fecha: "2025-03-10", salida: "14:30", llegada: "15:35", origen: "MVD", destino: "EZE", estado: "Programado", imagen: "https://picsum.photos/seed/uy123/640/360" },
        { id: 1002, codigo: "UY124", fecha: "2025-03-12", salida: "09:00", llegada: "10:05", origen: "MVD", destino: "EZE", estado: "Programado" }
      ]
    },
    {
      id: 102,
      aerolineaId: "condor",
      nombre: "Montevideo → São Paulo",
      origen: "MVD", destino: "GRU",
      estado: "Confirmada",
      vuelos: [
        { id: 2001, codigo: "CD401", fecha: "2025-03-08", salida: "07:10", llegada: "10:05", origen: "MVD", destino: "GRU", estado: "Programado" }
      ]
    },
    // Ejemplo no confirmada (no debe aparecer en el select de rutas)
    { id: 103, aerolineaId: "airuy", nombre: "MVD → MAD", origen: "MVD", destino: "MAD", estado: "Borrador", vuelos: [] }
  ];

  // --- 2) Helpers DOM ---
  const $ = (s) => document.querySelector(s);
  const byId = (id) => document.getElementById(id);
  const airlineName = (id) => (AIRLINES.find(a => a.id === id)?.nombre || id);

  // --- 3) Referencias ---
  const selAer = byId('fltAerolinea');
  const selRuta = byId('fltRuta');
  const inpTxt  = byId('fltTexto');
  const listEl  = byId('flightList');
  const countEl = byId('flightCount');

  const flightModalEl = byId('flightModal');
  const flightTitleEl = flightModalEl.querySelector('.modal-title');
  const flightBodyEl  = flightModalEl.querySelector('.modal-body');
  const flightModal   = new bootstrap.Modal(flightModalEl);

  // --- 4) Poblar aerolíneas ---
  function loadAirlines() {
    AIRLINES.forEach(a => {
      const opt = document.createElement('option');
      opt.value = a.id; opt.textContent = a.nombre;
      selAer.appendChild(opt);
    });
  }

  // --- 5) Cuando cambia aerolínea → cargar rutas confirmadas ---
  function loadRoutesForAirline() {
    const aId = selAer.value;
    selRuta.innerHTML = '<option value="" selected>Elegir…</option>';
    selRuta.disabled = !aId;

    if (!aId) {
      renderFlights([]); // limpiar lista
      return;
    }

    const rutas = ROUTES.filter(r => r.aerolineaId === aId && r.estado === 'Confirmada');
    rutas.forEach(r => {
      const opt = document.createElement('option');
      opt.value = String(r.id);
      opt.textContent = `${r.nombre} (${r.origen} → ${r.destino})`;
      selRuta.appendChild(opt);
    });

    // Si querés, seleccioná la primera automáticamente:
    if (rutas.length) {
      selRuta.value = String(rutas[0].id);
      loadFlightsForRoute();
    } else {
      renderFlights([]);
    }
  }

  // --- 6) Cuando cambia ruta → mostrar vuelos ---
  function loadFlightsForRoute() {
    const routeId = Number(selRuta.value);
    const route = ROUTES.find(r => r.id === routeId);
    if (!route) { renderFlights([]); return; }

    // aplicamos búsqueda de texto
    const q = (inpTxt.value || '').trim().toLowerCase();
    const flights = (route.vuelos || []).filter(v => {
      if (!q) return true;
      return [v.codigo, v.origen, v.destino, v.fecha]
        .filter(Boolean).some(s => String(s).toLowerCase().includes(q));
    });

    renderFlights(flights, route);
  }

  // --- 7) Renderizar lista de vuelos ---
  function renderFlights(flights, route = null) {
    listEl.innerHTML = '';
    flights.forEach(v => {
      const a = document.createElement('a');
      a.href = '#';
      a.className = 'list-group-item list-group-item-action d-flex justify-content-between align-items-center';
      a.innerHTML = `
        <div>
          <strong>${v.codigo}</strong>
          <span class="text-muted small ms-2">${v.origen} → ${v.destino}</span>
          <div class="text-muted small">${v.fecha} · ${v.salida} — ${v.llegada}</div>
        </div>
        <span class="badge ${v.estado === 'Programado' ? 'text-bg-primary' : 'text-bg-secondary'}">${v.estado}</span>
      `;
      a.addEventListener('click', (e) => {
        e.preventDefault();
        openFlightModal(v, route);
      });
      listEl.appendChild(a);
    });
    countEl.textContent = `${flights.length} vuelo${flights.length !== 1 ? 's' : ''} encontrado${flights.length !== 1 ? 's' : ''}`;
  }

  // --- 8) Modal detalle de vuelo ---
  function openFlightModal(v, route) {
    const title = `${v.codigo} — ${v.origen} → ${v.destino}`;
    flightTitleEl.textContent = title;

    const img = v.imagen ? `<img src="${v.imagen}" alt="" class="img-fluid rounded mb-3">` : '';

    flightBodyEl.innerHTML = `
      ${img}
      <div class="mb-2 small text-muted">
        Aerolínea: <strong>${airlineName(route?.aerolineaId)}</strong> ·
        Ruta: <strong>${route?.nombre || ''}</strong>
      </div>
      <ul class="list-unstyled mb-0">
        <li><strong>Fecha:</strong> ${v.fecha}</li>
        <li><strong>Horario:</strong> ${v.salida} — ${v.llegada}</li>
        <li><strong>Estado:</strong> ${v.estado}</li>
      </ul>
    `;
    flightModal.show();
  }

  // --- 9) Init ---
  document.addEventListener('DOMContentLoaded', () => {
    loadAirlines();         // llena select de aerolínea
    // listeners
    selAer.addEventListener('change', loadRoutesForAirline);
    selRuta.addEventListener('change', loadFlightsForRoute);
    inpTxt.addEventListener('input', loadFlightsForRoute);
  });
})();
