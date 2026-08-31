package br.com.tartarugacometa.seguranca;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SegurancaTest {

    private static final Path SRC_MAIN_JAVA = Path.of("src/main/java");
    private static final Path SRC_MAIN_WEBAPP = Path.of("src/main/webapp");

    @Test
    void nenhumDaoUsaStatementSemParametrizacao() throws IOException {
        List<Path> arquivosComCreateStatement = arquivosJavaContendo("createStatement()");

        assertThat(arquivosComCreateStatement).isEmpty();
    }

    @Test
    void nenhumSqlConcatenaValorDeUsuarioDiretamente() throws IOException {
        Pattern concatenacaoSuspeita = Pattern.compile(
            "\"\\s*\\+\\s*\\w+\\s*\\+\\s*\"[^\"]*(WHERE|SELECT|INSERT|UPDATE|DELETE)",
            Pattern.CASE_INSENSITIVE
        );

        try (Stream<Path> arquivos = Files.walk(SRC_MAIN_JAVA)) {
            List<Path> suspeitos = arquivos
                .filter(p -> p.toString().endsWith("DAO.java"))
                .filter(p -> {
                    try {
                        String conteudo = Files.readString(p);
                        return concatenacaoSuspeita.matcher(conteudo).find();
                    } catch (IOException e) {
                        return false;
                    }
                })
                .toList();

            assertThat(suspeitos).isEmpty();
        }
    }

    @Test
    void nenhumJspUsaScriptletDeSaidaBruta() throws IOException {
        List<Path> arquivosComScriptletOutput = arquivosWebappContendo("<%= ");

        assertThat(arquivosComScriptletOutput).isEmpty();
    }

    @Test
    void webXmlDefineCookieDeSessaoSeguro() throws IOException {
        Path webXml = SRC_MAIN_WEBAPP.resolve("WEB-INF/web.xml");
        String conteudo = Files.readString(webXml);

        assertThat(conteudo).contains("<http-only>true</http-only>");
    }

    @Test
    void webXmlDefineSessionConfig() throws IOException {
        Path webXml = SRC_MAIN_WEBAPP.resolve("WEB-INF/web.xml");
        String conteudo = Files.readString(webXml);

        assertThat(conteudo).contains("<session-config>");
    }

    private List<Path> arquivosJavaContendo(String trecho) throws IOException {
        try (Stream<Path> arquivos = Files.walk(SRC_MAIN_JAVA)) {
            return arquivos
                .filter(p -> p.toString().endsWith(".java"))
                .filter(p -> contemTrecho(p, trecho))
                .toList();
        }
    }

    private List<Path> arquivosWebappContendo(String trecho) throws IOException {
        try (Stream<Path> arquivos = Files.walk(SRC_MAIN_WEBAPP)) {
            return arquivos
                .filter(p -> p.toString().endsWith(".jsp"))
                .filter(p -> contemTrecho(p, trecho))
                .toList();
        }
    }

    private boolean contemTrecho(Path arquivo, String trecho) {
        try {
            return Files.readString(arquivo).contains(trecho);
        } catch (IOException e) {
            return false;
        }
    }
}
