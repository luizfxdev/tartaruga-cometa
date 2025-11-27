package com.tartarugacometasystem.filter;

import java.io.IOException;

<<<<<<< HEAD
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
=======
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
>>>>>>> 5969be611d1e7ac7d5a3125a0b921338a13b354d

@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    private String encoding;

    @Override
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nenhuma ação necessária
    }
}
