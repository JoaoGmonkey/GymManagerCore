package br.com.gymmanager.servico;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.repositorio.AlunoRepositorio;
import br.com.gymmanager.repositorio.PagamentoRepositorio;
import java.time.Clock;
import java.time.LocalDate;

/** Mantém a situação da matrícula dos alunos coerente com os pagamentos (RF004). */
public class MatriculaService {

    private final AlunoRepositorio alunos;
    private final PagamentoRepositorio pagamentos;
    private final PoliticaMatricula politica;
    private final Clock relogio;

    public MatriculaService(AlunoRepositorio alunos, PagamentoRepositorio pagamentos,
                            PoliticaMatricula politica, Clock relogio) {
        this.alunos = alunos;
        this.pagamentos = pagamentos;
        this.politica = politica;
        this.relogio = relogio;
    }

    public void atualizarSituacao(Aluno aluno) {
        aluno.atualizarStatus(politica.calcular(pagamentos.dataUltimoPagamento(aluno.getId()),
                LocalDate.now(relogio)));
        alunos.salvar(aluno);
    }

    /** Reavalia todos os alunos (pode ser agendado diariamente na versão web). */
    public void atualizarTodasAsSituacoes() {
        alunos.listarTodos().forEach(this::atualizarSituacao);
    }
}
