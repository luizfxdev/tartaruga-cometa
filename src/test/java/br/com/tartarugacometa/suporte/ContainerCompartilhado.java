package br.com.tartarugacometa.suporte;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.PostgreSQLContainer;

public final class ContainerCompartilhado {
    private static final int PORTA_FIXA = 15433;
    private static PostgreSQLContainer<?> container;
    private static boolean schemaCarregado = false;

    private ContainerCompartilhado() {
    }

    public static synchronized PostgreSQLContainer<?> iniciar() throws Exception {
        if (container == null) {
            container = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("tartaruga_teste")
                .withUsername("tartaruga_user")
                .withPassword("Tartaruga123!")
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                        new PortBinding(Ports.Binding.bindPort(PORTA_FIXA), new ExposedPort(5432))
                    )
                ));
            container.start();
        }
        if (!schemaCarregado) {
            BancoDeTeste.executarScripts(container,
                "00-banco-e-tipos", "01-cliente", "02-endereco", "03-produto",
                "04-entrega", "05-entrega-produto", "06-historico-entrega",
                "07-views", "08-funcoes-triggers", "09-indices");
            schemaCarregado = true;
        }
        return container;
    }
}
