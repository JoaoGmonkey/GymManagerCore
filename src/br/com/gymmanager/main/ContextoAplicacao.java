package br.com.gymmanager.main;

import br.com.gymmanager.relatorio.*;
import br.com.gymmanager.repositorio.memoria.*;
import br.com.gymmanager.seguranca.HashSenha;
import br.com.gymmanager.seguranca.HashSenhaPbkdf2;
import br.com.gymmanager.servico.*;
import java.time.Clock;
import java.util.List;

/**
 * Raiz de composição: único lugar que conhece as classes concretas e monta o
 * grafo de objetos (injeção de dependência manual). Na fase web, este papel
 * pode ser trocado por um framework de DI sem tocar nos serviços.
 */
public class ContextoAplicacao {

    public final AlunoRepositorioMemoria alunoRepo = new AlunoRepositorioMemoria();
    public final FuncionarioRepositorioMemoria funcionarioRepo = new FuncionarioRepositorioMemoria();
    public final ProdutoRepositorioMemoria produtoRepo = new ProdutoRepositorioMemoria();
    public final PagamentoRepositorioMemoria pagamentoRepo = new PagamentoRepositorioMemoria();

    public final HashSenha hashSenha = new HashSenhaPbkdf2();
    public final MatriculaService matriculaService;
    public final AlunoService alunoService;
    public final PagamentoService pagamentoService;
    public final ProdutoService produtoService;
    public final FuncionarioService funcionarioService;
    public final AutenticacaoService autenticacaoService;
    public final DashboardService dashboardService;
    public final RelatorioService relatorioService;
    public final ControleAcesso controleAcesso = new ControleAcesso();

    public ContextoAplicacao(Clock relogio) {
        matriculaService = new MatriculaService(alunoRepo, pagamentoRepo, new PoliticaMatricula(), relogio);
        alunoService = new AlunoService(alunoRepo, matriculaService, relogio);
        pagamentoService = new PagamentoService(pagamentoRepo, alunoService, matriculaService, relogio);
        produtoService = new ProdutoService(produtoRepo);
        funcionarioService = new FuncionarioService(funcionarioRepo, hashSenha);
        autenticacaoService = new AutenticacaoService(funcionarioRepo, hashSenha);
        dashboardService = new DashboardService(alunoRepo, produtoRepo);
        relatorioService = new RelatorioService(List.of(
                new RelatorioAlunosIrregulares(alunoRepo),
                new RelatorioPagamentos(pagamentoRepo),
                new RelatorioProdutosEmFalta(produtoRepo)));
    }
}
