package br.com.gymmanager.servico;

import static br.com.gymmanager.util.Cenario.*;
import static org.junit.Assert.*;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.main.ContextoAplicacao;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.StatusMatricula;
import br.com.gymmanager.util.Cenario;
import org.junit.Before;
import org.junit.Test;

/** CT-S01..S10 — casos de uso de alunos (RF001), com repositório em memória. */
public class AlunoServiceTest {

    private ContextoAplicacao ctx;

    @Before public void preparar() { ctx = Cenario.novo(); }

    @Test public void cadastraAlunoComCpfNormalizadoEDataDeHoje() {
        Aluno a = ctx.alunoService.cadastrar("  Ana Souza ", CPF_A, "(11) 90000-0001");
        assertTrue(a.getId() > 0);
        assertEquals("Ana Souza", a.getNome());
        assertEquals("12345678909", a.getCpf());
        assertEquals(HOJE, a.getDataIngresso());
    }

    @Test public void alunoNovoSemPagamentoNasceIrregular() {
        assertEquals(StatusMatricula.IRREGULAR, ctx.alunoService.cadastrar("Ana", CPF_A, "").getStatusMatricula());
    }

    @Test public void recusaCpfInvalido() {
        RegraNegocioException e = assertThrows(RegraNegocioException.class,
                () -> ctx.alunoService.cadastrar("Ana", "123.456.789-10", ""));
        assertEquals("CPF inválido.", e.getMessage());
    }

    @Test public void recusaCpfDuplicadoMesmoComMascaraDiferente() {
        ctx.alunoService.cadastrar("Ana", CPF_A, "");
        assertThrows(RegraNegocioException.class, () -> ctx.alunoService.cadastrar("Outra", "12345678909", ""));
    }

    @Test public void recusaNomeVazio() {
        assertThrows(RegraNegocioException.class, () -> ctx.alunoService.cadastrar("   ", CPF_A, ""));
        assertThrows(RegraNegocioException.class, () -> ctx.alunoService.cadastrar(null, CPF_A, ""));
    }

    @Test public void buscaPorNomeIgnoraMaiusculasEAceitaTrecho() {
        ctx.alunoService.cadastrar("Ana Souza", CPF_A, "");
        ctx.alunoService.cadastrar("Bruno Lima", CPF_B, "");
        assertEquals(1, ctx.alunoService.buscarPorNome("SOUZA").size());
        assertEquals(2, ctx.alunoService.buscarPorNome("a").size());
        assertTrue(ctx.alunoService.buscarPorNome("zzz").isEmpty());
    }

    @Test public void buscaComTextoVazioELancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> ctx.alunoService.buscarPorNome(" "));
    }

    @Test public void atualizaDadosMantendoOMesmoCpf() {
        Aluno a = ctx.alunoService.cadastrar("Ana", CPF_A, "");
        ctx.alunoService.atualizar(a.getId(), "Ana Lima", CPF_A, "123");
        assertEquals("Ana Lima", ctx.alunoService.buscarPorId(a.getId()).getNome());
    }

    @Test public void naoPermiteAtualizarParaCpfDeOutroAluno() {
        Aluno a = ctx.alunoService.cadastrar("Ana", CPF_A, "");
        ctx.alunoService.cadastrar("Bruno", CPF_B, "");
        assertThrows(RegraNegocioException.class, () -> ctx.alunoService.atualizar(a.getId(), "Ana", CPF_B, ""));
    }

    @Test public void excluiAlunoEErroAoBuscarDepois() {
        Aluno a = ctx.alunoService.cadastrar("Ana", CPF_A, "");
        ctx.alunoService.excluir(a.getId());
        assertTrue(ctx.alunoService.listar().isEmpty());
        assertThrows(RecursoNaoEncontradoException.class, () -> ctx.alunoService.buscarPorId(a.getId()));
    }
}
