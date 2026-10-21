package br.com.gymmanager.servico;

import static org.junit.Assert.*;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.main.ContextoAplicacao;
import br.com.gymmanager.model.CategoriaProduto;
import br.com.gymmanager.model.Produto;
import br.com.gymmanager.util.Cenario;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

/** CT-S30..S39 — produtos e estoque (RF003). */
public class ProdutoServiceTest {

    private ContextoAplicacao ctx;
    private Produto creatina;
    private Produto whey;

    @Before public void preparar() {
        ctx = Cenario.novo();
        creatina = ctx.produtoService.cadastrar("Creatina", CategoriaProduto.SUPLEMENTO, 20, new BigDecimal("89.90"));
        whey = ctx.produtoService.cadastrar("Whey", CategoriaProduto.SUPLEMENTO, 5, new BigDecimal("129.90"));
        ctx.produtoService.cadastrar("Camiseta Oversize", CategoriaProduto.ROUPAS, 19, new BigDecimal("59.90"));
    }

    @Test public void totalDeUnidadesSomaTodoOEstoque() {
        assertEquals(44, ctx.produtoService.totalDeUnidades());
    }

    @Test public void entradaESaidaAtualizamOSaldo() {
        ctx.produtoService.registrarEntrada(creatina.getId(), 10);
        ctx.produtoService.registrarSaida(creatina.getId(), 4);
        assertEquals(26, ctx.produtoService.buscarPorId(creatina.getId()).getQuantidadeEstoque());
        assertEquals(50, ctx.produtoService.totalDeUnidades());
    }

    @Test public void saidaAcimaDoSaldoELancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.registrarSaida(whey.getId(), 6));
        assertEquals(5, whey.getQuantidadeEstoque());
    }

    @Test public void listaSomenteProdutosEmFalta() {
        assertEquals(1, ctx.produtoService.listarEmFalta().size());
        assertEquals("Whey", ctx.produtoService.listarEmFalta().get(0).getNome());
    }

    @Test public void reposicaoTiraProdutoDaListaDeFalta() {
        ctx.produtoService.registrarEntrada(whey.getId(), 1);
        assertTrue(ctx.produtoService.listarEmFalta().isEmpty());
    }

    @Test public void recusaQuantidadeInicialNegativaPrecoNegativoENomeVazio() {
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.cadastrar("X", CategoriaProduto.ACESSORIO, -1, BigDecimal.ONE));
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.cadastrar("X", CategoriaProduto.ACESSORIO, 1, new BigDecimal("-1")));
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.cadastrar(" ", CategoriaProduto.ACESSORIO, 1, BigDecimal.ONE));
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.cadastrar("X", null, 1, BigDecimal.ONE));
    }

    @Test public void buscaPorNomeParcial() {
        assertEquals(1, ctx.produtoService.buscarPorNome("crea").size());
        assertThrows(RegraNegocioException.class, () -> ctx.produtoService.buscarPorNome(""));
    }

    @Test public void atualizaDadosDoProduto() {
        ctx.produtoService.atualizar(whey.getId(), "Whey Protein", CategoriaProduto.SUPLEMENTO, new BigDecimal("139.90"));
        assertEquals("Whey Protein", ctx.produtoService.buscarPorId(whey.getId()).getNome());
    }

    @Test public void excluiProdutoEErroAoBuscarDepois() {
        ctx.produtoService.excluir(whey.getId());
        assertEquals(2, ctx.produtoService.listar().size());
        assertThrows(RecursoNaoEncontradoException.class, () -> ctx.produtoService.buscarPorId(whey.getId()));
    }
}
