package br.com.gymmanager.servico;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.repositorio.AlunoRepositorio;
import br.com.gymmanager.repositorio.ProdutoRepositorio;
import br.com.gymmanager.model.Produto;
import java.util.List;

/** Monta o resumo exibido na tela inicial. */
public class DashboardService {

    private final AlunoRepositorio alunos;
    private final ProdutoRepositorio produtos;

    public DashboardService(AlunoRepositorio alunos, ProdutoRepositorio produtos) {
        this.alunos = alunos;
        this.produtos = produtos;
    }

    public ResumoDashboard resumir() {
        List<Aluno> todos = alunos.listarTodos();
        List<Aluno> irregulares = todos.stream().filter(a -> !a.estaRegular()).toList();
        int emFalta = (int) produtos.listarTodos().stream().filter(Produto::estaEmFalta).count();
        return new ResumoDashboard(todos.size(), todos.size() - irregulares.size(), irregulares.size(),
                emFalta, irregulares);
    }
}
