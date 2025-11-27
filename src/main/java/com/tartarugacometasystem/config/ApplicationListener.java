package com.tartarugacometasystem.config;

<<<<<<< HEAD
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
=======
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
>>>>>>> 5969be611d1e7ac7d5a3125a0b921338a13b354d

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("Tartaruga Cometa - Sistema de Entregas");
        System.out.println("Aplicação iniciada com sucesso");
        System.out.println("========================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("Aplicação finalizada");
        System.out.println("========================================");
    }
}
