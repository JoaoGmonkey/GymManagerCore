package br.com.gymmanager.servico;

import static org.junit.Assert.assertEquals;

import br.com.gymmanager.model.StatusMatricula;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * CT-U10 — Regra do documento: "matrícula irregular = ausência de pagamento nos últimos 30 dias".
 * Teste parametrizado: cada linha da tabela é um caso (valores-limite incluídos).
 */
@RunWith(Parameterized.class)
public class PoliticaMatriculaTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 19);

    @Parameters(name = "último pagamento há {0} dia(s) => {1}")
    public static Collection<Object[]> casos() {
        return Arrays.asList(new Object[][]{
            {0, StatusMatricula.REGULAR},
            {1, StatusMatricula.REGULAR},
            {29, StatusMatricula.REGULAR},
            {30, StatusMatricula.REGULAR},      // limite: ainda dentro do prazo
            {31, StatusMatricula.IRREGULAR},    // limite: vencido
            {90, StatusMatricula.IRREGULAR},
            {-5, StatusMatricula.REGULAR},      // data adiantada
        });
    }

    private final int diasAtras;
    private final StatusMatricula esperado;

    public PoliticaMatriculaTest(int diasAtras, StatusMatricula esperado) {
        this.diasAtras = diasAtras;
        this.esperado = esperado;
    }

    @Test
    public void calculaSituacaoPeloUltimoPagamento() {
        PoliticaMatricula politica = new PoliticaMatricula();
        assertEquals(esperado, politica.calcular(Optional.of(HOJE.minusDays(diasAtras)), HOJE));
    }
}
