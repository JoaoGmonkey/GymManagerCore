package br.com.gymmanager.servico;

import static br.com.gymmanager.util.Cenario.*;
import static org.junit.Assert.*;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.main.ContextoAplicacao;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.CategoriaProduto;
import br.com.gymmanager.relatorio.ResultadoRelatorio;
import br.com.gymmanager.util.Cenario;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** CT-S60..S68 — resumo do dashboard e relatórios (RF005). */
public class DashboardERelatorioServiceTest {

    private ContextoAplicacao ctx;

    @Before public void preparar() {
        ctx = Cenario.novo();
        Aluno ana = ctx.alunoService.cadastrar("Ana", CPF_A, "");
        ctx.alunoService.cadastrar("Bruno", CPF_B, "");           // sem pagamento => irregular
        ctx.pagamentoService.registrar(ana.getId(), new BigDecimal("120"), HOJE);
        ctx.produtoService.cadastrar("Whey", CategoriaProduto.SUPLEMENTO, 5, BigDecimal.TEN);
        ctx.produtoService.cadastrar("Creatina", CategoriaProduto.SUPLEMENTO, 20, BigDecimal.TEN);
    }

    @Test public void resumoContaAlunosEProdutosEmFalta() {
        ResumoDashboard r = ctx.dashboardService.resumir();
        assertEquals(2, r.totalAlunos());
        assertEquals(1, r.alunosRegulares());
        assertEquals(1, r.alunosIrregulares());
        assertEquals(1, r.produtosEmFalta());
        assertEquals("Bruno", r.irregulares().get(0).getNome());
    }

    @Test public void resumoSemDadosZeraTudo() {
        ResumoDashboard r = Cenario.novo().dashboardService.resumir();
        assertEquals(0, r.totalAlunos());
        assertEquals(0, r.produtosEmFalta());
    }

    @Test public void catalogoListaOsTresRelatoriosNaOrdem() {
        assertEquals(List.of("Alunos Irregulares", "Pagamentos", "Produtos em falta"),
                ctx.relatorioService.nomesDisponiveis());
    }

    @Test public void relatorioDeIrregularesTrazSomenteIrregulares() {
        ResultadoRelatorio r = ctx.relatorioService.gerar("Alunos Irregulares");
        assertEquals(1, r.linhas().size());
        assertEquals(List.of("Bruno", "Irregular", "19/10/2026"), r.linhas().get(0));
    }

    @Test public void relatorioDePagamentosFormataValorEData() {
        ResultadoRelatorio r = ctx.relatorioService.gerar("Pagamentos");
        assertEquals(List.of("Ana", "R$ 120,00", "19/10/2026"), r.linhas().get(0));
    }

    @Test public void relatorioDeProdutosEmFaltaTrazCategoriaEQuantidade() {
        ResultadoRelatorio r = ctx.relatorioService.gerar("Produtos em falta");
        assertEquals(List.of("Whey", "Suplemento", "5"), r.linhas().get(0));
    }

    @Test public void relatorioInexistenteELancaExcecao() {
        assertThrows(RecursoNaoEncontradoException.class, () -> ctx.relatorioService.gerar("Nada"));
    }
}
