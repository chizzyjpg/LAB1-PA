/**
 * Consulta de Paquetes de Rutas de Vuelo
 * Implementa el caso de uso completo según especificación
 */

(() => {
  "use strict";

  // === DATOS MOCK ===
  const PAQUETES_RUTAS_VUELO = [
    {
      id: 1,
      nombre: "Escapada Sudamericana",
      descripcion: "Descubre los destinos más fascinantes de Sudamérica con este paquete completo",
      periodo_validez: "2025-01-01 a 2025-12-31",
      descuento: 15,
      fecha_alta: "2024-12-01",
      imagen: "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 2850.00,
      rutas_vuelo: [
        {
          id: 1,
          nombre: "Montevideo - Buenos Aires",
          descripcion: "Ruta clásica rioplatense",
          origen: "MVD",
          destino: "EZE", 
          categoria: "Regional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1589909202802-8f4aadce1849?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
          hora_salida: "08:00",
          hora_llegada: "09:30",
          costo_turista: 280.00,
          costo_ejecutivo: 420.00,
          costo_primera: 650.00,
          aerolinea: "AirUY",
          cantidad_en_paquete: 2,
          vuelos: [
            { id: 1001, codigo: "UY123", fecha: "2025-03-10", salida: "08:00", llegada: "09:30", estado: "Programado", origen: "MVD", destino: "EZE" },
            { id: 1002, codigo: "UY124", fecha: "2025-03-12", salida: "14:15", llegada: "15:45", estado: "Programado", origen: "MVD", destino: "EZE" },
            { id: 1003, codigo: "UY125", fecha: "2025-03-15", salida: "10:20", llegada: "11:50", estado: "Confirmado", origen: "MVD", destino: "EZE" }
          ]
        },
        {
          id: 2,
          nombre: "Buenos Aires - São Paulo",
          descripcion: "Conectando capitales económicas",
          origen: "EZE",
          destino: "GRU",
          categoria: "Internacional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "14:00",
          hora_llegada: "17:15",
          costo_turista: 450.00,
          costo_ejecutivo: 680.00,
          costo_primera: 980.00,
          aerolinea: "Cóndor Air",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 2001, codigo: "CD401", fecha: "2025-03-11", salida: "14:00", llegada: "17:15", estado: "Programado", origen: "EZE", destino: "GRU" },
            { id: 2002, codigo: "CD402", fecha: "2025-03-18", salida: "16:30", llegada: "19:45", estado: "Programado", origen: "EZE", destino: "GRU" }
          ]
        },
        {
          id: 3,
          nombre: "São Paulo - Lima",
          descripcion: "Cruzando el continente",
          origen: "GRU",
          destino: "LIM",
          categoria: "Internacional",
          estado: "Confirmada", 
          imagen: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "10:30",
          hora_llegada: "13:45",
          costo_turista: 520.00,
          costo_ejecutivo: 750.00,
          costo_primera: 1100.00,
          aerolinea: "Aurora Air",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 3001, codigo: "AR331", fecha: "2025-03-13", salida: "10:30", llegada: "13:45", estado: "Programado", origen: "GRU", destino: "LIM" },
            { id: 3002, codigo: "AR332", fecha: "2025-03-20", salida: "11:15", llegada: "14:30", estado: "Confirmado", origen: "GRU", destino: "LIM" }
          ]
        }
      ]
    },
    {
      id: 2,
      nombre: "Circuito Atlántico",
      descripcion: "Recorre las principales ciudades de la costa atlántica sudamericana",
      periodo_validez: "2025-03-01 a 2025-11-30",
      descuento: 20,
      fecha_alta: "2024-11-15",
      imagen: "https://images.unsplash.com/photo-1516738901171-8eb4fc13bd20?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 1890.00,
      rutas_vuelo: [
        {
          id: 4,
          nombre: "Montevideo - Río de Janeiro",
          descripcion: "Directo a la ciudad maravillosa",
          origen: "MVD",
          destino: "GIG",
          categoria: "Internacional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "11:00",
          hora_llegada: "14:20",
          costo_turista: 380.00,
          costo_ejecutivo: 580.00,
          costo_primera: 850.00,
          aerolinea: "AirUY",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 4001, codigo: "UY201", fecha: "2025-03-14", salida: "11:00", llegada: "14:20", estado: "Programado", origen: "MVD", destino: "GIG" },
            { id: 4002, codigo: "UY202", fecha: "2025-03-21", salida: "12:30", llegada: "15:50", estado: "Confirmado", origen: "MVD", destino: "GIG" }
          ]
        },
        {
          id: 5,
          nombre: "Río de Janeiro - Salvador",
          descripcion: "Costa brasileña",
          origen: "GIG",
          destino: "SSA",
          categoria: "Doméstica",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "16:00",
          hora_llegada: "18:15",
          costo_turista: 290.00,
          costo_ejecutivo: 440.00,
          costo_primera: 620.00,
          aerolinea: "Pampas Air",
          cantidad_en_paquete: 2,
          vuelos: [
            { id: 5001, codigo: "PA101", fecha: "2025-03-15", salida: "16:00", llegada: "18:15", estado: "Programado", origen: "GIG", destino: "SSA" },
            { id: 5002, codigo: "PA102", fecha: "2025-03-16", salida: "17:30", llegada: "19:45", estado: "Programado", origen: "GIG", destino: "SSA" },
            { id: 5003, codigo: "PA103", fecha: "2025-03-22", salida: "15:15", llegada: "17:30", estado: "Confirmado", origen: "GIG", destino: "SSA" }
          ]
        }
      ]
    },
    {
      id: 3,
      nombre: "Ruta Andina Express",
      descripcion: "Explora la majestuosa cordillera de los Andes en un viaje inolvidable",
      periodo_validez: "2025-02-01 a 2025-10-31",
      descuento: 25,
      fecha_alta: "2024-10-20",
      imagen: "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 3200.00,
      rutas_vuelo: [
        {
          id: 6,
          nombre: "Lima - La Paz",
          descripcion: "Hacia el altiplano boliviano",
          origen: "LIM",
          destino: "LPB",
          categoria: "Internacional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "07:30",
          hora_llegada: "09:45",
          costo_turista: 420.00,
          costo_ejecutivo: 640.00,
          costo_primera: 890.00,
          aerolinea: "Aurora Air",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 6001, codigo: "AR401", fecha: "2025-03-16", salida: "07:30", llegada: "09:45", estado: "Programado", origen: "LIM", destino: "LPB" },
            { id: 6002, codigo: "AR402", fecha: "2025-03-23", salida: "08:15", llegada: "10:30", estado: "Confirmado", origen: "LIM", destino: "LPB" }
          ]
        },
        {
          id: 7,
          nombre: "La Paz - Cusco", 
          descripcion: "Rumbo al ombligo del mundo",
          origen: "LPB",
          destino: "CUZ",
          categoria: "Internacional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "13:00",
          hora_llegada: "15:30",
          costo_turista: 380.00,
          costo_ejecutivo: 570.00,
          costo_primera: 810.00,
          aerolinea: "Cóndor Air",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 7001, codigo: "CD501", fecha: "2025-03-17", salida: "13:00", llegada: "15:30", estado: "Programado", origen: "LPB", destino: "CUZ" },
            { id: 7002, codigo: "CD502", fecha: "2025-03-24", salida: "14:20", llegada: "16:50", estado: "Programado", origen: "LPB", destino: "CUZ" }
          ]
        },
        {
          id: 8,
          nombre: "Cusco - Santiago",
          descripcion: "Cruzando los Andes hacia Chile",
          origen: "CUZ",
          destino: "SCL",
          categoria: "Internacional",
          estado: "Confirmada",
          imagen: "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=800&h=400&fit=crop&crop=entropy&q=80",
          hora_salida: "09:00",
          hora_llegada: "12:45",
          costo_turista: 590.00,
          costo_ejecutivo: 850.00,
          costo_primera: 1200.00,
          aerolinea: "AirUY",
          cantidad_en_paquete: 1,
          vuelos: [
            { id: 8001, codigo: "UY301", fecha: "2025-03-18", salida: "09:00", llegada: "12:45", estado: "Programado", origen: "CUZ", destino: "SCL" },
            { id: 8002, codigo: "UY302", fecha: "2025-03-25", salida: "10:30", llegada: "14:15", estado: "Confirmado", origen: "CUZ", destino: "SCL" }
          ]
        }
      ]
    }
  ];

  // === FUNCIONES PRINCIPALES ===

  // Exponer datos para debugging
  window.PaquetesRutasVuelo = window.PaquetesRutasVuelo || {};
  window.PaquetesRutasVuelo.debugPaquetes = () => console.table(PAQUETES_RUTAS_VUELO);
  
  // Obtener todos los paquetes
  window.PaquetesRutasVuelo.getAllPaquetes = () => PAQUETES_RUTAS_VUELO;
  
  // Obtener paquete por ID
  window.PaquetesRutasVuelo.getPaqueteById = (id) => {
    return PAQUETES_RUTAS_VUELO.find(p => p.id === parseInt(id));
  };

  // === FUNCIONES DE RENDERIZADO ===

  function renderPaquetesList() {
    const listContainer = document.getElementById('paquetesList');
    if (!listContainer) return;

    listContainer.innerHTML = '';

    PAQUETES_RUTAS_VUELO.forEach(paquete => {
      const paqueteCard = document.createElement('div');
      paqueteCard.className = 'col-md-6 col-lg-4 mb-4';
      
      paqueteCard.innerHTML = `
        <div class="card h-100 paquete-card" data-paquete-id="${paquete.id}">
          <img src="${paquete.imagen}" class="card-img-top" alt="${paquete.nombre}" style="height: 200px; object-fit: cover;" 
               onerror="this.src='https://via.placeholder.com/400x250/6c757d/ffffff?text=Imagen+No+Disponible'">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${paquete.nombre}</h5>
            <p class="card-text flex-grow-1">${paquete.descripcion}</p>
            <div class="mt-auto">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="badge bg-success">-${paquete.descuento}%</span>
                <strong class="text-primary">$${paquete.costo_total.toFixed(2)}</strong>
              </div>
              <div class="small text-muted mb-2">
                <i class="fas fa-calendar"></i> Válido: ${paquete.periodo_validez}
              </div>
              <div class="small text-muted mb-3">
                <i class="fas fa-route"></i> ${paquete.rutas_vuelo.length} rutas incluidas
              </div>
              <button class="btn btn-primary w-100" onclick="mostrarDetallesPaquete(${paquete.id})">
                Ver Detalles
              </button>
            </div>
          </div>
        </div>
      `;
      
      listContainer.appendChild(paqueteCard);
    });

    // Actualizar contador
    const countElement = document.getElementById('paquetesCount');
    if (countElement) {
      countElement.textContent = `${PAQUETES_RUTAS_VUELO.length} paquete${PAQUETES_RUTAS_VUELO.length !== 1 ? 's' : ''} disponible${PAQUETES_RUTAS_VUELO.length !== 1 ? 's' : ''}`;
    }
  }

  function mostrarDetallesPaquete(paqueteId) {
    const paquete = window.PaquetesRutasVuelo.getPaqueteById(paqueteId);
    if (!paquete) {
      alert('Paquete no encontrado');
      return;
    }

    // Ocultar lista y mostrar detalles
    document.getElementById('listView').style.display = 'none';
    document.getElementById('detailView').style.display = 'block';

    // Actualizar título
    document.getElementById('detailTitle').textContent = paquete.nombre;

    // Renderizar información básica
    document.getElementById('detailInfo').innerHTML = `
      <div class="row">
        <div class="col-md-6">
          <img src="${paquete.imagen}" class="img-fluid rounded mb-3" alt="${paquete.nombre}"
               onerror="this.src='https://via.placeholder.com/600x300/6c757d/ffffff?text=Imagen+No+Disponible'">
        </div>
        <div class="col-md-6">
          <h4>${paquete.nombre}</h4>
          <p class="text-muted">${paquete.descripcion}</p>
          
          <div class="row g-3">
            <div class="col-sm-6">
              <div class="card bg-light">
                <div class="card-body text-center p-3">
                  <h5 class="card-title text-success mb-1">$${paquete.costo_total.toFixed(2)}</h5>
                  <small class="text-muted">Costo Total</small>
                </div>
              </div>
            </div>
            <div class="col-sm-6">
              <div class="card bg-light">
                <div class="card-body text-center p-3">
                  <h5 class="card-title text-primary mb-1">${paquete.descuento}%</h5>
                  <small class="text-muted">Descuento</small>
                </div>
              </div>
            </div>
          </div>
          
          <div class="mt-3">
            <p><strong>Período de validez:</strong> ${paquete.periodo_validez}</p>
            <p><strong>Fecha de alta:</strong> ${new Date(paquete.fecha_alta).toLocaleDateString('es-ES')}</p>
            <p><strong>Rutas incluidas:</strong> ${paquete.rutas_vuelo.length}</p>
          </div>
        </div>
      </div>
    `;

    // Renderizar rutas de vuelo
    const rutasContainer = document.getElementById('rutasVueloList');
    rutasContainer.innerHTML = '';

    paquete.rutas_vuelo.forEach((ruta, index) => {
      const rutaCard = document.createElement('div');
      rutaCard.className = 'col-12 mb-3';
      
      rutaCard.innerHTML = `
        <div class="card ruta-card" style="cursor: pointer;" onclick="mostrarDetallesRuta(${ruta.id})">
          <div class="card-body">
            <div class="row align-items-center">
              <div class="col-md-8">
                <h6 class="card-title mb-1">
                  <span class="badge bg-secondary me-2">${index + 1}</span>
                  ${ruta.nombre}
                  <i class="fas fa-external-link-alt ms-2 text-primary small"></i>
                </h6>
                <p class="card-text text-muted small mb-2">${ruta.descripcion}</p>
                <div class="d-flex gap-3 small text-muted">
                  <span><i class="fas fa-plane-departure"></i> ${ruta.hora_salida}</span>
                  <span><i class="fas fa-plane-arrival"></i> ${ruta.hora_llegada}</span>
                  <span><i class="fas fa-building"></i> ${ruta.aerolinea}</span>
                </div>
                <div class="mt-2">
                  <span class="badge bg-info text-dark me-1">${ruta.categoria}</span>
                  <span class="badge bg-success">${ruta.estado}</span>
                </div>
              </div>
              <div class="col-md-4">
                <div class="text-end">
                  <div class="mb-1">
                    <strong>Cantidad: ${ruta.cantidad_en_paquete}</strong>
                  </div>
                  <div class="small">
                    <div>Turista: $${ruta.costo_turista.toFixed(2)}</div>
                    <div>Ejecutivo: $${ruta.costo_ejecutivo.toFixed(2)}</div>
                    <div>Primera: $${ruta.costo_primera.toFixed(2)}</div>
                  </div>
                  <div class="mt-2">
                    <small class="text-primary">
                      <i class="fas fa-plane me-1"></i>${ruta.vuelos.length} vuelos
                    </small>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      `;
      
      rutasContainer.appendChild(rutaCard);
    });
  }

  // Función para mostrar detalles de ruta individual
  function mostrarDetallesRuta(rutaId) {
    // Buscar la ruta en todos los paquetes
    let rutaEncontrada = null;
    let paquetePadre = null;
    
    for (const paquete of PAQUETES_RUTAS_VUELO) {
      const ruta = paquete.rutas_vuelo.find(r => r.id === rutaId);
      if (ruta) {
        rutaEncontrada = ruta;
        paquetePadre = paquete;
        break;
      }
    }

    if (!rutaEncontrada) {
      alert('Ruta no encontrada');
      return;
    }

    // Crear y mostrar modal
    mostrarModalRuta(rutaEncontrada, paquetePadre);
  }

  function mostrarModalRuta(ruta, paquete) {
    // Eliminar modal existente si existe
    const existingModal = document.getElementById('rutaModal');
    if (existingModal) {
      existingModal.remove();
    }

    // Crear modal dinámicamente
    const modalHTML = `
      <div class="modal fade" id="rutaModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Detalles de Ruta: ${ruta.nombre}</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <div class="row">
                <div class="col-md-6">
                  <img src="${ruta.imagen}" class="img-fluid rounded mb-3" alt="${ruta.nombre}"
                       onerror="this.onerror=null; this.src=getBackupImage('${ruta.nombre}', '${ruta.origen}', '${ruta.destino}')">
                </div>
                <div class="col-md-6">
                  <div class="mb-2">
                    <strong>Tramo:</strong> ${ruta.origen} → ${ruta.destino}
                  </div>
                  <div class="mb-2">
                    <strong>Aerolínea:</strong> ${ruta.aerolinea}
                  </div>
                  <div class="mb-2">
                    <strong>Categoría:</strong> 
                    <span class="badge bg-info text-dark">${ruta.categoria}</span>
                  </div>
                  <div class="mb-2">
                    <strong>Estado:</strong> 
                    <span class="badge bg-success">${ruta.estado}</span>
                  </div>
                  <div class="mb-2">
                    <strong>Incluido en paquete:</strong> ${paquete.nombre}
                  </div>
                  <div class="mb-3">
                    <strong>Cantidad en paquete:</strong> ${ruta.cantidad_en_paquete}
                  </div>
                  
                  <h6>Precios por Clase:</h6>
                  <div class="row g-2 mb-3">
                    <div class="col-4">
                      <div class="card bg-light text-center p-2">
                        <small class="text-muted">Turista</small>
                        <strong>$${ruta.costo_turista.toFixed(2)}</strong>
                      </div>
                    </div>
                    <div class="col-4">
                      <div class="card bg-light text-center p-2">
                        <small class="text-muted">Ejecutivo</small>
                        <strong>$${ruta.costo_ejecutivo.toFixed(2)}</strong>
                      </div>
                    </div>
                    <div class="col-4">
                      <div class="card bg-light text-center p-2">
                        <small class="text-muted">Primera</small>
                        <strong>$${ruta.costo_primera.toFixed(2)}</strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="mt-4">
                <p class="text-muted">${ruta.descripcion}</p>
              </div>

              <h6 class="mt-4">Vuelos Asociados</h6>
              <div class="table-responsive">
                <table class="table table-sm align-middle">
                  <thead>
                    <tr>
                      <th>Código</th>
                      <th>Fecha</th>
                      <th>Tramo</th>
                      <th>Horario</th>
                      <th>Estado</th>
                    </tr>
                  </thead>
                  <tbody>
                    ${ruta.vuelos.map(vuelo => `
                      <tr class="hover-row" style="cursor: pointer;" onclick="mostrarDetallesVuelo(${vuelo.id}, ${ruta.id})">
                        <td><strong>${vuelo.codigo}</strong></td>
                        <td>${vuelo.fecha}</td>
                        <td>${vuelo.origen} → ${vuelo.destino}</td>
                        <td>${vuelo.salida} — ${vuelo.llegada}</td>
                        <td>
                          <span class="badge ${vuelo.estado === 'Programado' ? 'bg-primary' : 'bg-success'}">
                            ${vuelo.estado}
                          </span>
                        </td>
                      </tr>
                    `).join('')}
                  </tbody>
                </table>
              </div>
              <p class="text-muted small mb-0 mt-2">
                <i class="fas fa-info-circle me-1"></i>
                Haz clic en cualquier vuelo para ver más detalles
              </p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
              <button type="button" class="btn btn-primary" data-roles="Cliente">
                <i class="fas fa-shopping-cart me-1"></i>Reservar Vuelo
              </button>
            </div>
          </div>
        </div>
      </div>
    `;

    // Agregar modal al DOM
    document.body.insertAdjacentHTML('beforeend', modalHTML);
    
    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('rutaModal'));
    modal.show();

    // Limpiar modal cuando se cierre
    document.getElementById('rutaModal').addEventListener('hidden.bs.modal', function() {
      this.remove();
    });
  }

  function mostrarDetallesVuelo(vueloId, rutaId) {
    // Buscar el vuelo específico
    let vueloEncontrado = null;
    let rutaPadre = null;

    for (const paquete of PAQUETES_RUTAS_VUELO) {
      for (const ruta of paquete.rutas_vuelo) {
        if (ruta.id === rutaId) {
          const vuelo = ruta.vuelos.find(v => v.id === vueloId);
          if (vuelo) {
            vueloEncontrado = vuelo;
            rutaPadre = ruta;
            break;
          }
        }
      }
      if (vueloEncontrado) break;
    }

    if (!vueloEncontrado) {
      alert('Vuelo no encontrado');
      return;
    }

    // Crear modal de vuelo
    mostrarModalVuelo(vueloEncontrado, rutaPadre);
  }

  function mostrarModalVuelo(vuelo, ruta) {
    // Eliminar modal existente si existe
    const existingModal = document.getElementById('vueloModal');
    if (existingModal) {
      existingModal.remove();
    }

    const modalHTML = `
      <div class="modal fade" id="vueloModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Vuelo ${vuelo.codigo} — ${ruta.origen} → ${ruta.destino}</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <div class="row">
                <div class="col-6">
                  <div class="card bg-light mb-3">
                    <div class="card-body text-center">
                      <i class="fas fa-plane-departure fa-2x text-primary mb-2"></i>
                      <h6>Salida</h6>
                      <div><strong>${vuelo.origen}</strong></div>
                      <div>${vuelo.salida}</div>
                    </div>
                  </div>
                </div>
                <div class="col-6">
                  <div class="card bg-light mb-3">
                    <div class="card-body text-center">
                      <i class="fas fa-plane-arrival fa-2x text-success mb-2"></i>
                      <h6>Llegada</h6>
                      <div><strong>${vuelo.destino}</strong></div>
                      <div>${vuelo.llegada}</div>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="mb-2">
                <strong>Aerolínea:</strong> ${ruta.aerolinea}
              </div>
              <div class="mb-2">
                <strong>Ruta:</strong> ${ruta.nombre}
              </div>
              <div class="mb-2">
                <strong>Fecha:</strong> ${vuelo.fecha}
              </div>
              <div class="mb-2">
                <strong>Estado:</strong> 
                <span class="badge ${vuelo.estado === 'Programado' ? 'bg-primary' : 'bg-success'}">
                  ${vuelo.estado}
                </span>
              </div>
              
              <div class="mt-4">
                <h6>Precios Disponibles:</h6>
                <div class="row g-2">
                  <div class="col-4">
                    <div class="card text-center">
                      <div class="card-body p-2">
                        <small class="text-muted">Turista</small>
                        <div><strong>$${ruta.costo_turista.toFixed(2)}</strong></div>
                      </div>
                    </div>
                  </div>
                  <div class="col-4">
                    <div class="card text-center">
                      <div class="card-body p-2">
                        <small class="text-muted">Ejecutivo</small>
                        <div><strong>$${ruta.costo_ejecutivo.toFixed(2)}</strong></div>
                      </div>
                    </div>
                  </div>
                  <div class="col-4">
                    <div class="card text-center">
                      <div class="card-body p-2">
                        <small class="text-muted">Primera</small>
                        <div><strong>$${ruta.costo_primera.toFixed(2)}</strong></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
              <button type="button" class="btn btn-primary" data-roles="Cliente">
                <i class="fas fa-ticket-alt me-1"></i>Reservar Este Vuelo
              </button>
            </div>
          </div>
        </div>
      </div>
    `;

    // Agregar modal al DOM
    document.body.insertAdjacentHTML('beforeend', modalHTML);
    
    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('vueloModal'));
    modal.show();

    // Limpiar modal cuando se cierre
    document.getElementById('vueloModal').addEventListener('hidden.bs.modal', function() {
      this.remove();
    });
  }

  // Funciones globales
  window.mostrarDetallesPaquete = mostrarDetallesPaquete;
  window.mostrarDetallesRuta = mostrarDetallesRuta;
  window.mostrarDetallesVuelo = mostrarDetallesVuelo;
  
  window.volverALista = function() {
    document.getElementById('detailView').style.display = 'none';
    document.getElementById('listView').style.display = 'block';
  };

  // === FUNCIÓN PARA IMÁGENES DE RESPALDO ===
  window.getBackupImage = function(rutaNombre, origen, destino) {
    // Imágenes de respaldo específicas por ruta
    const backupImages = {
      "Río de Janeiro - Salvador": "https://images.unsplash.com/photo-1516306580123-e6e52b1b7b5f?w=800&h=400&fit=crop&crop=entropy&q=80",
      "Cusco - Santiago": "https://images.unsplash.com/photo-1555400292-1ccc6c9be2e9?w=800&h=400&fit=crop&crop=entropy&q=80",
      "Montevideo - Buenos Aires": "https://images.unsplash.com/photo-1589909202802-8f4aadce1849?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
      "Buenos Aires - São Paulo": "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800&h=400&fit=crop&crop=entropy&q=80",
      "São Paulo - Lima": "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop&crop=entropy&q=80",
      "Montevideo - Río de Janeiro": "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800&h=400&fit=crop&crop=entropy&q=80",
      "Lima - La Paz": "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop&crop=entropy&q=80",
      "La Paz - Cusco": "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=800&h=400&fit=crop&crop=entropy&q=80"
    };

    // Devolver imagen específica o placeholder genérico
    return backupImages[rutaNombre] || `https://via.placeholder.com/400x200/6c757d/ffffff?text=${encodeURIComponent(origen + '-' + destino)}`;
  };

  // === FUNCIONES DE DEBUG ===
  window.PaquetesRutasVuelo.checkImages = () => {
    console.log('Verificando imágenes de paquetes:');
    PAQUETES_RUTAS_VUELO.forEach(paquete => {
      console.log(`${paquete.nombre}: ${paquete.imagen}`);
      // Probar cargar imagen
      const img = new Image();
      img.onload = () => console.log(`✅ ${paquete.nombre} - Imagen carga correctamente`);
      img.onerror = () => console.log(`❌ ${paquete.nombre} - Error al cargar imagen`);
      img.src = paquete.imagen;
    });
  };

  window.PaquetesRutasVuelo.checkRutasImages = () => {
    console.log('Verificando imágenes de rutas:');
    PAQUETES_RUTAS_VUELO.forEach(paquete => {
      paquete.rutas_vuelo.forEach(ruta => {
        console.log(`${ruta.nombre}: ${ruta.imagen}`);
        const img = new Image();
        img.onload = () => console.log(`✅ ${ruta.nombre} - Imagen carga correctamente`);
        img.onerror = () => {
          console.log(`❌ ${ruta.nombre} - Error al cargar imagen, probando respaldo...`);
          const backup = window.getBackupImage(ruta.nombre, ruta.origen, ruta.destino);
          console.log(`🔄 Imagen de respaldo: ${backup}`);
        };
        img.src = ruta.imagen;
      });
    });
  };

  // === INICIALIZACIÓN ===
  document.addEventListener('DOMContentLoaded', function() {
    // Renderizar lista inicial
    renderPaquetesList();
    
    // Configurar búsqueda si existe
    const searchInput = document.getElementById('searchPaquetes');
    if (searchInput) {
      searchInput.addEventListener('input', function() {
        // Implementar filtrado si se requiere
        console.log('Buscando:', this.value);
      });
    }
  });

})();