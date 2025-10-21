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

  // --- 4) Cargar aerolíneas desde backend ---
  function cargarAerolineas() {
    fetch('consultaVuelo?accion=listarAerolineas')
      .then(r => r.json())
      .then(aerolineas => {
        selAer.innerHTML = '<option value="" selected>Elegir…</option>';
        aerolineas.forEach(a => {
          const opt = document.createElement('option');
          opt.value = a.nickname;
          opt.textContent = a.nombre;
          selAer.appendChild(opt);
        });
        selAer.disabled = false;
      });
  }

  // --- 5) Cargar rutas confirmadas según aerolínea ---
  function cargarRutasConfirmadas(nicknameAerolinea) {
    selRuta.innerHTML = '<option value="" selected>Cargando…</option>';
    selRuta.disabled = true;
    fetch(`consultaVuelo?accion=listarRutasConfirmadas&nicknameAerolinea=${encodeURIComponent(nicknameAerolinea)}`)
      .then(r => r.json())
      .then(rutas => {
        selRuta.innerHTML = '<option value="" selected>Elegir…</option>';
        rutas.forEach(r => {
          const opt = document.createElement('option');
          opt.value = r.nombre;
          opt.textContent = r.nombre;
          selRuta.appendChild(opt);
        });
        selRuta.disabled = false;
      });
  }

  // --- 6) Cargar vuelos de la ruta seleccionada ---
  function cargarVuelos(nicknameAerolinea, nombreRuta) {
    listEl.innerHTML = '<div class="list-group-item">Cargando vuelos…</div>';
    // Usar el nombre visible de la ruta, no el id
    const nombreRutaVisible = selRuta.options[selRuta.selectedIndex].text;
    fetch(`consultaVuelo?accion=listarVuelos&nicknameAerolinea=${encodeURIComponent(nicknameAerolinea)}&nombreRuta=${encodeURIComponent(nombreRutaVisible)}`)
      .then(r => r.json())
      .then(vuelos => {
        listEl.innerHTML = '';
        if (!vuelos.length) {
          listEl.innerHTML = '<div class="list-group-item">No hay vuelos para esta ruta.</div>';
          return;
        }
        vuelos.forEach(v => {
          v.nicknameAerolinea = selAer.value;
          v.nombreRuta = nombreRutaVisible;
          v.codigoVuelo = v.codigoVuelo || v.nombre || v.codigo;
          const item = document.createElement('button');
          item.className = 'list-group-item list-group-item-action';
          item.textContent = `${v.codigoVuelo} - ${v.origen} → ${v.destino} (${v.fechaSalida})`;
          item.onclick = () => mostrarDetalleVuelo(v);
          listEl.appendChild(item);
        });
      });
  }

  // --- 7) Mostrar detalle de vuelo y reservas ---
  function mostrarDetalleVuelo(vuelo) {
    vuelo.nicknameAerolinea = selAer.value;
    fetch(`consultaVuelo?accion=detalleVuelo&nicknameAerolinea=${encodeURIComponent(vuelo.nicknameAerolinea)}&nombreRuta=${encodeURIComponent(vuelo.nombreRuta)}&codigoVuelo=${encodeURIComponent(vuelo.codigoVuelo)}`)
      .then(r => r.json())
      .then(data => {
        const modalBody = document.querySelector('#flightModal .modal-body');
        const origen = data.DRuta && data.DRuta.ciudadOrigen ? data.DRuta.ciudadOrigen.nombre : "No disponible";
        const destino = data.DRuta && data.DRuta.ciudadDestino ? data.DRuta.ciudadDestino.nombre : "No disponible";
        const estado = data.DRuta && data.DRuta.estado ? data.DRuta.estado : "No disponible";
        modalBody.innerHTML = `
          <h5>${data.nombre || data.codigoVuelo}</h5>
          <p><strong>Origen:</strong> ${origen} <br>
          <strong>Destino:</strong> ${destino} <br>
          <strong>Fecha salida:</strong> ${data.fecha || data.fechaSalida} <br>
          <strong>Estado:</strong> ${estado}</p>
          <div id="reservasVuelo"></div>
        `;
        cargarReservasVuelo(data);
        const modal = new bootstrap.Modal(document.getElementById('flightModal'));
        modal.show();
      });
  }

  function cargarReservasVuelo(vuelo) {
    vuelo.nicknameAerolinea = selAer.value;
    // Usar el nombre visible de la ruta
    vuelo.nombreRuta = selRuta.options[selRuta.selectedIndex].text;
    vuelo.codigoVuelo = vuelo.codigoVuelo || vuelo.nombre || vuelo.codigo;
    fetch(`consultaVuelo?accion=listarReservas&nicknameAerolinea=${encodeURIComponent(vuelo.nicknameAerolinea)}&nombreRuta=${encodeURIComponent(vuelo.nombreRuta)}&codigoVuelo=${encodeURIComponent(vuelo.codigoVuelo)}`)
      .then(r => r.json())
      .then(reservas => {
        const reservasDiv = document.getElementById('reservasVuelo');
        if (!reservas.length) {
          reservasDiv.innerHTML = '<p>No hay reservas para este vuelo.</p>';
          return;
        }
        reservasDiv.innerHTML = '<h6>Reservas:</h6>';
        reservas.forEach(res => {
          const btn = document.createElement('button');
          btn.className = 'btn btn-link';
          btn.textContent = `Reserva #${res.idReserva} - ${res.nickCliente?.nombre || ''}`;
          btn.onclick = () => mostrarDetalleReserva(vuelo, res.idReserva);
          reservasDiv.appendChild(btn);
        });
      });
  }

  function mostrarDetalleReserva(vuelo, idReserva) {
    fetch(`consultaVuelo?accion=detalleReserva&nicknameAerolinea=${encodeURIComponent(vuelo.nicknameAerolinea)}&nombreRuta=${encodeURIComponent(vuelo.nombreRuta)}&codigoVuelo=${encodeURIComponent(vuelo.codigoVuelo)}&idReserva=${idReserva}`)
      .then(r => r.json())
      .then(res => {
        const reservasDiv = document.getElementById('reservasVuelo');
        reservasDiv.innerHTML = `
          <h6>Detalle de Reserva</h6>
          <p><strong>ID:</strong> ${res.idReserva}<br>
          <strong>Cliente:</strong> ${res.nickCliente?.nombre || ''} (${res.nickCliente?.nickname || ''})<br>
          <strong>Estado:</strong> ${res.estado}<br>
          <strong>Fecha:</strong> ${res.fechaReserva}</p>
          <button class="btn btn-secondary" onclick="window.location.reload()">Volver</button>
        `;
      });
  }

  // --- 8) Eventos ---
  selAer.addEventListener('change', e => {
    const nicknameAerolinea = selAer.value;
    if (!nicknameAerolinea) {
      selRuta.innerHTML = '<option value="" selected>Elegir aerolínea primero…</option>';
      selRuta.disabled = true;
      listEl.innerHTML = '';
      return;
    }
    cargarRutasConfirmadas(nicknameAerolinea);
    listEl.innerHTML = '';
  });

  selRuta.addEventListener('change', e => {
    const nicknameAerolinea = selAer.value;
    const nombreRuta = selRuta.value;
    if (!nicknameAerolinea || !nombreRuta) {
      listEl.innerHTML = '';
      return;
    }
    cargarVuelos(nicknameAerolinea, nombreRuta);
  });

  // --- 9) Inicialización ---
  cargarAerolineas();
})();