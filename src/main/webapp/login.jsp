<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Tartaruga Cometa</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            position: relative;
        }
        .animated-bg {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(135deg, #047857, #059669, #10B981, #34D399);
            background-size: 400% 400%;
            animation: gradient 15s ease infinite;
            z-index: -1;
        }
        @keyframes gradient {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        .login-container {
            width: 100%;
            max-width: 450px;
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
            border: 1px solid rgba(255, 255, 255, 0.18);
            margin: 20px;
        }
        .logo-container {
            text-align: center;
            margin-bottom: 30px;
        }
        .logo-container img {
            width: 128px;
            height: 113px;
            object-fit: contain;
        }
        .title {
            color: white;
            font-size: 28px;
            font-weight: 300;
            text-align: center;
            margin-bottom: 30px;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .form-group {
            margin-bottom: 20px;
            position: relative;
        }
        .input-field {
            width: 100%;
            padding: 14px 16px;
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 8px;
            color: white;
            font-size: 16px;
            transition: all 0.3s ease;
        }
        .input-field::placeholder {
            color: rgba(255, 255, 255, 0.6);
        }
        .input-field:focus {
            outline: none;
            border-color: rgba(255, 255, 255, 0.8);
            background: rgba(255, 255, 255, 0.25);
        }
        .error-message {
            color: #FEE2E2;
            background: rgba(239, 68, 68, 0.3);
            border: 1px solid rgba(239, 68, 68, 0.5);
            border-radius: 8px;
            padding: 12px;
            margin-bottom: 20px;
            font-size: 14px;
            display: none;
            animation: shake 0.5s;
        }
        .error-message.show {
            display: block;
        }
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            10%, 30%, 50%, 70%, 90% { transform: translateX(-10px); }
            20%, 40%, 60%, 80% { transform: translateX(10px); }
        }

        .center {
            width: 100%;
            display: flex;
            justify-content: center;
            margin-bottom: 20px;
        }
        .btn {
            width: 180px;
            height: 60px;
            cursor: pointer;
            background: transparent;
            border: 1px solid #91C9FF;
            outline: none;
            transition: 1s ease-in-out;
            position: relative;
            overflow: hidden;
            border-radius: 8px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .btn span {
            color: white;
            font-size: 18px;
            font-weight: 500;
            position: relative;
            z-index: 2;
        }
        .btn svg {
            position: absolute;
            left: 0;
            top: 0;
            fill: none;
            stroke: #fff;
            stroke-width: 2;
            stroke-dasharray: 150 480;
            stroke-dashoffset: 150;
            transition: 1s ease-in-out;
            z-index: 1;
        }
        .btn:hover {
            transition: 1s ease-in-out;
            background: rgba(255, 255, 255, 0.3);
            border-color: rgba(255, 255, 255, 0.6);
        }
        .btn:hover svg {
            stroke-dashoffset: -480;
        }
        .btn:active {
            transform: scale(0.98);
        }
        .btn:disabled {
            opacity: 0.5;
            cursor: not-allowed;
            background: rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.2);
        }
        .btn svg .bg-line {
            stroke: rgba(255, 255, 255, 0.4);
            stroke-dashoffset: 0;
            transition: none;
        }
        .btn svg .hl-line {
            stroke: white;
        }

        .link-back {
            text-align: center;
            margin-top: 20px;
        }
        .link-back a {
            color: rgba(255, 255, 255, 0.9);
            text-decoration: none;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        .link-back a:hover {
            color: white;
            text-decoration: underline;
        }
        .credentials-info {
            text-align: center;
            color: rgba(255, 255, 255, 0.8);
            font-size: 12px;
            margin-top: 20px;
            padding: 15px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        .credentials-info strong {
            color: white;
            font-weight: 600;
        }
        @media (max-width: 768px) {
            .login-container {
                padding: 30px 20px;
                margin: 15px;
            }
            .title {
                font-size: 24px;
            }
            .logo-container img {
                width: 100px;
                height: 88px;
            }
        }
        @media (max-width: 480px) {
            .login-container {
                padding: 25px 15px;
            }
            .title {
                font-size: 20px;
                margin-bottom: 20px;
            }
            .input-field {
                padding: 12px 14px;
                font-size: 14px;
            }
            .btn {
                width: 100%;
                max-width: 180px;
                padding: 12px;
                font-size: 16px;
            }
            .btn svg {
                width: 100%;
                height: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="animated-bg"></div>
    <div class="login-container">
        <div class="logo-container">
            <img src="${pageContext.request.contextPath}/assets/logolight.png" alt="Tartaruga Cometa">
        </div>
        <h2 class="title">Sistema de Entregas</h2>
        <div id="errorMessage" class="error-message"></div>
        <form id="loginForm">
            <div class="form-group">
                <input
                    type="text"
                    id="username"
                    class="input-field"
                    placeholder="Usuário"
                    autocomplete="username"
                    value="admin"
                    required
                >
            </div>
            <div class="form-group">
                <input
                    type="password"
                    id="password"
                    class="input-field"
                    placeholder="Senha"
                    autocomplete="current-password"
                    value="admin123"
                    required
                >
            </div>
            <div class="center">
                <button type="submit" class="btn" id="loginButton">
                    <span>ENTRAR</span>
                    <svg width="180px" height="60px" viewBox="0 0 180 60" class="border">
                        <polyline points="179,1 179,59 1,59 1,1 179,1" class="bg-line" />
                        <polyline points="179,1 179,59 1,59 1,1 179,1" class="hl-line" />
                    </svg>
                </button>
            </div>
        </form>
        <div class="credentials-info">
            <p style="margin-bottom: 5px;">Credenciais de teste:</p>
            <p>Usuário: <strong>admin</strong> | Senha: <strong>admin123</strong></p>
        </div>
        <div class="link-back">
            <a href="${pageContext.request.contextPath}/">← Voltar para página inicial</a>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>
