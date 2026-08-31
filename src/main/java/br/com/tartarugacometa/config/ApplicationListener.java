package br.com.tartarugacometa.config;

import java.util.logging.Logger;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ApplicationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Tartaruga Cometa - Aplicação iniciada com sucesso");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConfig.shutdown();
        LOG.info("Tartaruga Cometa - Aplicação finalizada");
    }
}
