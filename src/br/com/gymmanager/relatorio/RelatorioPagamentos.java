package br.com.gymmanager.relatorio;

import br.com.gymmanager.repositorio.PagamentoRepositorio;
import java.util.List;

public class RelatorioPagamentos implements Relatorio {

    private final PagamentoRepositorio pagamentos;

    public RelatorioPagamentos(PagamentoRepositorio pagamentos) {
        this.pagamentos = pagamentos;
    }

    @Override
    public String getNome() {
        return "Pagamentos";
    }

    @Override
    public ResultadoRelatorio gerar() {
        List<List<String>> linhas = pagamentos.listarTodos().stream()
                .map(p -> List.of(p.getAluno().getNome(), Formatos.moeda(p.getValor()),
                        Formatos.data(p.getDataPagamento())))
                .toList();
        return new ResultadoRelatorio(getNome(), List.of("Nome", "Valor", "Data"), linhas);
    }
}
