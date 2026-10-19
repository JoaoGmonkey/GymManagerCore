package br.com.gymmanager.repositorio.memoria;

import br.com.gymmanager.model.Pagamento;
import br.com.gymmanager.repositorio.PagamentoRepositorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PagamentoRepositorioMemoria extends RepositorioMemoria<Pagamento> implements PagamentoRepositorio {

    @Override
    public List<Pagamento> listarPorAluno(int alunoId) {
        return dados.values().stream().filter(p -> p.getAluno().getId() == alunoId).toList();
    }

    @Override
    public Optional<LocalDate> dataUltimoPagamento(int alunoId) {
        return listarPorAluno(alunoId).stream().map(Pagamento::getDataPagamento).max(LocalDate::compareTo);
    }
}
