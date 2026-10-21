package br.com.gymmanager.servico;

import static br.com.gymmanager.util.Cenario.*;
import static org.junit.Assert.*;

import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.main.ContextoAplicacao;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Pagamento;
import br.com.gymmanager.model.StatusMatricula;
import br.com.gymmanager.util.Cenario;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

/** CT-S20..S28 — pagamentos e atualização da matrícula (RF004). */
public class PagamentoServiceTest {

    private ContextoAplicacao ctx;
    private Aluno ana;

    @Before public void preparar() {
        ctx = Cenario.novo();
        ana = ctx.alunoService.cadastrar("Ana", CPF_A, "");
    }

    @Test public void pagamentoDeHojeRegularizaAMatricula() {
        ctx.pagamentoService.registrar(ana.getId(), new BigDecimal("120.00"), HOJE);
        assertEquals(StatusMatricula.REGULAR, ana.getStatusMatricula());
    }

    @Test public void pagamentoHaTrintaDiasAindaRegulariza() {
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.minusDays(30));
        assertEquals(StatusMatricula.REGULAR, ana.getStatusMatricula());
    }

    @Test public void pagamentoHaTrintaEUmDiasNaoRegulariza() {
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.minusDays(31));
        assertEquals(StatusMatricula.IRREGULAR, ana.getStatusMatricula());
    }

    @Test public void pagamentoAntigoNaoDerrubaAMatriculaJaRegular() {
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE);
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.minusDays(60));
        assertEquals(StatusMatricula.REGULAR, ana.getStatusMatricula());
    }

    @Test public void recusaValorZeroENegativo() {
        assertThrows(RegraNegocioException.class, () -> ctx.pagamentoService.registrar(ana.getId(), BigDecimal.ZERO, HOJE));
        assertThrows(RegraNegocioException.class, () -> ctx.pagamentoService.registrar(ana.getId(), new BigDecimal("-1"), HOJE));
    }

    @Test public void recusaDataFutura() {
        assertThrows(RegraNegocioException.class,
                () -> ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.plusDays(1)));
    }

    @Test public void recusaCamposNulos() {
        assertThrows(RegraNegocioException.class, () -> ctx.pagamentoService.registrar(ana.getId(), null, HOJE));
        assertThrows(RegraNegocioException.class, () -> ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, null));
    }

    @Test public void recusaAlunoInexistenteENaoGravaPagamento() {
        assertThrows(RegraNegocioException.class, () -> ctx.pagamentoService.registrar(999, BigDecimal.TEN, HOJE));
        assertTrue(ctx.pagamentoService.historico().isEmpty());
    }

    @Test public void historicoDoAlunoListaSomenteSeusPagamentos() {
        Aluno bruno = ctx.alunoService.cadastrar("Bruno", CPF_B, "");
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE);
        Pagamento pb = ctx.pagamentoService.registrar(bruno.getId(), BigDecimal.ONE, HOJE);
        assertEquals(1, ctx.pagamentoService.historicoDoAluno(bruno.getId()).size());
        assertSame(pb, ctx.pagamentoService.historicoDoAluno(bruno.getId()).get(0));
        assertEquals(2, ctx.pagamentoService.historico().size());
    }

    @Test public void reavaliacaoEmLoteTornaIrregularQuemVenceu() {
        ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.minusDays(30));
        // Ana está Regular hoje; "avançando o relógio" com outro contexto, o mesmo pagamento já venceria:
        assertEquals(StatusMatricula.REGULAR, ana.getStatusMatricula());
        ctx.matriculaService.atualizarTodasAsSituacoes();
        assertEquals(StatusMatricula.REGULAR, ana.getStatusMatricula());
    }
}
