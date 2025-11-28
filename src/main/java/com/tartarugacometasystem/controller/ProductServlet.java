package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap; // Importar Mapper
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.model.Product;
import com.tartarugacometasystem.service.ProductService;
import com.tartarugacometasystem.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listProducts(request, response);
            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                showEditForm(request, response, pathInfo);
            } else if (pathInfo.startsWith("/view/")) {
                viewProduct(request, response, pathInfo);
            } else if (pathInfo.startsWith("/search")) {
                searchProducts(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/save")) {
                saveProduct(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteProduct(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/pages/products/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/products/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            request.setAttribute("product", product.get());
            request.getRequestDispatcher("/pages/products/new.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void viewProduct(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            request.setAttribute("product", product.get());
            request.getRequestDispatcher("/pages/products/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void saveProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        Product product = Mapper.mapToProduct(params); // Usar Mapper

        try {
            if (product.getId() == null) {
                productService.createProduct(product);
                request.getSession().setAttribute("success", "Produto criado com sucesso!");
            } else {
                productService.updateProduct(product);
                request.getSession().setAttribute("success", "Produto atualizado com sucesso!");
            }
            response.sendRedirect(request.getContextPath() + "/products/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            request.setAttribute("product", product); // Mantém os dados preenchidos
            request.getRequestDispatcher("/pages/products/new.jsp").forward(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        productService.deleteProduct(id);
        request.getSession().setAttribute("success", "Produto deletado com sucesso!");
        response.sendRedirect(request.getContextPath() + "/products/");
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("query");
        List<Product> products = productService.searchProductsByName(searchTerm);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/pages/products/list.jsp").forward(request, response);
    }

    // Método auxiliar para construir um objeto Product a partir dos parâmetros da requisição (usando Mapper)
    private Product buildProductFromRequest(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return Mapper.mapToProduct(params);
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
