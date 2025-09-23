/**
 * Funciones para el formulario de registro
 * Maneja la lógica de campos dinámicos según el tipo de usuario
 */

// Función principal para cambiar campos según tipo de usuario
function toggleFields() {
    const userType = document.getElementById('userType').value;
    const clienteFields = document.getElementById('clienteFields');
    const aerolineaFields = document.getElementById('aerolineaFields');
    
    // Ocultar ambos conjuntos de campos
    clienteFields.style.display = 'none';
    aerolineaFields.style.display = 'none';
    
    // Limpiar campos required anteriores
    clearRequired();
    
    // Mostrar campos según el tipo seleccionado
    if (userType === 'cliente') {
        clienteFields.style.display = 'block';
        setClienteRequired();
    } else if (userType === 'aerolinea') {
        aerolineaFields.style.display = 'block';
        setAerolineaRequired();
    }
}

// Limpiar todas las validaciones required
function clearRequired() {
    // Limpiar required de campos de cliente
    const clienteInputs = ['nombre', 'apellido', 'tipoDocumento', 'numeroDocumento', 'fechaNacimiento', 'nacionalidad'];
    clienteInputs.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.required = false;
    });
    
    // Limpiar required de campos de aerolínea
    const aerolineaInputs = ['nombreAerolinea', 'descripcion', 'sitioWeb'];
    aerolineaInputs.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.required = false;
    });
}

// Establecer campos requeridos para cliente
function setClienteRequired() {
    const requiredFields = ['nombre', 'apellido', 'tipoDocumento', 'numeroDocumento', 'fechaNacimiento', 'nacionalidad'];
    requiredFields.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.required = true;
    });
}

// Establecer campos requeridos para aerolínea
function setAerolineaRequired() {
    const requiredFields = ['nombreAerolinea', 'descripcion'];
    requiredFields.forEach(id => {
        const element = document.getElementById(id);
        if (element) element.required = true;
    });
}

// Validación de formulario antes de enviar
function validateForm(event) {
    const userType = document.getElementById('userType').value;
    
    if (!userType) {
        event.preventDefault();
        alert('Por favor selecciona un tipo de usuario');
        return false;
    }
    
    // Validaciones adicionales según el tipo
    if (userType === 'cliente') {
        return validateClienteForm(event);
    } else if (userType === 'aerolinea') {
        return validateAerolineaForm(event);
    }
    
    return true;
}

// Validaciones específicas para cliente
function validateClienteForm(event) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (password !== confirmPassword) {
        event.preventDefault();
        alert('Las contraseñas no coinciden');
        return false;
    }
    
    return true;
}

// Validaciones específicas para aerolínea
function validateAerolineaForm(event) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (password !== confirmPassword) {
        event.preventDefault();
        alert('Las contraseñas no coinciden');
        return false;
    }
    
    return true;
}

// Inicialización cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    // Agregar event listener al formulario
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', validateForm);
    }
    
    // Inicializar campos ocultos
    const clienteFields = document.getElementById('clienteFields');
    const aerolineaFields = document.getElementById('aerolineaFields');
    if (clienteFields) clienteFields.style.display = 'none';
    if (aerolineaFields) aerolineaFields.style.display = 'none';
});