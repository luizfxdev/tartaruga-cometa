<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.tartarugacometasystem.model.Product" %>

<t:header title="Editar Produto">
    <%
        Product product = (Product) request.getAttribute("product");
    %>

    <div class="page-header">
        <h2>Editar Produto</h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/products/save" class="form" onsubmit="return validarFormularioProduto(this)">
        <input type="hidden" name="id" value="<%= product.getId() %>">

        <div class="form-group">
            <label for="name">Nome do Produto *</label>
            <input type="text" id="name" name="name" 
                value="<%= product.getName() %>" required placeholder="Nome do produto">
        </div>

        <div class="form-group">
            <label for="description">Descrição</label>
            <textarea id="description" name="description" placeholder="Descrição do produto" rows="4"><%=
                product.getDescription() != null ? product.getDescription() : ""
            %></textarea>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="category">Categoria</label>
                <input type="text" id="category" name="category" 
                    value="<%= product.getCategory() != null ? product.getCategory() : "" %>" 
                    placeholder="Ex: Eletrônicos">
            </div>

            <div class="form-group">
                <label for="weightKg">Peso (kg) *</label>
                <input type="number" id="weightKg" name="weightKg" 
                    value="<%= product.getWeightKg() %>" required step="0.01" placeholder="0.00">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="volumeM3">Volume (m³) *</label>
                <input type="number" id="volumeM3" name="volumeM3" 
                    value="<%= product.getVolumeM3() %>" required step="0.01" placeholder="0.00">
            </div>

            <div class="form-group">
                <label for="declaredValue">Valor Declarado (R$) *</label>
                <input type="number" id="declaredValue" name="declaredValue" 
                    value="<%= product.getDeclaredValue() %>" required step="0.01" placeholder="0.00">
            </div>
        </div>

        <div class="form-group">
            <label for="active">
                <input type="checkbox" id="active" name="active" value="true" 
                    <%= product.getActive() ? "checked" : "" %>>
                Ativo
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar Alterações</button>
            <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
