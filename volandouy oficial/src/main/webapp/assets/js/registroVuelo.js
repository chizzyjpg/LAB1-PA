function cargarRutas() {
            const aerolineaSelect = document.getElementById('aerolinea');
            const rutaSelect = document.getElementById('rutaVuelo');
            
            // Limpiar rutas anteriores
            rutaSelect.innerHTML = '<option value="">Selecciona una ruta...</option>';
            
            if (aerolineaSelect.value) {
                rutaSelect.disabled = false;
                
                // Aquí cargarías las rutas según la aerolínea seleccionada
                // Ejemplo:
                if (aerolineaSelect.value === 'aerolinea1') {
                    rutaSelect.innerHTML += '<option value="ruta1">Montevideo - Buenos Aires</option>';
                    rutaSelect.innerHTML += '<option value="ruta2">Montevideo - São Paulo</option>';
                }
                // ... más aerolíneas
            } else {
                rutaSelect.disabled = true;
            }
        }