package br.com.gymmanager.relatorio;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;

/** CT-U50..U52 — formatação de valores exibidos nos relatórios. */
public class FormatosTest {

    @Test public void formataMoedaEmReais() {
        assertEquals("R$ 120,00", Formatos.moeda(new BigDecimal("120")));
    }

    @Test public void formataMoedaComMilhar() {
        assertEquals("R$ 1.234,50", Formatos.moeda(new BigDecimal("1234.5")));
    }

    @Test public void formataDataNoPadraoBrasileiro() {
        assertEquals("05/04/2026", Formatos.data(LocalDate.of(2026, 4, 5)));
    }
}
