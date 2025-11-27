package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
=======
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
>>>>>>> 5969be611d1e7ac7d5a3125a0b921338a13b354d

import com.tartarugacometasystem.model.Product;
import com.tartarugacometasystem.service.ProductService;
import com.tartarugacometasystem.util.Mapper;

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
            if (pathInfo.equals("/save")) {
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
        Optional<Product> productOptional = productService.getProductById(id);
        Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        request.setAttribute("product", product);
        request.getRequestDispatcher("/pages/products/new.jsp").forward(request, response);
    }

    private void viewProduct(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Product> productOptional = productService.getProductById(id);
        Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        request.setAttribute("product", product);
        request.getRequestDispatcher("/pages/products/view.jsp").forward(request, response);
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("q");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products/");
            return;
        }

        List<Product> products = productService.searchProductsByName(searchTerm);
        request.setAttribute("products", products);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/pages/products/list.jsp").forward(request, response);
    }

    private void saveProduct(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", request.getParameter("id"));
            params.put("name", request.getParameter("name"));
            params.put("description", request.getParameter("description"));
            params.put("weightKg", request.getParameter("weightKg"));
            params.put("volumeM3", request.getParameter("volumeM3"));
            params.put("declaredValue", request.getParameter("declaredValue"));
            params.put("category", request.getParameter("category"));
            params.put("active", request.getParameter("active"));

            Product product = Mapper.mapToProduct(params);

            if (product.getId() != null && product.getId() > 0) {
                productService.updateProduct(product);
                request.getSession().setAttribute("success", "Produto atualizado com sucesso");
            } else {
                productService.createProduct(product);
                request.getSession().setAttribute("success", "Produto criado com sucesso");
            }

            response.sendRedirect(request.getContextPath() + "/products/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/products/new");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            productService.deleteProduct(id);
            request.getSession().setAttribute("success", "Produto deletado com sucesso");
            response.sendRedirect(request.getContextPath() + "/products/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/products/");
        }
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
