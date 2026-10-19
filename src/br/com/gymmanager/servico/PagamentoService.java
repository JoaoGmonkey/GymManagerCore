package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.model.Pagamento;
import br.com.gymmanager.repositorio.PagamentoRepositorio;
import br.com.gymmanager.validacao.Validacao;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Casos de uso de pagamentos (RF004): registra o pagamento e atualiza a matrícula. */
public class PagamentoService {

    private final PagamentoRepositorio pagamentos;
    private final AlunoService alunos;
    private final MatriculaService matriculas;
    private final Clock relogio;

    public PagamentoService(PagamentoRepositorio pagamentos, AlunoService alunos,
                            MatriculaService matriculas, Clock relogio) {
        this.pagamentos = pagamentos;
        this.alunos = alunos;
        this.matriculas = matriculas;
        this.relogio = relogio;
    }

    public Pagamento registrar(int alunoId, BigDecimal valor, LocalDate data) {
        Validacao.exigirNaoNulo(valor, "Valor");
        Validacao.exigirNaoNulo(data, "Data");
        if (valor.signum() <= 0) {
            throw new RegraNegocioException("O valor do pagamento deve ser maior que zero.");
        }
        if (data.isAfter(LocalDate.now(relogio))) {
            throw new RegraNegocioException("A data do pagamento não pode estar no futuro.");
        }
        Aluno aluno = alunos.buscarPorId(alunoId);
        Pagamento pagamento = pagamentos.salvar(new Pagamento(aluno, valor, data));
        matriculas.atualizarSituacao(aluno);
        return pagamento;
    }

    public List<Pagamento> historico() {
        return pagamentos.listarTodos();
    }

    public List<Pagamento> historicoDoAluno(int alunoId) {
        return pagamentos.listarPorAluno(alunoId);
    }
}
