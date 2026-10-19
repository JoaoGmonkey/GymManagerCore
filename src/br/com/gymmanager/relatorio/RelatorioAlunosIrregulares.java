package br.com.gymmanager.relatorio;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.repositorio.AlunoRepositorio;
import java.util.List;

public class RelatorioAlunosIrregulares implements Relatorio {

    private final AlunoRepositorio alunos;

    public RelatorioAlunosIrregulares(AlunoRepositorio alunos) {
        this.alunos = alunos;
    }

    @Override
    public String getNome() {
        return "Alunos Irregulares";
    }

    @Override
    public ResultadoRelatorio gerar() {
        List<List<String>> linhas = alunos.listarTodos().stream()
                .filter(a -> !a.estaRegular())
                .map((Aluno a) -> List.of(a.getNome(), a.getStatusMatricula().getDescricao(),
                        Formatos.data(a.getDataIngresso())))
                .toList();
        return new ResultadoRelatorio(getNome(), List.of("Nome", "Situação", "Ingresso"), linhas);
    }
}
