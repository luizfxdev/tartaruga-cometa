package br.com.tartarugacometa.interfaceweb;

import br.com.tartarugacometa.filter.EncodingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterfaceTest {

    private static final Path SRC_MAIN_WEBAPP = Path.of("src/main/webapp");
    private static final Path SRC_MAIN_JAVA = Path.of("src/main/java");

    private static final String[] SEQUENCIAS_MOJIBAKE = {
        "Ã©", "Ã§", "Ã¡", "Ã³",
        "Ãº", "Ã­", "Â "
    };

    @Mock
    private FilterConfig filterConfig;

    @Mock
    private ServletRequest request;

    @Mock
    private ServletResponse response;

    @Mock
    private FilterChain chain;

    @Test
    void encodingFilterForcaUtf8NaRequisicaoEResposta() throws Exception {
        when(filterConfig.getInitParameter("encoding")).thenReturn("UTF-8");

        EncodingFilter filtro = new EncodingFilter();
        filtro.init(filterConfig);
        filtro.doFilter(request, response, chain);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
        verify(chain).doFilter(request, response);
    }

    @Test
    void encodingFilterUsaUtf8ComoPadraoSemParametro() throws Exception {
        when(filterConfig.getInitParameter("encoding")).thenReturn(null);

        EncodingFilter filtro = new EncodingFilter();
        filtro.init(filterConfig);
        filtro.doFilter(request, response, chain);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    void todoJspDeclaraContentTypeOuPageEncodingUtf8() throws IOException {
        try (Stream<Path> arquivos = Files.walk(SRC_MAIN_WEBAPP)) {
            List<Path> semDeclaracao = arquivos
                .filter(p -> p.toString().endsWith(".jsp"))
                .filter(p -> {
                    try {
                        String conteudo = Files.readString(p);
                        return !conteudo.contains("charset=UTF-8") && !conteudo.contains("pageEncoding=\"UTF-8\"");
                    } catch (IOException e) {
                        return true;
                    }
                })
                .toList();

            assertThat(semDeclaracao).isEmpty();
        }
    }

    @Test
    void nenhumJspOuJavaContemMojibake() throws IOException {
        List<Path> comMojibake = new ArrayList<>();
        comMojibake.addAll(arquivosComMojibake(SRC_MAIN_WEBAPP, ".jsp"));
        comMojibake.addAll(arquivosComMojibake(SRC_MAIN_JAVA, ".java"));

        assertThat(comMojibake).isEmpty();
    }

    private List<Path> arquivosComMojibake(Path raiz, String extensao) throws IOException {
        try (Stream<Path> arquivos = Files.walk(raiz)) {
            return arquivos
                .filter(p -> p.toString().endsWith(extensao))
                .filter(this::contemMojibake)
                .toList();
        }
    }

    private boolean contemMojibake(Path arquivo) {
        try {
            String conteudo = Files.readString(arquivo);
            for (String sequencia : SEQUENCIAS_MOJIBAKE) {
                if (conteudo.contains(sequencia)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
