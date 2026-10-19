package br.com.gymmanager.repositorio;

import br.com.gymmanager.model.Pagamento;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagamentoRepositorio extends Repositorio<Pagamento> {

    List<Pagamento> listarPorAluno(int alunoId);

    Optional<LocalDate> dataUltimoPagamento(int alunoId);
}
