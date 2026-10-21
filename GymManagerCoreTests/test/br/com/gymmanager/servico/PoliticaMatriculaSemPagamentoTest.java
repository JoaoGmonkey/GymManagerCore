package br.com.gymmanager.servico;

import static org.junit.Assert.assertEquals;

import br.com.gymmanager.model.StatusMatricula;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Test;

/** CT-U11 — aluno que nunca pagou é irregular. */
public class PoliticaMatriculaSemPagamentoTest {

    @Test
    public void semPagamentoNenhumEIrregular() {
        assertEquals(StatusMatricula.IRREGULAR,
                new PoliticaMatricula().calcular(Optional.empty(), LocalDate.of(2026, 10, 19)));
    }

    @Test
    public void toleranciaConfiguradaEmTrintaDias() {
        assertEquals(30, PoliticaMatricula.DIAS_DE_TOLERANCIA);
    }
}
