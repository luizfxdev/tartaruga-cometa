<%@ tag description="Header Tag" pageEncoding="UTF-8" %>
<%@ attribute name="title" required="false" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title != null ? title : 'Tartaruga Cometa'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="navbar-brand">
                <a href="${pageContext.request.contextPath}/">
                    <h1><span class="logo-turtle">🐢</span> Tartaruga Cometa</h1>
                </a>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/clients/">Clientes</a></li>
                <li><a href="${pageContext.request.contextPath}/products/">Produtos</a></li>
                <li><a href="${pageContext.request.contextPath}/addresses/">Endereços</a></li>
                <li><a href="${pageContext.request.contextPath}/deliveries/">Entregas</a></li>
            </ul>
        </div>
    </nav>

    <main class="container">
        <jsp:doBody/>
    </main>
</body>
</html>
