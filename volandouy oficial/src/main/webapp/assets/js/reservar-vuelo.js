// assets/js/reservar-vuelo.js
// Implementa el caso de uso "Reserva de Vuelo" (mock, sin backend)
console.log("[reservar-vuelo] script cargado");

document.addEventListener("DOMContentLoaded", () => {
  console.log("[reservar-vuelo] DOM listo");

  /* ================== Datos MOCK ================== */
  const DATA = [
    {
      aerolinea: "Iberia",
      rutas: [
        {
          id: "MVD-MAD",
          nombre: "Montevideo — Madrid",
          img: "../assets/img/madrid.jpg",
          vuelos: [
            { codigo: "IB6012", duracion: "12 h", directo: true, precio: 890, extras: "Comidas, bebidas y entretenimiento" },
            { codigo: "IB6020", duracion: "12 h 15 m", directo: true, precio: 920, extras: "Comidas y entretenimiento" }
          ]
        }
      ]
    },
    {
      aerolinea: "Copa Airlines",
      rutas: [
        {
          id: "PTY-NYC",
          nombre: "Ciudad de Panamá — Nueva York",
          img: "../assets/img/nyc.jpg",
          vuelos: [
            { codigo: "CM804", duracion: "5 h 30 m", directo: true, precio: 420, extras: "Servicio a bordo completo" }
          ]
        }
      ]
    },
    {
      aerolinea: "ZulyFly",
      rutas: [
        {
          id: "MVD-RIO",
          nombre: "Montevideo — Río de Janeiro",
          img: "../assets/img/rio.jpg",
          vuelos: [
            { codigo: "ZL1502", duracion: "2 h 30 m", directo: true, precio: 260, extras: "Servicio de cortesía" }
          ]
        }
      ]
    }
  ];

  // Paquetes “del cliente” (persisten en localStorage)
  const LS_PK = "volando_paquetes";
  function getPaquetes() {
    try {
      const raw = localStorage.getItem(LS_PK);
      if (raw) return JSON.parse(raw);
    } catch {}
    const inicial = [
      { id: "PKG1", nombre: "Paquete Europa 2025", rutaId: "MVD-MAD", disponibles: 3 },
      { id: "PKG2", nombre: "Escapadas Brasil", rutaId: "MVD-RIO", disponibles: 2 }
    ];
    localStorage.setItem(LS_PK, JSON.stringify(inicial));
    return inicial;
  }
  function savePaquetes(list) { localStorage.setItem(LS_PK, JSON.stringify(list)); }

  // Reservas persistidas (simple)
  const LS_RS = "volando_reservas";
  function getReservas() { return JSON.parse(localStorage.getItem(LS_RS) || "{}"); }
  function saveReservas(obj) { localStorage.setItem(LS_RS, JSON.stringify(obj)); }

  /* ================== DOM & helpers ================== */
  const $  = (s) => document.querySelector(s);

  const selAerolinea = $("#selAerolinea");
  const selRuta      = $("#selRuta");
  const grid         = $("#gridVuelos");
  const resumen      = $("#resumenFiltros");
  const formFiltros  = $("#filtrosForm");

  if (!selAerolinea || !selRuta || !grid || !resumen || !formFiltros) {
    console.error("[reservar-vuelo] Faltan elementos base en el DOM.");
    return;
  }

  // Elementos del modal
  const modalEl      = document.getElementById("vueloModal");
  if (!modalEl) { console.error("[reservar-vuelo] Falta #vueloModal"); return; }
  const modal        = new bootstrap.Modal(modalEl);
  const modalTitle   = document.getElementById("vueloModalTitle");
  const detalleVuelo = document.getElementById("detalleVuelo");
  const reservaForm  = document.getElementById("reservaForm");
  const formAsiento  = document.getElementById("formAsiento");
  const formPax      = document.getElementById("formPax");
  const formEquipaje = document.getElementById("formEquipaje");
  const pasajerosBox = document.getElementById("pasajerosFields");
  const pagoGeneral  = document.getElementById("pagoGeneral");
  const pagoPaqueteR = document.getElementById("pagoPaquete");
  const bloquePaquete= document.getElementById("bloquePaquete");
  const selPaquete   = document.getElementById("selPaquete");
  const pasajesPkg   = document.getElementById("pasajesPaquete");
  const costoResumen = document.getElementById("costoResumen");
  const btnConfirmar = document.getElementById("btnConfirmarReserva");

  // Estado del vuelo seleccionado
  let vueloActual = null; // { aerolinea, ruta, vuelo, img, precio, ... }

  /* ================== Poblar selects de filtros ================== */
  function cargarAerolineas() {
    DATA.forEach(a => {
      const opt = document.createElement("option");
      opt.value = a.aerolinea;
      opt.textContent = a.aerolinea;
      selAerolinea.appendChild(opt);
    });
  }
  function cargarRutas(aerolinea) {
    selRuta.innerHTML = '<option value="" selected disabled>Seleccionar…</option>';
    selRuta.disabled = true;
    const item = DATA.find(a => a.aerolinea === aerolinea);
    if (!item) return;
    item.rutas.forEach(r => {
      const opt = document.createElement("option");
      opt.value = r.id;
      opt.textContent = r.nombre;
      opt.dataset.img = r.img;
      selRuta.appendChild(opt);
    });
    selRuta.disabled = false;
  }

  /* ================== Render de cards ================== */
  function renderVuelos(aerolinea, rutaId, asiento, pax) {
    grid.innerHTML = "";
    const item = DATA.find(a => a.aerolinea === aerolinea);
    if (!item) return;
    const ruta = item.rutas.find(r => r.id === rutaId);
    if (!ruta) return;

    resumen.textContent = `Mostrando ${ruta.vuelos.length} vuelo(s) — ${aerolinea} · ${ruta.nombre} · Asiento: ${asiento} · Pasajeros: ${pax}`;

    ruta.vuelos.forEach(v => {
      const col = document.createElement("div");
      col.className = "col-12 col-md-10 col-lg-9";
      col.innerHTML = `
        <div class="card h-100 shadow-sm">
          <div class="row g-0 align-items-center">
            <div class="col-12 col-sm-4">
              <img src="${ruta.img}" alt="${ruta.nombre}" class="img-fluid card-img-top rounded-start" style="height:160px;object-fit:cover">
            </div>
            <div class="col-12 col-sm-8">
              <div class="card-body">
                <h3 class="h6 mb-2">${ruta.nombre} <small class="text-muted">(${v.codigo})</small></h3>
                <p class="mb-2">
                  ✈️ <strong>Aerolínea:</strong> ${aerolinea}<br>
                  ⏱ <strong>Duración:</strong> ${v.duracion} ${v.directo ? "(directo)" : ""}<br>
                  💺 <strong>Asiento:</strong> ${asiento}<br>
                  👥 <strong>Pasajeros:</strong> ${pax}<br>
                  🍽️ ${v.extras}
                </p>
                <div class="d-flex align-items-center justify-content-between">
                  <span class="fw-bold">$ ${v.precio}</span>
                  <button class="btn btn-sm btn-primary"
                          data-codigo="${v.codigo}"
                          data-aerolinea="${aerolinea}"
                          data-rutaid="${ruta.id}"
                          data-ruta="${ruta.nombre}"
                          data-img="${ruta.img}"
                          data-duracion="${v.duracion}"
                          data-precio="${v.precio}"
                          data-asiento="${asiento}"
                          data-pax="${pax}">
                    Ver detalles
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>`;
      grid.appendChild(col);
    });
  }

  /* ================== Helpers de Pasajeros & Paquetes ================== */
  function renderPasajeros(n) {
    // Genera filas Nombre/Apellido obligatorias
    pasajerosBox.innerHTML = "";
    const cant = Math.max(1, Number(n || 1)); // SIEMPRE al menos 1
    for (let i = 1; i <= cant; i++) {
      const row = document.createElement("div");
      row.className = "col-12 col-md-6";
      row.innerHTML = `
        <div class="input-group">
          <span class="input-group-text">Pasajero ${i}</span>
          <input class="form-control" placeholder="Nombre"  required>
          <input class="form-control" placeholder="Apellido" required>
        </div>`;
      pasajerosBox.appendChild(row);
    }
  }

  function renderPaquetesPara(rutaId) {
    const pkgs = getPaquetes().filter(p => p.rutaId === rutaId && p.disponibles > 0);
    selPaquete.innerHTML = "";
    if (pkgs.length === 0) {
      selPaquete.innerHTML = `<option value="" disabled selected>No hay paquetes disponibles para esta ruta</option>`;
      pasajesPkg.value = 1;
      pasajesPkg.disabled = true;
      return;
    }
    pkgs.forEach(p => {
      const opt = document.createElement("option");
      opt.value = p.id;
      opt.textContent = `${p.nombre} — Disp: ${p.disponibles}`;
      opt.dataset.disp = String(p.disponibles);
      selPaquete.appendChild(opt);
    });
    pasajesPkg.disabled = false;
  }

  function actualizarResumenCosto() {
    if (!vueloActual) return;
    const pax = Math.max(1, Number(formPax.value || 1));
    const equipaje = Math.max(0, Number(formEquipaje.value || 0));
    const precio = Number(vueloActual.precio);
    const costoEquipaje = equipaje * 25; // tarifa mock por unidad

    let usados = 0;
    let textoPaquete = "—";
    if (pagoPaqueteR.checked) {
      const opt = selPaquete.options[selPaquete.selectedIndex];
      const disp = opt ? Number(opt.dataset.disp || 0) : 0;
      usados = Math.max(0, Math.min(pax, Number(pasajesPkg.value || 0), disp));
      textoPaquete = opt ? `${opt.textContent} · usar ${usados}` : "sin paquetes";
    }

    const basePagable = Math.max(0, pax - usados);
    const costoBase = basePagable * precio;
    const total = costoBase + costoEquipaje;
    const hoy = new Date().toLocaleDateString();

    costoResumen.innerHTML = `
      <div><strong>Fecha:</strong> ${hoy}</div>
      <div><strong>Base:</strong> $ ${precio} × ${basePagable} ${pagoPaqueteR.checked ? `(usando ${usados} de paquete)` : ""}</div>
      <div><strong>Equipaje extra:</strong> ${equipaje} × $25 = $ ${costoEquipaje}</div>
      <hr class="my-2">
      <div class="fs-6"><strong>Total a pagar:</strong> $ ${total}</div>
      <div class="text-muted">Pago: ${pagoGeneral.checked ? "general" : `paquete (${textoPaquete})`}</div>
    `;
  }

  /* ================== Abrir modal desde una card ================== */
  grid.addEventListener("click", (e) => {
    const btn = e.target.closest("button.btn-primary");
    if (!btn) return;

    vueloActual = {
      codigo: btn.dataset.codigo,
      aerolinea: btn.dataset.aerolinea,
      rutaId: btn.dataset.rutaid,
      ruta: btn.dataset.ruta,
      img: btn.dataset.img,
      duracion: btn.dataset.duracion,
      precio: Number(btn.dataset.precio),
      asiento: btn.dataset.asiento,
      pax: Number(btn.dataset.pax)
    };

    modalTitle.textContent = `Vuelo ${vueloActual.codigo} — ${vueloActual.aerolinea}`;
    detalleVuelo.innerHTML = `
      <div class="row g-3">
        <div class="col-12 col-md-4">
          <img src="${vueloActual.img}" alt="${vueloActual.ruta}" class="img-fluid rounded">
        </div>
        <div class="col-12 col-md-8">
          <ul class="list-unstyled mb-0">
            <li><strong>Ruta:</strong> ${vueloActual.ruta}</li>
            <li><strong>Duración:</strong> ${vueloActual.duracion}</li>
            <li><strong>Asiento (preseleccionado):</strong> ${vueloActual.asiento}</li>
            <li><strong>Pasajeros (preseleccionado):</strong> ${vueloActual.pax}</li>
            <li><strong>Precio por pasajero:</strong> $ ${vueloActual.precio}</li>
          </ul>
        </div>
      </div>
    `;

    // precargar valores en el form
    formAsiento.value = vueloActual.asiento || "turista";
    formPax.value = vueloActual.pax || 1;
    formEquipaje.value = 0;

    // pago por defecto: general
    pagoGeneral.checked = true;
    pagoPaqueteR.checked = false;
    bloquePaquete.classList.add("d-none");

    // GENERAR CAMPOS DE PASAJEROS **SIEMPRE** (al menos 1)
    renderPasajeros(formPax.value);

    // cargar paquetes aplicables a la ruta
    renderPaquetesPara(vueloActual.rutaId);

    // primer cálculo de costo
    actualizarResumenCosto();

    // aviso si ya existe reserva para este vuelo
    const reservas = getReservas();
    if (reservas[vueloActual.codigo]) {
      const alerta = document.createElement("div");
      alerta.className = "alert alert-warning mt-3";
      alerta.innerHTML = `Ya existe una reserva previa para este vuelo. Si confirmás, se <strong>reemplaza</strong>.`;
      detalleVuelo.appendChild(alerta);
    }

    modal.show();
  });

  /* ================== Eventos dentro del modal ================== */
  formPax.addEventListener("input", () => {
    // Re-generar nombre/apellido al cambiar cantidad
    renderPasajeros(formPax.value);
    actualizarResumenCosto();
  });

  formEquipaje.addEventListener("input", actualizarResumenCosto);
  formAsiento.addEventListener("change", actualizarResumenCosto);

  pagoGeneral.addEventListener("change", () => {
    bloquePaquete.classList.add("d-none");
    actualizarResumenCosto();
  });
  pagoPaqueteR.addEventListener("change", () => {
    bloquePaquete.classList.remove("d-none");
    actualizarResumenCosto();
  });
  selPaquete.addEventListener("change", actualizarResumenCosto);
  pasajesPkg.addEventListener("input", actualizarResumenCosto);

  /* ================== Confirmar reserva ================== */
  btnConfirmar.addEventListener("click", () => {
    if (!vueloActual) return;

    // Validar nombres y apellidos
    const inputs = pasajerosBox.querySelectorAll("input");
    for (const inp of inputs) {
      if (!inp.value.trim()) {
        alert("Completá Nombre y Apellido de todos los pasajeros.");
        return;
      }
    }

    const pax = Math.max(1, Number(formPax.value || 1));
    const equipaje = Math.max(0, Number(formEquipaje.value || 0));
    let paquetes = getPaquetes();
    let reservas = getReservas();

    let formaPago = pagoGeneral.checked ? "general" : "paquete";
    let usadosPkg = 0;
    let paqueteId = null;

    if (formaPago === "paquete") {
      const opt = selPaquete.options[selPaquete.selectedIndex];
      if (!opt) { alert("No hay paquete seleccionado."); return; }
      paqueteId = opt.value;
      const disp = Number(opt.dataset.disp || 0);
      usadosPkg = Math.max(0, Math.min(pax, Number(pasajesPkg.value || 0), disp));
      if (usadosPkg <= 0) { alert("Indicá cuántos pasajes usás del paquete."); return; }
      // descontar del paquete
      paquetes = paquetes.map(p => p.id === paqueteId ? { ...p, disponibles: p.disponibles - usadosPkg } : p);
      savePaquetes(paquetes);
    }

    const precio = Number(vueloActual.precio);
    const basePagable = Math.max(0, pax - usadosPkg);
    const total = basePagable * precio + equipaje * 25;

    const reserva = {
      codigo:    vueloActual.codigo,
      aerolinea: vueloActual.aerolinea,
      rutaId:    vueloActual.rutaId,
      ruta:      vueloActual.ruta,
      fecha:     new Date().toISOString(),
      asiento:   formAsiento.value,
      pax,
      equipaje,
      formaPago,
      paqueteId,
      usadosPkg,
      precioUnit: precio,
      total
    };

    reservas[vueloActual.codigo] = reserva; // reemplaza si ya existía
    saveReservas(reservas);

    modal.hide();
    alert(`✅ Reserva registrada.\nVuelo ${reserva.codigo} — Total $ ${reserva.total}`);
  });

  /* ================== Filtros iniciales ================== */
  cargarAerolineas();
  selAerolinea.addEventListener("change", (e) => cargarRutas(e.target.value));
  formFiltros.addEventListener("submit", (e) => {
    e.preventDefault();
    const aerolinea = selAerolinea.value;
    const rutaId    = selRuta.value;
    const asiento   = document.getElementById("selAsiento").value;
    const pax       = document.getElementById("numPasajeros").value || 1;
    if (!aerolinea || !rutaId) return;
    renderVuelos(aerolinea, rutaId, asiento, pax);
  });

  console.log("[reservar-vuelo] inicializado ✅");
});
