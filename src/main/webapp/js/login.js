document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const loginButton = document.getElementById('loginButton');
    const buttonText = loginButton.querySelector('span');
    const errorBox = document.getElementById('errorMessage');

    const VALID_USERNAME = 'admin';
    const VALID_PASSWORD = 'admin123';

    function showErrorBox(message) {
        errorBox.textContent = message;
        errorBox.classList.remove('show');
        void errorBox.offsetWidth;
        errorBox.classList.add('show');
    }

    function hideErrorBox() {
        errorBox.classList.remove('show');
    }

    function validateUsername(username) {
        if (!username || username.trim() === '') {
            return 'Usuário é obrigatório';
        }
        if (username.length < 3) {
            return 'Usuário deve ter no mínimo 3 caracteres';
        }
        return null;
    }

    function validatePassword(password) {
        if (!password || password.trim() === '') {
            return 'Senha é obrigatória';
        }
        if (password.length < 6) {
            return 'Senha deve ter no mínimo 6 caracteres';
        }
        return null;
    }

    function validateCredentials(username, password) {
        return username === VALID_USERNAME && password === VALID_PASSWORD;
    }

    function setLoading(isLoading) {
        loginButton.disabled = isLoading;
        buttonText.textContent = isLoading ? 'ENTRANDO...' : 'ENTRAR';
        usernameInput.disabled = isLoading;
        passwordInput.disabled = isLoading;
    }

    function redirectToClients() {
        const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
        window.location.href = contextPath + '/clients/';
    }

    usernameInput.addEventListener('input', function() {
        hideErrorBox();
    });

    passwordInput.addEventListener('input', function() {
        hideErrorBox();
    });

    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        hideErrorBox();

        const username = usernameInput.value.trim();
        const password = passwordInput.value;
        let hasError = false;

        const usernameValidation = validateUsername(username);
        if (usernameValidation) {
            showErrorBox(usernameValidation);
            hasError = true;
        }

        const passwordValidation = validatePassword(password);
        if (!hasError && passwordValidation) { // Só mostra erro de senha se não houver erro de usuário
            showErrorBox(passwordValidation);
            hasError = true;
        }

        if (hasError) {
            return;
        }

        if (!validateCredentials(username, password)) {
            showErrorBox('Credenciais inválidas. Verifique seu usuário e senha.');
            return;
        }

        setLoading(true);
        setTimeout(function() {
            sessionStorage.setItem('authenticated', 'true');
            sessionStorage.setItem('username', username);
            sessionStorage.setItem('loginTime', new Date().toISOString());
            redirectToClients();
        }, 800);
    });

    usernameInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            passwordInput.focus();
        }
    });

    passwordInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            loginForm.dispatchEvent(new Event('submit'));
        }
    });
});
