package br.com.gymmanager.model;

import static org.junit.Assert.*;

import br.com.gymmanager.excecao.RegraNegocioException;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

/** CT-U20..U27 — regras de estoque do produto (RF003). */
public class ProdutoTest {

    private Produto whey;

    @Before
    public void preparar() {
        whey = new Produto("Whey", CategoriaProduto.SUPLEMENTO, 10, new BigDecimal("129.90"));
    }

    @Test public void entradaSomaAoEstoque() {
        whey.adicionarEstoque(5);
        assertEquals(15, whey.getQuantidadeEstoque());
    }

    @Test public void saidaSubtraiDoEstoque() {
        whey.removerEstoque(4);
        assertEquals(6, whey.getQuantidadeEstoque());
    }

    @Test public void saidaDeTodoOSaldoEPermitida() {
        whey.removerEstoque(10);
        assertEquals(0, whey.getQuantidadeEstoque());
    }

    @Test public void saidaMaiorQueSaldoELancaExcecaoENaoAlteraEstoque() {
        RegraNegocioException e = assertThrows(RegraNegocioException.class, () -> whey.removerEstoque(11));
        assertTrue(e.getMessage().contains("Estoque insuficiente"));
        assertEquals(10, whey.getQuantidadeEstoque());
    }

    @Test public void entradaZeroOuNegativaELancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> whey.adicionarEstoque(0));
        assertThrows(RegraNegocioException.class, () -> whey.adicionarEstoque(-3));
    }

    @Test public void saidaZeroOuNegativaELancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> whey.removerEstoque(0));
        assertThrows(RegraNegocioException.class, () -> whey.removerEstoque(-1));
    }

    @Test public void estaEmFaltaNoLimiteMinimo() {
        assertEquals(5, Produto.ESTOQUE_MINIMO);
        assertTrue(new Produto("A", CategoriaProduto.ROUPAS, 5, BigDecimal.ONE).estaEmFalta());
        assertTrue(new Produto("B", CategoriaProduto.ROUPAS, 0, BigDecimal.ONE).estaEmFalta());
    }

    @Test public void naoEstaEmFaltaAcimaDoLimite() {
        assertFalse(new Produto("C", CategoriaProduto.ROUPAS, 6, BigDecimal.ONE).estaEmFalta());
    }
}
