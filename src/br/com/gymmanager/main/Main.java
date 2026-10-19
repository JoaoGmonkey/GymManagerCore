package br.com.gymmanager.main;

import br.com.gymmanager.excecao.AcessoNegadoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.*;
import br.com.gymmanager.navegacao.ItemMenu;
import br.com.gymmanager.relatorio.FormatadorTextoTabela;
import br.com.gymmanager.relatorio.ResultadoRelatorio;
import br.com.gymmanager.servico.ResumoDashboard;
import br.com.gymmanager.validacao.ValidadorCpf;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Ponto de entrada do projeto refatorado. Em vez de abrir uma tela Swing,
 * executa uma bateria de testes sobre as classes de negócio, provando que elas
 * funcionam sem qualquer interface gráfica (pré-requisito para a fase web).
 *
 * O relógio é fixo (19/10/2026) para que os testes da regra dos 30 dias sejam determinísticos.
 */
public class Main {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 19);
    private static final String CPF_1 = "123.456.789-09";
    private static final String CPF_2 = "987.654.321-00";
    private static final String CPF_3 = "246.813.579-28";

    public static void main(String[] args) {
        Clock relogio = Clock.fixed(Instant.parse("2026-10-19T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
        ContextoAplicacao ctx = new ContextoAplicacao(relogio);
        Verificador t = new Verificador();

        testarCpf(t);
        testarFuncionariosEAutenticacao(t, ctx);
        testarPermissoesEMenu(t, ctx);
        testarAlunos(t, ctx);
        testarPagamentosEMatricula(t, ctx);
        testarEstoque(t, ctx);
        testarDashboardERelatorios(t, ctx);

        t.resumo();
        if (!t.passou()) {
            System.exit(1);
        }
    }

    private static void testarCpf(Verificador t) {
        t.secao("Validação de CPF (RN do documento de projeto)");
        t.verdadeiro("CPF válido com máscara é aceito", ValidadorCpf.ehValido(CPF_1));
        t.verdadeiro("CPF com dígito verificador errado é rejeitado", !ValidadorCpf.ehValido("123.456.789-00"));
        t.verdadeiro("CPF com todos os dígitos iguais é rejeitado", !ValidadorCpf.ehValido("111.111.111-11"));
        t.verdadeiro("CPF com tamanho incorreto é rejeitado", !ValidadorCpf.ehValido("12344"));
        t.igual("Normalização remove pontuação", "12345678909", ValidadorCpf.normalizar(CPF_1));
    }

    private static void testarFuncionariosEAutenticacao(Verificador t, ContextoAplicacao ctx) {
        t.secao("Funcionários e autenticação (RF002, RNF004)");
        Funcionario admin = ctx.funcionarioService.cadastrar("João", Cargo.ADMINISTRADOR_GERAL, "joao", "senha123");
        ctx.funcionarioService.cadastrar("Gustavo", Cargo.INSTRUTOR, "gustavo", "treino456");
        ctx.funcionarioService.cadastrar("Carla", Cargo.FINANCEIRO, "carla", "caixa789");
        ctx.funcionarioService.cadastrar("Edu", Cargo.ESTOQUISTA, "edu", "estoque1");
        ctx.funcionarioService.cadastrar("Rita", Cargo.RECEPCIONISTA, "rita", "recepcao1");

        t.verdadeiro("Senha é armazenada como hash, nunca em texto puro", !admin.getSenhaHash().contains("senha123"));
        t.igual("Login correto retorna o funcionário", "João", ctx.autenticacaoService.autenticar("joao", "senha123").getNome());
        t.lanca("Senha errada é recusada", RegraNegocioException.class,
                () -> ctx.autenticacaoService.autenticar("joao", "errada"));
        t.lanca("Usuário inexistente é recusado", RegraNegocioException.class,
                () -> ctx.autenticacaoService.autenticar("fantasma", "qualquer1"));
        t.lanca("Usuário duplicado é recusado", RegraNegocioException.class,
                () -> ctx.funcionarioService.cadastrar("Outro João", Cargo.INSTRUTOR, "JOAO", "abcdef"));
        t.lanca("Senha curta é recusada", RegraNegocioException.class,
                () -> ctx.funcionarioService.cadastrar("Novo", Cargo.INSTRUTOR, "novo", "123"));
        t.igual("Busca de funcionário por nome", 1, ctx.funcionarioService.buscarPorNome("gust").size());
    }

    private static void testarPermissoesEMenu(Verificador t, ContextoAplicacao ctx) {
        t.secao("Permissões por cargo e menu (RNF001)");
        Funcionario admin = ctx.funcionarioRepo.buscarPorUsuario("joao").orElseThrow();
        Funcionario instrutor = ctx.funcionarioRepo.buscarPorUsuario("gustavo").orElseThrow();
        Funcionario estoquista = ctx.funcionarioRepo.buscarPorUsuario("edu").orElseThrow();

        t.verdadeiro("Administrador pode gerenciar funcionários", ctx.controleAcesso.pode(admin, Permissao.FUNCIONARIOS_GERENCIAR));
        t.verdadeiro("Instrutor NÃO pode editar alunos", !ctx.controleAcesso.pode(instrutor, Permissao.ALUNOS_EDITAR));
        t.lanca("exigir() bloqueia instrutor em pagamentos", AcessoNegadoException.class,
                () -> ctx.controleAcesso.exigir(instrutor, Permissao.PAGAMENTOS_GERENCIAR));
        t.verdadeiro("Estoquista gerencia estoque", ctx.controleAcesso.pode(estoquista, Permissao.ESTOQUE_GERENCIAR));
        t.igual("Administrador vê todos os itens do menu", ItemMenu.values().length, ItemMenu.visiveisPara(Cargo.ADMINISTRADOR_GERAL).size());
        t.verdadeiro("Instrutor não vê Pagamentos nem Funcionários no menu",
                !ItemMenu.visiveisPara(Cargo.INSTRUTOR).contains(ItemMenu.PAGAMENTOS)
                        && !ItemMenu.visiveisPara(Cargo.INSTRUTOR).contains(ItemMenu.FUNCIONARIOS));
    }

    private static void testarAlunos(Verificador t, ContextoAplicacao ctx) {
        t.secao("Cadastro de alunos (RF001)");
        Aluno ana = ctx.alunoService.cadastrar("Ana Souza", CPF_1, "(11) 90000-0001");
        t.igual("CPF é salvo normalizado", "12345678909", ana.getCpf());
        t.igual("Data de ingresso = data do cadastro", HOJE, ana.getDataIngresso());
        t.igual("Aluno novo sem pagamento nasce Irregular", StatusMatricula.IRREGULAR, ana.getStatusMatricula());
        t.lanca("CPF duplicado é recusado", RegraNegocioException.class,
                () -> ctx.alunoService.cadastrar("Outra Ana", "123.456.789-09", ""));
        t.lanca("CPF inválido é recusado", RegraNegocioException.class,
                () -> ctx.alunoService.cadastrar("Fulano", "000.000.000-01", ""));
        t.lanca("Nome vazio é recusado", RegraNegocioException.class,
                () -> ctx.alunoService.cadastrar("  ", CPF_2, ""));

        ctx.alunoService.cadastrar("Bruno Lima", CPF_2, "");
        ctx.alunoService.cadastrar("Anderson Reis", CPF_3, "");
        t.igual("Busca parcial por nome 'an' (case-insensitive)", 2, ctx.alunoService.buscarPorNome("AN").size());
        t.lanca("Busca com texto vazio é recusada", RegraNegocioException.class, () -> ctx.alunoService.buscarPorNome(" "));

        ctx.alunoService.atualizar(ana.getId(), "Ana Souza Lima", CPF_1, "(11) 98888-7777");
        t.igual("Edição altera o nome", "Ana Souza Lima", ctx.alunoService.buscarPorId(ana.getId()).getNome());
        t.lanca("Editar para CPF de outro aluno é recusado", RegraNegocioException.class,
                () -> ctx.alunoService.atualizar(ana.getId(), "Ana", CPF_2, ""));

        int idAnderson = ctx.alunoRepo.buscarPorCpf("24681357928").orElseThrow().getId();
        ctx.alunoService.excluir(idAnderson);
        t.igual("Exclusão remove o aluno", 2, ctx.alunoService.listar().size());
        t.lanca("Excluir aluno inexistente falha", RegraNegocioException.class, () -> ctx.alunoService.excluir(idAnderson));
    }

    private static void testarPagamentosEMatricula(Verificador t, ContextoAplicacao ctx) {
        t.secao("Pagamentos e situação da matrícula (RF004)");
        Aluno ana = ctx.alunoRepo.buscarPorCpf("12345678909").orElseThrow();
        Aluno bruno = ctx.alunoRepo.buscarPorCpf("98765432100").orElseThrow();

        ctx.pagamentoService.registrar(ana.getId(), new BigDecimal("120.00"), HOJE);
        t.igual("Pagamento de hoje torna a matrícula Regular", StatusMatricula.REGULAR, ana.getStatusMatricula());

        ctx.pagamentoService.registrar(bruno.getId(), new BigDecimal("120.00"), HOJE.minusDays(30));
        t.igual("Pagamento há exatamente 30 dias ainda é Regular", StatusMatricula.REGULAR, bruno.getStatusMatricula());

        ctx.pagamentoService.registrar(bruno.getId(), new BigDecimal("100.00"), HOJE.minusDays(31));
        t.igual("Último pagamento continua o de 30 dias (31 dias não o substitui)", HOJE.minusDays(30),
                ctx.pagamentoRepo.dataUltimoPagamento(bruno.getId()).orElseThrow());

        t.lanca("Valor zero é recusado", RegraNegocioException.class,
                () -> ctx.pagamentoService.registrar(ana.getId(), BigDecimal.ZERO, HOJE));
        t.lanca("Valor negativo é recusado", RegraNegocioException.class,
                () -> ctx.pagamentoService.registrar(ana.getId(), new BigDecimal("-5"), HOJE));
        t.lanca("Data futura é recusada", RegraNegocioException.class,
                () -> ctx.pagamentoService.registrar(ana.getId(), BigDecimal.TEN, HOJE.plusDays(1)));
        t.lanca("Aluno inexistente é recusado", RegraNegocioException.class,
                () -> ctx.pagamentoService.registrar(999, BigDecimal.TEN, HOJE));

        // O tempo passa: 2 dias depois, o pagamento de 30 dias atrás vence (31 dias) => Irregular
        Clock doisDiasDepois = Clock.fixed(Instant.parse("2026-10-21T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
        ContextoAplicacao futuro = new ContextoAplicacao(doisDiasDepois);
        Aluno copia = futuro.alunoService.cadastrar("Carlos", CPF_3, "");
        futuro.pagamentoService.registrar(copia.getId(), BigDecimal.TEN, LocalDate.of(2026, 9, 20));
        t.igual("Pagamento de 31 dias atrás => Irregular", StatusMatricula.IRREGULAR, copia.getStatusMatricula());
        futuro.pagamentoService.registrar(copia.getId(), BigDecimal.TEN, LocalDate.of(2026, 9, 21));
        t.igual("Pagamento de 30 dias atrás => Regular", StatusMatricula.REGULAR, copia.getStatusMatricula());

        ctx.matriculaService.atualizarTodasAsSituacoes();
        t.igual("Reavaliação em lote mantém Ana Regular", StatusMatricula.REGULAR, ana.getStatusMatricula());
        t.igual("Histórico geral tem os 3 pagamentos lançados", 3, ctx.pagamentoService.historico().size());
        t.igual("Histórico do aluno", 1, ctx.pagamentoService.historicoDoAluno(ana.getId()).size());
    }

    private static void testarEstoque(Verificador t, ContextoAplicacao ctx) {
        t.secao("Estoque (RF003)");
        Produto creatina = ctx.produtoService.cadastrar("Creatina", CategoriaProduto.SUPLEMENTO, 20, new BigDecimal("89.90"));
        Produto whey = ctx.produtoService.cadastrar("Whey", CategoriaProduto.SUPLEMENTO, 5, new BigDecimal("129.90"));
        ctx.produtoService.cadastrar("Camiseta Oversize", CategoriaProduto.ROUPAS, 19, new BigDecimal("59.90"));

        t.igual("Total de unidades (20+5+19)", 44, ctx.produtoService.totalDeUnidades());
        ctx.produtoService.registrarEntrada(creatina.getId(), 3);
        t.igual("Entrada soma ao estoque", 23, creatina.getQuantidadeEstoque());
        ctx.produtoService.registrarSaida(creatina.getId(), 3);
        t.igual("Saída subtrai do estoque", 20, creatina.getQuantidadeEstoque());
        t.lanca("Saída maior que o saldo é recusada", RegraNegocioException.class,
                () -> ctx.produtoService.registrarSaida(whey.getId(), 6));
        t.lanca("Entrada negativa é recusada", RegraNegocioException.class,
                () -> ctx.produtoService.registrarEntrada(whey.getId(), -1));
        t.lanca("Quantidade inicial negativa é recusada", RegraNegocioException.class,
                () -> ctx.produtoService.cadastrar("X", CategoriaProduto.ACESSORIO, -1, BigDecimal.ONE));
        t.igual("Whey (5 un.) está em falta", true, whey.estaEmFalta());
        t.igual("Creatina (20 un.) não está em falta", false, creatina.estaEmFalta());
        t.igual("Busca de produto por nome", 1, ctx.produtoService.buscarPorNome("whe").size());
        ctx.produtoService.atualizar(whey.getId(), "Whey Protein", CategoriaProduto.SUPLEMENTO, new BigDecimal("139.90"));
        t.igual("Edição de produto", "Whey Protein", ctx.produtoService.buscarPorId(whey.getId()).getNome());
        Produto temp = ctx.produtoService.cadastrar("Garrafa", CategoriaProduto.ACESSORIO, 1, BigDecimal.TEN);
        ctx.produtoService.excluir(temp.getId());
        t.igual("Exclusão de produto", 3, ctx.produtoService.listar().size());
    }

    private static void testarDashboardERelatorios(Verificador t, ContextoAplicacao ctx) {
        t.secao("Dashboard e relatórios (RF005)");
        ctx.alunoService.cadastrar("Davi Alves", "111.222.333-96", ""); // sem pagamento => Irregular
        ResumoDashboard resumo = ctx.dashboardService.resumir();
        t.igual("Total de alunos", 3, resumo.totalAlunos());
        t.igual("Alunos regulares", 2, resumo.alunosRegulares());
        t.igual("Alunos irregulares", 1, resumo.alunosIrregulares());
        t.igual("Produtos em falta", 1, resumo.produtosEmFalta());
        t.igual("Relatório de irregulares lista Davi", "Davi Alves",
                ctx.relatorioService.gerar("Alunos Irregulares").linhas().get(0).get(0));

        t.igual("Relatórios disponíveis", List.of("Alunos Irregulares", "Pagamentos", "Produtos em falta"),
                ctx.relatorioService.nomesDisponiveis());
        FormatadorTextoTabela formatador = new FormatadorTextoTabela();
        for (String nome : ctx.relatorioService.nomesDisponiveis()) {
            ResultadoRelatorio r = ctx.relatorioService.gerar(nome);
            System.out.println("\n  >> " + r.titulo());
            System.out.println(formatador.formatar(r).indent(4).stripTrailing());
            t.verdadeiro("Relatório '" + nome + "' gerado com cabeçalho", !r.colunas().isEmpty());
        }
        t.igual("Relatório de pagamentos lista os 3 lançamentos", 3, ctx.relatorioService.gerar("Pagamentos").linhas().size());
        t.verdadeiro("Valor formatado em reais", ctx.relatorioService.gerar("Pagamentos").linhas().get(0).get(1).startsWith("R$"));
        t.lanca("Relatório inexistente falha", RegraNegocioException.class, () -> ctx.relatorioService.gerar("Inexistente"));
    }
}
