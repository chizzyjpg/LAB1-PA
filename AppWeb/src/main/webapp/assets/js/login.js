/**
 * Funciones para el formulario de login
 * Maneja la autenticación y guardado de sesión
 */

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const loginError = document.getElementById('loginError');

    if (!loginForm) {
        console.warn('[login.js] Formulario de login no encontrado');
        return;
    }

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevenir envío tradicional del formulario
        
        // Ocultar mensaje de error previo
        if (loginError) {
            loginError.style.display = 'none';
        }

        // Obtener datos del formulario
        const loginId = document.getElementById('loginId').value.trim();
        const loginPass = document.getElementById('loginPass').value;

        // Validar que los campos no estén vacíos
        if (!loginId || !loginPass) {
            showError('Por favor completa todos los campos');
            return;
        }

        // Verificar que el sistema de usuarios esté disponible
        if (!window.Volando || !window.Volando.findUser) {
            showError('Sistema de autenticación no disponible');
            console.error('window.Volando.findUser no está disponible. Asegúrate de que consulta-usuario.js está cargado.');
            return;
        }

        // Buscar usuario por nickname o email
        const user = findUserByNicknameOrEmail(loginId, loginPass);

        if (!user) {
            showError('Usuario o contraseña incorrectos');
            return;
        }

        // Login exitoso - guardar sesión
        saveUserSession(user);

        // Mostrar mensaje de éxito y redirigir
        showSuccess('Iniciando sesión...');
        
        setTimeout(() => {
            window.location.href = 'home.html';
        }, 1000);
    });

    function findUserByNicknameOrEmail(loginId, password) {
        // Primero intentar buscar por nickname
        let user = window.Volando.findUser(loginId, password);
        
        if (!user) {
            // Si no se encontró por nickname, buscar por email
            // Necesitamos acceder a la lista de usuarios directamente
            if (window.Volando.USERS) {
                user = window.Volando.USERS.find(u => 
                    String(u.email) === String(loginId) && 
                    String(u.pass) === String(password)
                );
            }
        }
        
        return user;
    }

    function saveUserSession(user) {
        // Verificar que el sistema de auth esté disponible
        if (!window.Volando || !window.Volando.loginUser) {
            // Si auth.js no está disponible, usar localStorage directamente
            const sessionData = {
                id: user.id,
                rol: user.rol,
                nickname: user.nickname,
                nombre: user.nombre,
                email: user.email,
                avatar: user.imagen || null
            };
            localStorage.setItem('auth', JSON.stringify(sessionData));
            console.log('Sesión guardada en localStorage:', sessionData);
        } else {
            // Usar la función de auth.js si está disponible
            window.Volando.loginUser(user);
            console.log('Sesión guardada usando auth.js:', user);
        }
    }

    function showError(message) {
        if (loginError) {
            const alertDiv = loginError.querySelector('.alert');
            if (alertDiv) {
                alertDiv.textContent = message;
            }
            loginError.style.display = 'block';
        } else {
            alert('Error: ' + message);
        }
    }

    function showSuccess(message) {
        if (loginError) {
            const alertDiv = loginError.querySelector('.alert');
            if (alertDiv) {
                alertDiv.className = 'alert alert-success';
                alertDiv.textContent = message;
            }
            loginError.style.display = 'block';
        }
    }
});