package br.com.gymmanager.relatorio;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

/** CT-U53..U55 — formatação tabular dos relatórios (RF005). */
public class FormatadorTextoTabelaTest {

    private final FormatadorTextoTabela formatador = new FormatadorTextoTabela();

    @Test public void cabecalhoSeparadorELinhasNaOrdem() {
        ResultadoRelatorio r = new ResultadoRelatorio("Produtos", List.of("Produto", "QTD"),
                List.of(List.of("Whey", "5"), List.of("Pré-treino", "3")));
        String[] linhas = formatador.formatar(r).split("\n");
        assertEquals(4, linhas.length);
        assertTrue(linhas[0].startsWith("Produto"));
        assertTrue(linhas[0].endsWith("QTD"));
        assertTrue(linhas[1].matches("-+"));
        assertTrue(linhas[2].startsWith("Whey"));
        assertTrue(linhas[3].startsWith("Pré-treino"));
    }

    @Test public void colunasFicamAlinhadasPelaMaiorCelula() {
        ResultadoRelatorio r = new ResultadoRelatorio("T", List.of("A", "B"),
                List.of(List.of("longo-valor", "x"), List.of("c", "y")));
        String[] linhas = formatador.formatar(r).split("\n");
        assertEquals(linhas[2].indexOf('x'), linhas[3].indexOf('y'));
    }

    @Test public void semLinhasMostraSoCabecalho() {
        ResultadoRelatorio r = new ResultadoRelatorio("Vazio", List.of("Nome", "Situação"), List.of());
        assertEquals(2, formatador.formatar(r).split("\n").length);
    }
}
