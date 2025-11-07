// assets/js/consulta-reservas.js
// Caso de uso: Consulta de Reserva de vuelo (Cliente y Aerolínea) - Mock sin backend
(() => {
  "use strict";

  const NS = window.Volando || window.volando || (window.Volando = {});
  if (!window.volando) window.volando = NS;

  // ====== Helpers de sesión/rol ======
  const auth = (NS.session && NS.session.get && NS.session.get()) || null;
  const role = (auth?.rol || "").toLowerCase(); // "cliente" | "aerolínea"
  const isCliente   = role === "cliente";
  const isAerolinea = role === "aerolínea" || role === "aerolinea"; // por si viene sin tilde

  // Redirección básica si no hay sesión o rol inválido
  document.addEventListener("DOMContentLoaded", () => {
    if (!auth || (!isCliente && !isAerolinea)) {
      // Sin permisos
      const msg = document.createElement("div");
      msg.className = "container py-5";
      msg.innerHTML = `<div class="alert alert-warning">
        Debes iniciar sesión como <strong>Cliente</strong> o <strong>Aerolínea</strong> para ver esta pantalla.
      </div>`;
      document.body.appendChild(msg);
    }
  });

  // ====== Mock DATA (compatible con reservar-vuelo.js) ======
  // Si querés centralizar esto, podés moverlo a un módulo compartido.
  const DATA = [
    {
      aerolinea: "Iberia",
      rutas: [
        {
          id: "MVD-MAD",
          nombre: "Montevideo — Madrid",
          vuelos: [
            { codigo: "IB6012", precio: 890,  duracion: "12 h",   directo: true },
            { codigo: "IB6020", precio: 920,  duracion: "12 h 15",directo: true }
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
          vuelos: [
            { codigo: "CM804",  precio: 420,  duracion: "5 h 30 m",directo: true }
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
          vuelos: [
            { codigo: "ZL1502", precio: 260,  duracion: "2 h 30 m",directo: true }
          ]
        }
      ]
    }
  ];

  // ====== Storage: mismas claves que reservar-vuelo.js ======
  const LS_RS = "volando_reservas";
  const getReservasObj = () => JSON.parse(localStorage.getItem(LS_RS) || "{}");

  // ====== DOM ======
  const $ = (s) => document.querySelector(s);
  const titulo       = $("#tituloConsulta");
  const subtitulo    = $("#subtituloConsulta");
  const frmFiltros   = $("#frmFiltros");
  const selAerolinea = $("#selAerolinea");
  const selRuta      = $("#selRuta");
  const selVuelo     = $("#selVuelo");
  const btnVer       = $("#btnVer");
  const resumenSel   = $("#resumenSeleccion");
  const areaResultados = $("#areaResultados");
  const areaDetalle    = $("#areaDetalle");

  if (!frmFiltros || !selAerolinea || !selRuta || !selVuelo || !areaResultados) {
    console.error("[consulta-reservas] Faltan elementos en el DOM.");
    return;
  }

  function showInfo(msg) {
    const areaResultados = document.getElementById("areaResultados");
    const areaDetalle = document.getElementById("areaDetalle");
    const resumenSel = document.getElementById("resumenSeleccion");
    if (resumenSel) resumenSel.textContent = "";
    if (areaResultados) areaResultados.innerHTML = `<div class="alert alert-info">${msg}</div>`;
    if (areaDetalle) areaDetalle.innerHTML = "";
  }

  // ====== Títulos por modo ======
  function setTitulo() {
    if (isCliente) {
      titulo.innerHTML   = `<h1 class="mb-2">Mis reservas de vuelo</h1>`;
      subtitulo.textContent = "Elegí aerolínea, ruta y vuelo para ver si tienes una reserva asociada y su detalle.";
    } else if (isAerolinea) {
      titulo.innerHTML   = `<h1 class="mb-2">Reservas de nuestros vuelos</h1>`;
      subtitulo.textContent = "Seleccioná una ruta y un vuelo para ver el listado de reservas y los pasajeros.";
    }
  }

  // ====== Poblar selects ======
  function cargarAerolineas() {
    selAerolinea.innerHTML = `<option value="" selected disabled>Seleccionar…</option>`;
    DATA.forEach(a => {
      // Si es Aerolínea, mostrar sólo la suya (si coincide por nombre)
      if (isAerolinea && auth?.aerolinea && a.aerolinea !== auth.aerolinea) return;
      const opt = document.createElement("option");
      opt.value = a.aerolinea;
      opt.textContent = a.aerolinea;
      selAerolinea.appendChild(opt);
    });

    // Si es Aerolínea y conocemos su nombre, fijar y bloquear
    const inCatalog = DATA.some(a => a.aerolinea === auth?.aerolinea);

    if (isAerolinea && auth?.aerolinea) {
      if (!inCatalog) {
        // Insertamos la aerolínea del usuario como opción “virtual”
        const opt = document.createElement("option");
        opt.value = auth.aerolinea;
        opt.textContent = `${auth.aerolinea} (sin catálogo)`;
        selAerolinea.appendChild(opt);
      }
      selAerolinea.value = auth.aerolinea;
      selAerolinea.disabled = true;

      if (inCatalog) {
        cargarRutas(auth.aerolinea);
      } else {
        // Mensaje claro si no hay rutas/vuelos en DATA para esa aerolínea
        showInfo(`Tu aerolínea <strong>${auth.aerolinea}</strong> no tiene rutas ni vuelos cargados aún, por lo tanto no hay reservas asociadas para mostrar.`);
      }
    }
  }

  function cargarRutas(nombreAerolinea) {
    selRuta.innerHTML = `<option value="" selected disabled>Seleccionar…</option>`;
    selRuta.disabled = true;
    selVuelo.innerHTML = `<option value="" selected disabled>Seleccionar…</option>`;
    selVuelo.disabled = true;

    const aero = DATA.find(a => a.aerolinea === nombreAerolinea);
    if (!aero) return;

    aero.rutas.forEach(r => {
      const opt = document.createElement("option");
      opt.value = r.id;
      opt.textContent = r.nombre;
      selRuta.appendChild(opt);
    });
    selRuta.disabled = false;
  }

  function cargarVuelos(nombreAerolinea, rutaId) {
    selVuelo.innerHTML = `<option value="" selected disabled>Seleccionar…</option>`;
    selVuelo.disabled = true;

    const aero = DATA.find(a => a.aerolinea === nombreAerolinea);
    const ruta = aero?.rutas.find(r => r.id === rutaId);
    if (!ruta) return;

    ruta.vuelos.forEach(v => {
      const opt = document.createElement("option");
      opt.value = v.codigo;
      opt.textContent = `${v.codigo} — ${ruta.nombre}`;
      opt.dataset.precio = String(v.precio);
      selVuelo.appendChild(opt);
    });
    selVuelo.disabled = false;
  }

  // ====== Render Cliente: mi reserva (0/1) ======
  function renderMiReserva(codigoVuelo) {
    areaDetalle.innerHTML = "";
    const reservas = getReservasObj();
    const r = reservas[codigoVuelo];

    resumenSel.textContent = `Selección: ${selAerolinea.value} · ${selRuta.options[selRuta.selectedIndex]?.textContent ?? ""} · ${codigoVuelo}`;

    if (!r) {
      areaResultados.innerHTML = `
        <div class="alert alert-info">
          No tienes una reserva registrada para este vuelo.
        </div>`;
      return;
    }

    const pasajerosHTML = (r.pasajeros && r.pasajeros.length)
      ? `<ul class="mb-0">${r.pasajeros.map((p,i)=>`<li>Pasajero ${i+1}: ${p.nombre} ${p.apellido}</li>`).join("")}</ul>`
      : `<em class="text-muted">No hay datos de pasajeros almacenados en esta reserva (mock).</em>`;

    areaResultados.innerHTML = `
      <div class="card shadow-sm">
        <div class="card-body">
          <h5 class="card-title mb-3">Reserva para vuelo <strong>${r.codigo}</strong></h5>
          <div class="row">
            <div class="col-md-6">
              <ul class="list-unstyled">
                <li><strong>Aerolínea:</strong> ${r.aerolinea}</li>
                <li><strong>Ruta:</strong> ${r.ruta}</li>
                <li><strong>Fecha (alta):</strong> ${new Date(r.fecha).toLocaleString()}</li>
                <li><strong>Asiento:</strong> ${r.asiento}</li>
                <li><strong>Pasajes:</strong> ${r.pax}</li>
                <li><strong>Equipaje extra:</strong> ${r.equipaje}</li>
                <li><strong>Forma de pago:</strong> ${r.formaPago}${r.paqueteId ? ` (paquete ${r.paqueteId}, usados ${r.usadosPkg})` : ""}</li>
              </ul>
            </div>
            <div class="col-md-6">
              <ul class="list-unstyled">
                <li><strong>Precio unitario:</strong> $ ${r.precioUnit}</li>
                <li><strong>Total:</strong> $ ${r.total}</li>
              </ul>
              <div class="mt-2">
                <strong>Pasajeros:</strong><br>
                ${pasajerosHTML}
              </div>
            </div>
          </div>
        </div>
      </div>`;
  }

  // ====== Render Aerolínea: tabla + detalle ======

  // Mock simple: genera una lista de reservas a partir del LS del cliente
  // y agrega 1–2 filas falsas para que puedas ver la tabla llena.
  function listarReservasDeVueloMock(codigoVuelo) {
    const base = [];
    const mine = getReservasObj()[codigoVuelo];
    if (mine) {
      base.push({
        ...mine,
        idReserva: `R-${codigoVuelo}-001`,
        cliente: mine.clienteNick || mine.clienteNombre || mine.clienteEmail || "Cliente demo",
        estadoReserva: "CONFIRMADA",
        pasajeros: mine.pasajeros || []
      });
    }
    // Falsas de ejemplo
    base.push({
      idReserva: `R-${codigoVuelo}-002`,
      codigo: codigoVuelo,
      aerolinea: selAerolinea.value,
      ruta: selRuta.options[selRuta.selectedIndex]?.textContent ?? "",
      fecha: new Date().toISOString(),
      asiento: "turista",
      pax: 2,
      equipaje: 1,
      formaPago: "general",
      precioUnit: Number(selVuelo.options[selVuelo.selectedIndex]?.dataset?.precio || 0),
      total: 2 * Number(selVuelo.options[selVuelo.selectedIndex]?.dataset?.precio || 0) + 25,
      cliente: "juan.perez",
      estadoReserva: "RECHAZADA",
      pasajeros: [{nombre:"Juan",apellido:"Pérez"},{nombre:"Ana",apellido:"Pérez"}]
    });
    base.push({
      idReserva: `R-${codigoVuelo}-003`,
      codigo: codigoVuelo,
      aerolinea: selAerolinea.value,
      ruta: selRuta.options[selRuta.selectedIndex]?.textContent ?? "",
      fecha: new Date(Date.now()-86400000).toISOString(),
      asiento: "ejecutivo",
      pax: 1,
      equipaje: 0,
      formaPago: "general",
      precioUnit: Number(selVuelo.options[selVuelo.selectedIndex]?.dataset?.precio || 0) + 180,
      total: Number(selVuelo.options[selVuelo.selectedIndex]?.dataset?.precio || 0) + 180,
      cliente: "m.rodriguez",
      estadoReserva: "CONFIRMADA",
      pasajeros: [{nombre:"María",apellido:"Rodríguez"}]
    });
    return base;
  }

  function badgeEstado(estado) {
    const e = (estado || "").toUpperCase();
    const map = {
      "CONFIRMADA": "success",
      "RECHAZADA": "danger",
      "CANCELADA": "secondary",
      "PENDIENTE": "warning"
    };
    const cls = map[e] || "secondary";
    return `<span class="badge bg-${cls}">${e || "—"}</span>`;
  }

  function renderTablaReservas(lista) {
    if (!lista.length) {
      areaResultados.innerHTML = `<div class="alert alert-info">No hay reservas para este vuelo.</div>`;
      areaDetalle.innerHTML = "";
      return;
    }
    const rows = lista.map(r => `
      <tr data-id="${r.idReserva}">
        <td class="text-nowrap">${r.idReserva}</td>
        <td>${r.cliente || "—"}</td>
        <td class="text-center">${r.pax}</td>
        <td>${badgeEstado(r.estadoReserva)}</td>
        <td class="text-nowrap">${new Date(r.fecha).toLocaleString()}</td>
        <td class="text-end">$ ${r.total}</td>
      </tr>
    `).join("");

    areaResultados.innerHTML = `
      <div class="table-responsive">
        <table class="table table-sm align-middle table-hover clickable-rows">
          <thead>
            <tr>
              <th>Reserva</th>
              <th>Cliente</th>
              <th class="text-center">Pax</th>
              <th>Estado</th>
              <th>Fecha</th>
              <th class="text-end">Total</th>
            </tr>
          </thead>
          <tbody>${rows}</tbody>
        </table>
      </div>
      <div class="small text-muted">Click en una fila para ver el detalle de pasajeros.</div>
    `;

    // click row → detalle
    areaResultados.querySelectorAll("tbody tr").forEach(tr => {
      tr.addEventListener("click", () => {
        const id = tr.getAttribute("data-id");
        const r = lista.find(x => x.idReserva === id);
        renderDetalleReserva(r);
      });
    });

    // pinta el primero por defecto
    renderDetalleReserva(lista[0]);
  }

  function renderDetalleReserva(r) {
    if (!r) { areaDetalle.innerHTML = ""; return; }
    const paxHTML = (r.pasajeros && r.pasajeros.length)
      ? `<ul class="mb-0">${r.pasajeros.map((p,i)=>`<li>Pasajero ${i+1}: ${p.nombre} ${p.apellido}</li>`).join("")}</ul>`
      : `<em class="text-muted">Sin datos de pasajeros (mock).</em>`;

    areaDetalle.innerHTML = `
      <div class="card border-0">
        <div class="card-body">
          <h5 class="card-title mb-3">Detalle de reserva <strong>${r.idReserva}</strong> ${badgeEstado(r.estadoReserva)}</h5>
          <div class="row">
            <div class="col-md-6">
              <ul class="list-unstyled">
                <li><strong>Vuelo:</strong> ${r.codigo}</li>
                <li><strong>Ruta:</strong> ${r.ruta}</li>
                <li><strong>Aerolínea:</strong> ${r.aerolinea}</li>
                <li><strong>Asiento:</strong> ${r.asiento}</li>
                <li><strong>Pasajes:</strong> ${r.pax}</li>
                <li><strong>Equipaje extra:</strong> ${r.equipaje}</li>
              </ul>
            </div>
            <div class="col-md-6">
              <ul class="list-unstyled">
                <li><strong>Cliente:</strong> ${r.cliente || "—"}</li>
                <li><strong>Fecha:</strong> ${new Date(r.fecha).toLocaleString()}</li>
                <li><strong>Forma de pago:</strong> ${r.formaPago || "—"}</li>
                <li><strong>Precio unitario:</strong> $ ${r.precioUnit}</li>
                <li><strong>Total:</strong> $ ${r.total}</li>
              </ul>
              <div class="mt-2">
                <strong>Pasajeros:</strong><br>
                ${paxHTML}
              </div>
            </div>
          </div>
        </div>
      </div>`;
  }

  // ====== Eventos ======
  document.addEventListener("DOMContentLoaded", () => {
    setTitulo();
    cargarAerolineas();

    selAerolinea.addEventListener("change", e => {
      cargarRutas(e.target.value);
      areaResultados.innerHTML = "";
      areaDetalle.innerHTML = "";
      resumenSel.textContent = "";
    });

    selRuta.addEventListener("change", e => {
      cargarVuelos(selAerolinea.value, e.target.value);
      areaResultados.innerHTML = "";
      areaDetalle.innerHTML = "";
      resumenSel.textContent = "";
    });

    frmFiltros.addEventListener("submit", (ev) => {
      ev.preventDefault();
      const aero = selAerolinea.value;
      const rutaTxt = selRuta.options[selRuta.selectedIndex]?.textContent ?? "";
      const vueloCod = selVuelo.value;
      if (!aero || !selRuta.value || !vueloCod) return;

      resumenSel.textContent = `Selección: ${aero} · ${rutaTxt} · ${vueloCod}`;

      if (isCliente) {
        renderMiReserva(vueloCod);
      } else if (isAerolinea) {
        const lista = listarReservasDeVueloMock(vueloCod);
        renderTablaReservas(lista);
      }
    });
  });

})();
