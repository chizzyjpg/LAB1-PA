/**
 * Comprar Paquete de Rutas de Vuelo
 * Implementa el caso de uso completo de compra de paquetes
 */

(() => {
  "use strict";

  // === DATOS MOCK PARA COMPRAS DE PAQUETES ===
  
  // Utilizamos los mismos paquetes de consulta-paquetes.js
  const PAQUETES_DISPONIBLES = [
    {
      id: 1,
      nombre: "Escapada Sudamericana",
      descripcion: "Descubre los destinos más fascinantes de Sudamérica con este paquete completo",
      periodo_validez: "2025-01-01 a 2025-12-31",
      descuento: 15,
      fecha_alta: "2024-12-01",
      imagen: "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 2850.00,
      validez_dias: 365,
      cant_rutas: 3,
      estado: "Disponible"
    },
    {
      id: 2,
      nombre: "Ruta Andina Express",
      descripcion: "Conecta las principales ciudades de la cordillera andina con vuelos directos",
      periodo_validez: "2025-02-01 a 2025-11-30",
      descuento: 20,
      fecha_alta: "2024-11-15",
      imagen: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 3200.00,
      validez_dias: 302,
      cant_rutas: 2,
      estado: "Disponible"
    },
    {
      id: 3,
      nombre: "Circuito Atlántico Sur",
      descripcion: "Explora las costas atlánticas desde Uruguay hasta Brasil con conexiones perfectas",
      periodo_validez: "2025-03-01 a 2025-10-31",
      descuento: 12,
      fecha_alta: "2024-10-20",
      imagen: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=250&fit=crop&crop=entropy&q=80",
      costo_total: 1950.00,
      validez_dias: 244,
      cant_rutas: 3,
      estado: "Disponible"
    }
  ];

  // Simulación de compras existentes del usuario (para validar duplicados)
  let COMPRAS_USUARIO = [];

  // === FUNCIONES PRINCIPALES ===

  /**
   * Inicializa la página de comprar paquetes
   */
  function inicializarCompraPaquetes() {
    console.log('Inicializando página de compra de paquetes...');
    
    // Verificar autenticación
    const usuario = obtenerUsuarioLogueado();
    if (!usuario) {
      mostrarError('Debe estar logueado para comprar paquetes');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
      return;
    }

    // Solo clientes pueden comprar paquetes
    if (usuario.rol !== 'Cliente') {
      mostrarError('Solo los clientes pueden comprar paquetes');
      return;
    }

    cargarComprasExistentes(usuario.nickname);
    mostrarPaquetesDisponibles();
    configurarEventos();
  }

  /**
   * Obtiene el usuario logueado desde el sistema de auth
   */
  function obtenerUsuarioLogueado() {
    try {
      const authData = localStorage.getItem('auth');
      if (!authData) return null;
      
      const auth = JSON.parse(authData);
      return auth;
    } catch (error) {
      console.error('Error al obtener usuario logueado:', error);
      return null;
    }
  }

  /**
   * Carga las compras existentes del usuario
   */
  function cargarComprasExistentes(nickname) {
    // Simulamos cargar compras del localStorage
    const comprasKey = `compras_${nickname}`;
    const comprasGuardadas = localStorage.getItem(comprasKey);
    
    if (comprasGuardadas) {
      try {
        COMPRAS_USUARIO = JSON.parse(comprasGuardadas);
      } catch (error) {
        console.error('Error al cargar compras:', error);
        COMPRAS_USUARIO = [];
      }
    }
  }

  /**
   * Verifica si el usuario ya compro un paquete específico (solo compras activas)
   */
  function usuarioYaComproPaquete(nombrePaquete) {
    return COMPRAS_USUARIO.some(compra => 
      compra.nombre_paquete === nombrePaquete && compra.estado === 'Activa'
    );
  }

  /**
   * Muestra los paquetes disponibles para compra
   */
  function mostrarPaquetesDisponibles() {
    const container = document.querySelector('main');
    if (!container) return;

    // Filtrar paquetes que tienen al menos una ruta
    const paquetesConRutas = PAQUETES_DISPONIBLES.filter(paquete => 
      paquete.cant_rutas > 0 && paquete.estado === 'Disponible'
    );

    const html = `
      <div class="container-fluid py-4">
        <div class="row">
          <div class="col-12">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h1 class="col-12 col-lg-9 col-xl-10 py-4 text-center">
                <i class="fas fa-shopping-cart me-2"></i>
                Comprar Paquete de Rutas de Vuelo
              </h1>
              <div class="text-muted">
                <i class="fas fa-package me-1"></i>
                ${paquetesConRutas.length} paquetes disponibles
              </div>
            </div>
          </div>
        </div>

        <div class="row g-4" id="paquetesContainer">
          ${paquetesConRutas.map(paquete => renderPaqueteCard(paquete)).join('')}
        </div>
      </div>
    `;

    container.innerHTML = html;
  }

  /**
   * Renderiza una tarjeta de paquete
   */
  function renderPaqueteCard(paquete) {
    const yaComprado = usuarioYaComproPaquete(paquete.nombre);
    const fechaVencimiento = calcularFechaVencimiento(paquete.validez_dias);
    const imagenFallback = obtenerImagenFallback(paquete.nombre);
    
    return `
      <div class="col-lg-4 col-md-6">
        <div class="card h-100 shadow-sm paquete-card" data-paquete-id="${paquete.id}">
          <div class="position-relative">
            <img src="${paquete.imagen}" 
                 class="card-img-top" 
                 alt="${paquete.nombre}"
                 style="height: 200px; object-fit: cover;"
                 onerror="this.onerror=null; this.src='${imagenFallback}'; console.log('🔄 Usando imagen fallback para: ${paquete.nombre}');">
            
            ${yaComprado ? `
              <div class="position-absolute top-0 end-0 m-2">
                <span class="badge bg-success">
                  <i class="fas fa-check me-1"></i>Ya adquirido
                </span>
              </div>
            ` : ''}
            
            <div class="position-absolute bottom-0 start-0 m-2">
              <span class="badge bg-primary">${paquete.descuento}% descuento</span>
            </div>
          </div>
          
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${paquete.nombre}</h5>
            <p class="card-text text-muted">${paquete.descripcion}</p>
            
            <div class="mt-auto">
              <div class="row g-2 mb-3">
                <div class="col-6">
                  <small class="text-muted">Rutas incluidas:</small>
                  <div class="fw-bold">${paquete.cant_rutas} rutas</div>
                </div>
                <div class="col-6">
                  <small class="text-muted">Validez:</small>
                  <div class="fw-bold">${paquete.validez_dias} días</div>
                </div>
              </div>
              
              <div class="mb-3">
                <small class="text-muted">Período de validez:</small>
                <div class="fw-bold">${paquete.periodo_validez}</div>
              </div>
              
              <div class="d-flex justify-content-between align-items-center">
                <div>
                  <small class="text-muted">Precio:</small>
                  <div class="h5 text-success mb-0">$${paquete.costo_total.toFixed(2)} USD</div>
                </div>
                
                ${yaComprado ? `
                  <div class="d-flex gap-2">
                    <button class="btn btn-outline-success btn-sm" disabled>
                      <i class="fas fa-check me-1"></i>Adquirido
                    </button>
                    <button class="btn btn-outline-danger btn-sm" onclick="window.ComprarPaquetes.mostrarCancelacionCompra('${paquete.nombre}')">
                      <i class="fas fa-times me-1"></i>Cancelar
                    </button>
                  </div>
                ` : `
                  <button class="btn btn-primary" onclick="window.ComprarPaquetes.mostrarDetallesCompra(${paquete.id})">
                    <i class="fas fa-shopping-cart me-1"></i>Comprar
                  </button>
                `}
              </div>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  /**
   * Calcula la fecha de vencimiento basada en los días de validez
   */
  function calcularFechaVencimiento(diasValidez) {
    const fechaActual = new Date();
    const fechaVencimiento = new Date(fechaActual);
    fechaVencimiento.setDate(fechaActual.getDate() + diasValidez);
    
    return fechaVencimiento.toLocaleDateString('es-UY', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  /**
   * Muestra los detalles de compra de un paquete
   */
  function mostrarDetallesCompra(paqueteId) {
    const paquete = PAQUETES_DISPONIBLES.find(p => p.id === paqueteId);
    if (!paquete) {
      mostrarError('Paquete no encontrado');
      return;
    }

    // Verificar nuevamente si ya lo compró
    if (usuarioYaComproPaquete(paquete.nombre)) {
      mostrarInfo('Ya has adquirido este paquete anteriormente');
      return;
    }

    const usuario = obtenerUsuarioLogueado();
    const fechaCompra = new Date().toLocaleDateString('es-UY');
    const fechaVencimiento = calcularFechaVencimiento(paquete.validez_dias);

    const modalHtml = `
      <div class="modal fade" id="modalComprarPaquete" tabindex="-1">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header bg-primary text-white">
              <h5 class="modal-title">
                <i class="fas fa-shopping-cart me-2"></i>
                Confirmar Compra de Paquete
              </h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            
            <div class="modal-body">
              <div class="row">
                <div class="col-md-4">
                  <img src="${paquete.imagen}" 
                       class="img-fluid rounded mb-3" 
                       alt="${paquete.nombre}"
                       onerror="this.onerror=null; this.src='${obtenerImagenFallback(paquete.nombre)}'; console.log('🔄 Usando imagen fallback en modal para: ${paquete.nombre}');">
                </div>
                <div class="col-md-8">
                  <h4 class="text-primary">${paquete.nombre}</h4>
                  <p class="text-muted">${paquete.descripcion}</p>
                  
                  <div class="row g-3">
                    <div class="col-sm-6">
                      <strong>Cliente:</strong><br>
                      <span class="text-muted">${usuario.nombre} (${usuario.nickname})</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Fecha de compra:</strong><br>
                      <span class="text-muted">${fechaCompra}</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Rutas incluidas:</strong><br>
                      <span class="text-muted">${paquete.cant_rutas} rutas de vuelo</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Validez:</strong><br>
                      <span class="text-muted">${paquete.validez_dias} días</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Vence:</strong><br>
                      <span class="text-muted">${fechaVencimiento}</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Descuento:</strong><br>
                      <span class="text-success">${paquete.descuento}% de descuento</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <hr>
              
              <div class="bg-light p-3 rounded">
                <div class="row align-items-center">
                  <div class="col">
                    <h5 class="mb-0">Total a pagar:</h5>
                    <small class="text-muted">Precio final con descuento incluido</small>
                  </div>
                  <div class="col-auto">
                    <span class="h3 text-success mb-0">$${paquete.costo_total.toFixed(2)} USD</span>
                  </div>
                </div>
              </div>
              
              <div class="alert alert-info mt-3">
                <i class="fas fa-info-circle me-2"></i>
                Al confirmar la compra, el paquete quedará disponible en tu cuenta por ${paquete.validez_dias} días a partir de hoy.
              </div>
            </div>
            
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                <i class="fas fa-times me-1"></i>Cancelar
              </button>
              <button type="button" class="btn btn-primary" onclick="window.ComprarPaquetes.confirmarCompra(${paquete.id})">
                <i class="fas fa-credit-card me-1"></i>Confirmar Compra
              </button>
            </div>
          </div>
        </div>
      </div>
    `;

    // Remover modal existente si hay uno
    const existingModal = document.getElementById('modalComprarPaquete');
    if (existingModal) {
      existingModal.remove();
    }

    // Agregar modal al DOM
    document.body.insertAdjacentHTML('beforeend', modalHtml);

    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('modalComprarPaquete'));
    modal.show();
  }

  /**
   * Muestra el modal de cancelación de compra
   */
  function mostrarCancelacionCompra(nombrePaquete) {
    const usuario = obtenerUsuarioLogueado();
    if (!usuario) {
      mostrarError('Error: Usuario no autenticado');
      return;
    }

    // Buscar la compra específica
    const compra = COMPRAS_USUARIO.find(c => 
      c.nombre_paquete === nombrePaquete && c.estado === 'Activa'
    );

    if (!compra) {
      mostrarError('No se encontró la compra del paquete');
      return;
    }

    const paquete = PAQUETES_DISPONIBLES.find(p => p.nombre === nombrePaquete);
    const fechaCompra = new Date(compra.fecha_compra).toLocaleDateString('es-UY');
    const fechaVencimiento = new Date(compra.fecha_vencimiento).toLocaleDateString('es-UY');

    const modalHtml = `
      <div class="modal fade" id="modalCancelarCompra" tabindex="-1">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header bg-danger text-white">
              <h5 class="modal-title">
                <i class="fas fa-exclamation-triangle me-2"></i>
                Cancelar Compra de Paquete
              </h5>
              <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            
            <div class="modal-body">
              <div class="row">
                <div class="col-md-4">
                  <img src="${paquete?.imagen || obtenerImagenFallback(nombrePaquete)}" 
                       class="img-fluid rounded mb-3" 
                       alt="${nombrePaquete}"
                       onerror="this.onerror=null; this.src='${obtenerImagenFallback(nombrePaquete)}';">
                </div>
                <div class="col-md-8">
                  <h4 class="text-danger">${nombrePaquete}</h4>
                  <p class="text-muted">${paquete?.descripcion || 'Paquete de rutas de vuelo'}</p>
                  
                  <div class="row g-3">
                    <div class="col-sm-6">
                      <strong>Cliente:</strong><br>
                      <span class="text-muted">${usuario.nombre} (${usuario.nickname})</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Fecha de compra:</strong><br>
                      <span class="text-muted">${fechaCompra}</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Monto pagado:</strong><br>
                      <span class="text-muted">$${compra.costo.toFixed(2)} USD</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Fecha de vencimiento:</strong><br>
                      <span class="text-muted">${fechaVencimiento}</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Rutas incluidas:</strong><br>
                      <span class="text-muted">${compra.rutas_incluidas} rutas</span>
                    </div>
                    <div class="col-sm-6">
                      <strong>Estado:</strong><br>
                      <span class="badge bg-success">${compra.estado}</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <hr>
              
              <div class="alert alert-warning">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>¿Estás seguro de que deseas cancelar esta compra?</strong>
                <ul class="mb-0 mt-2">
                  <li>Perderás acceso a todas las rutas incluidas en este paquete</li>
                  <li>Esta acción no se puede deshacer</li>
                  <li>Se procesará la devolución del monto pagado</li>
                </ul>
              </div>
            </div>
            
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                <i class="fas fa-arrow-left me-1"></i>Mantener Compra
              </button>
              <button type="button" class="btn btn-danger" onclick="window.ComprarPaquetes.confirmarCancelacion('${nombrePaquete}')">
                <i class="fas fa-times me-1"></i>Confirmar Cancelación
              </button>
            </div>
          </div>
        </div>
      </div>
    `;

    // Remover modal existente si hay uno
    const existingModal = document.getElementById('modalCancelarCompra');
    if (existingModal) {
      existingModal.remove();
    }

    // Agregar modal al DOM
    document.body.insertAdjacentHTML('beforeend', modalHtml);

    // Mostrar modal
    const modal = new bootstrap.Modal(document.getElementById('modalCancelarCompra'));
    modal.show();
  }

  /**
   * Confirma la cancelación de la compra
   */
  function confirmarCancelacion(nombrePaquete) {
    const usuario = obtenerUsuarioLogueado();
    if (!usuario) {
      mostrarError('Error: Usuario no autenticado');
      return;
    }

    try {
      // Buscar y marcar la compra como cancelada
      const compraIndex = COMPRAS_USUARIO.findIndex(c => 
        c.nombre_paquete === nombrePaquete && c.estado === 'Activa'
      );

      if (compraIndex === -1) {
        mostrarError('No se encontró la compra activa del paquete');
        return;
      }

      // Marcar como cancelada en lugar de eliminar (para historial)
      COMPRAS_USUARIO[compraIndex].estado = 'Cancelada';
      COMPRAS_USUARIO[compraIndex].fecha_cancelacion = new Date().toISOString();

      // Guardar en localStorage
      const comprasKey = `compras_${usuario.nickname}`;
      localStorage.setItem(comprasKey, JSON.stringify(COMPRAS_USUARIO));

      // Cerrar modal
      const modal = bootstrap.Modal.getInstance(document.getElementById('modalCancelarCompra'));
      if (modal) {
        modal.hide();
      }

      // Mostrar mensaje de éxito
      mostrarExito(`Compra del paquete "${nombrePaquete}" cancelada exitosamente. Se procesará la devolución del monto pagado.`);

      // Actualizar la vista
      setTimeout(() => {
        mostrarPaquetesDisponibles();
      }, 2000);

    } catch (error) {
      console.error('Error al cancelar compra:', error);
      mostrarError('Error al procesar la cancelación. Intente nuevamente.');
    }
  }

  /**
   * Confirma la compra del paquete
   */
  function confirmarCompra(paqueteId) {
    const paquete = PAQUETES_DISPONIBLES.find(p => p.id === paqueteId);
    const usuario = obtenerUsuarioLogueado();
    
    if (!paquete || !usuario) {
      mostrarError('Error en los datos de compra');
      return;
    }

    // Verificar una vez más si ya compró el paquete
    if (usuarioYaComproPaquete(paquete.nombre)) {
      mostrarError('Ya has adquirido este paquete anteriormente');
      return;
    }

    try {
      // Crear registro de compra
      const compra = {
        id: Date.now(), // ID único basado en timestamp
        nombre_paquete: paquete.nombre,
        cliente_nickname: usuario.nickname,
        cliente_nombre: usuario.nombre,
        fecha_compra: new Date().toISOString(),
        fecha_vencimiento: new Date(Date.now() + (paquete.validez_dias * 24 * 60 * 60 * 1000)).toISOString(),
        costo: paquete.costo_total,
        descuento: paquete.descuento,
        rutas_incluidas: paquete.cant_rutas,
        validez_dias: paquete.validez_dias,
        estado: 'Activa'
      };

      // Agregar a compras del usuario
      COMPRAS_USUARIO.push(compra);

      // Guardar en localStorage
      const comprasKey = `compras_${usuario.nickname}`;
      localStorage.setItem(comprasKey, JSON.stringify(COMPRAS_USUARIO));

      // Cerrar modal
      const modal = bootstrap.Modal.getInstance(document.getElementById('modalComprarPaquete'));
      if (modal) {
        modal.hide();
      }

      // Mostrar mensaje de éxito
      mostrarExito(`¡Compra realizada exitosamente! El paquete "${paquete.nombre}" está ahora disponible en tu cuenta.`);

      // Actualizar la vista
      setTimeout(() => {
        mostrarPaquetesDisponibles();
      }, 2000);

    } catch (error) {
      console.error('Error al procesar compra:', error);
      mostrarError('Error al procesar la compra. Intente nuevamente.');
    }
  }

  /**
   * Configura los eventos de la página
   */
  function configurarEventos() {
    // Event delegation para botones dinámicos
    document.addEventListener('click', (e) => {
      if (e.target.matches('[data-action="comprar"]') || e.target.closest('[data-action="comprar"]')) {
        const button = e.target.matches('[data-action="comprar"]') ? e.target : e.target.closest('[data-action="comprar"]');
        const paqueteId = parseInt(button.dataset.paqueteId);
        mostrarDetallesCompra(paqueteId);
      }
    });
  }

  // === FUNCIONES DE UTILIDAD ===

  /**
   * Función de debug para verificar las imágenes de los paquetes
   */
  function verificarImagenesPaquetes() {
    console.log('🔍 Verificando imágenes de paquetes...');
    PAQUETES_DISPONIBLES.forEach(paquete => {
      const img = new Image();
      img.onload = () => console.log(`✅ ${paquete.nombre}: Imagen cargada correctamente`);
      img.onerror = () => console.log(`❌ ${paquete.nombre}: Error al cargar imagen - ${paquete.imagen}`);
      img.src = paquete.imagen;
    });
  }

  /**
   * Función de debug para mostrar el historial de compras del usuario
   */
  function mostrarHistorialCompras() {
    const usuario = obtenerUsuarioLogueado();
    if (!usuario) {
      console.log('❌ Usuario no autenticado');
      return;
    }

    console.log(`📋 Historial de compras para ${usuario.nickname}:`);
    if (COMPRAS_USUARIO.length === 0) {
      console.log('   No hay compras registradas');
      return;
    }

    COMPRAS_USUARIO.forEach((compra, index) => {
      const estado = compra.estado === 'Activa' ? '✅' : '❌';
      console.log(`${estado} ${index + 1}. ${compra.nombre_paquete}`);
      console.log(`   Estado: ${compra.estado}`);
      console.log(`   Comprado: ${new Date(compra.fecha_compra).toLocaleDateString('es-UY')}`);
      console.log(`   Costo: $${compra.costo} USD`);
      if (compra.fecha_cancelacion) {
        console.log(`   Cancelado: ${new Date(compra.fecha_cancelacion).toLocaleDateString('es-UY')}`);
      }
      console.log('   ---');
    });
  }

  /**
   * Obtiene una imagen de fallback mejorada basada en el nombre del paquete
   */
  function obtenerImagenFallback(nombrePaquete) {
    const fallbacks = {
      "Circuito Atlántico Sur": "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=400&h=250&fit=crop&crop=entropy&q=80",
      "Escapada Sudamericana": "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=400&h=250&fit=crop&crop=entropy&q=80",
      "Ruta Andina Express": "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=250&fit=crop&crop=entropy&q=80"
    };
    
    return fallbacks[nombrePaquete] || "https://via.placeholder.com/400x250/0d6efd/ffffff?text=Paquete+de+Vuelo";
  }

  function mostrarError(mensaje) {
    mostrarNotificacion(mensaje, 'error');
  }

  function mostrarExito(mensaje) {
    mostrarNotificacion(mensaje, 'success');
  }

  function mostrarInfo(mensaje) {
    mostrarNotificacion(mensaje, 'info');
  }

  function mostrarNotificacion(mensaje, tipo = 'info') {
    // Crear elemento de notificación
    const notification = document.createElement('div');
    notification.className = `alert alert-${tipo === 'error' ? 'danger' : tipo === 'success' ? 'success' : 'info'} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    
    notification.innerHTML = `
      <i class="fas fa-${tipo === 'error' ? 'exclamation-triangle' : tipo === 'success' ? 'check-circle' : 'info-circle'} me-2"></i>
      ${mensaje}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(notification);

    // Auto-remover después de 5 segundos
    setTimeout(() => {
      if (notification.parentNode) {
        notification.remove();
      }
    }, 5000);
  }

  // === EXPORTAR FUNCIONES AL SCOPE GLOBAL ===
  window.ComprarPaquetes = {
    inicializar: inicializarCompraPaquetes,
    mostrarDetallesCompra: mostrarDetallesCompra,
    confirmarCompra: confirmarCompra,
    mostrarCancelacionCompra: mostrarCancelacionCompra,
    confirmarCancelacion: confirmarCancelacion,
    verificarImagenes: verificarImagenesPaquetes,
    obtenerImagenFallback: obtenerImagenFallback,
    mostrarHistorial: mostrarHistorialCompras
  };

  // === INICIALIZACIÓN AUTOMÁTICA ===
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', inicializarCompraPaquetes);
  } else {
    inicializarCompraPaquetes();
  }

})();