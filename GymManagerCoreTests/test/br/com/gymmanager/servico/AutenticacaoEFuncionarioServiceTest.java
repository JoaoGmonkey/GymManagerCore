package br.com.gymmanager.servico;

import static org.junit.Assert.*;

import br.com.gymmanager.excecao.AcessoNegadoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.main.ContextoAplicacao;
import br.com.gymmanager.model.Cargo;
import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.model.Permissao;
import br.com.gymmanager.util.Cenario;
import org.junit.Before;
import org.junit.Test;

/** CT-S40..S52 — funcionários, autenticação (RF002, RNF004) e controle de acesso (RNF001). */
public class AutenticacaoEFuncionarioServiceTest {

    private ContextoAplicacao ctx;
    private Funcionario joao;

    @Before public void preparar() {
        ctx = Cenario.novo();
        joao = ctx.funcionarioService.cadastrar("João", Cargo.ADMINISTRADOR_GERAL, "joao", "senha123");
    }

    @Test public void senhaFicaArmazenadaComoHash() {
        assertNotEquals("senha123", joao.getSenhaHash());
        assertFalse(joao.getSenhaHash().contains("senha123"));
    }

    @Test public void loginComCredenciaisCorretas() {
        assertSame(joao, ctx.autenticacaoService.autenticar("joao", "senha123"));
    }

    @Test public void loginIgnoraEspacosNoUsuario() {
        assertSame(joao, ctx.autenticacaoService.autenticar("  joao ", "senha123"));
    }

    @Test public void senhaErradaEUsuarioInexistenteDaoAMesmaMensagem() {
        RegraNegocioException e1 = assertThrows(RegraNegocioException.class, () -> ctx.autenticacaoService.autenticar("joao", "x"));
        RegraNegocioException e2 = assertThrows(RegraNegocioException.class, () -> ctx.autenticacaoService.autenticar("ninguem", "senha123"));
        assertEquals(e1.getMessage(), e2.getMessage());
    }

    @Test public void loginComCamposNulosELancaExcecao() {
        assertThrows(RegraNegocioException.class, () -> ctx.autenticacaoService.autenticar(null, "x"));
        assertThrows(RegraNegocioException.class, () -> ctx.autenticacaoService.autenticar("joao", null));
    }

    @Test public void recusaUsuarioDuplicadoSemDiferenciarMaiusculas() {
        assertThrows(RegraNegocioException.class,
                () -> ctx.funcionarioService.cadastrar("Outro", Cargo.INSTRUTOR, "JOAO", "abcdef"));
    }

    @Test public void recusaSenhaCurta() {
        assertThrows(RegraNegocioException.class,
                () -> ctx.funcionarioService.cadastrar("Novo", Cargo.INSTRUTOR, "novo", "12345"));
    }

    @Test public void recusaCargoNuloENomeVazio() {
        assertThrows(RegraNegocioException.class, () -> ctx.funcionarioService.cadastrar("N", null, "n", "abcdef"));
        assertThrows(RegraNegocioException.class, () -> ctx.funcionarioService.cadastrar("", Cargo.INSTRUTOR, "n", "abcdef"));
    }

    @Test public void atualizaNomeECargo() {
        ctx.funcionarioService.atualizar(joao.getId(), "João G.", Cargo.FINANCEIRO);
        assertEquals(Cargo.FINANCEIRO, ctx.funcionarioService.buscarPorId(joao.getId()).getCargo());
    }

    @Test public void buscaPorNomeEExclusao() {
        assertEquals(1, ctx.funcionarioService.buscarPorNome("JOÃ").size());
        ctx.funcionarioService.excluir(joao.getId());
        assertTrue(ctx.funcionarioService.listar().isEmpty());
    }

    @Test public void controleDeAcessoPermiteEBloqueiaConformeOCargo() {
        Funcionario instrutor = ctx.funcionarioService.cadastrar("Gus", Cargo.INSTRUTOR, "gus", "treino1");
        assertTrue(ctx.controleAcesso.pode(joao, Permissao.FUNCIONARIOS_GERENCIAR));
        assertFalse(ctx.controleAcesso.pode(instrutor, Permissao.ALUNOS_EDITAR));
        assertFalse(ctx.controleAcesso.pode(null, Permissao.ALUNOS_VISUALIZAR));
        assertThrows(AcessoNegadoException.class, () -> ctx.controleAcesso.exigir(instrutor, Permissao.PAGAMENTOS_GERENCIAR));
        ctx.controleAcesso.exigir(joao, Permissao.PAGAMENTOS_GERENCIAR); // não lança
    }
}
