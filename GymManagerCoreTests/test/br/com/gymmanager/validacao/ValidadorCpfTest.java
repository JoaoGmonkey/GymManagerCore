package br.com.gymmanager.validacao;

import static org.junit.Assert.*;

import org.junit.Test;

/** CT-U01..U07 — validação de CPF (RF001 / observação técnica da etapa 1). */
public class ValidadorCpfTest {

    @Test public void aceitaCpfValidoComMascara() { assertTrue(ValidadorCpf.ehValido("123.456.789-09")); }

    @Test public void aceitaCpfValidoSemMascara() { assertTrue(ValidadorCpf.ehValido("12345678909")); }

    @Test public void aceitaCpfComDigitoVerificadorZero() { assertTrue(ValidadorCpf.ehValido("987.654.321-00")); }

    @Test public void rejeitaDigitoVerificadorErrado() { assertFalse(ValidadorCpf.ehValido("123.456.789-10")); }

    @Test public void rejeitaTodosOsDigitosIguais() { assertFalse(ValidadorCpf.ehValido("111.111.111-11")); }

    @Test public void rejeitaTamanhoIncorreto() {
        assertFalse(ValidadorCpf.ehValido("12344"));
        assertFalse(ValidadorCpf.ehValido("123456789012"));
    }

    @Test public void rejeitaNuloVazioELetras() {
        assertFalse(ValidadorCpf.ehValido(null));
        assertFalse(ValidadorCpf.ehValido(""));
        assertFalse(ValidadorCpf.ehValido("abc.def.ghi-jk"));
    }

    @Test public void normalizarRemoveTudoQueNaoForDigito() {
        assertEquals("12345678909", ValidadorCpf.normalizar(" 123.456.789-09 "));
        assertEquals("", ValidadorCpf.normalizar(null));
    }
}
