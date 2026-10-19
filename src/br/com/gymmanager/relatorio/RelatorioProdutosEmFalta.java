package br.com.gymmanager.relatorio;

import br.com.gymmanager.model.Produto;
import br.com.gymmanager.repositorio.ProdutoRepositorio;
import java.util.List;

public class RelatorioProdutosEmFalta implements Relatorio {

    private final ProdutoRepositorio produtos;

    public RelatorioProdutosEmFalta(ProdutoRepositorio produtos) {
        this.produtos = produtos;
    }

    @Override
    public String getNome() {
        return "Produtos em falta";
    }

    @Override
    public ResultadoRelatorio gerar() {
        List<List<String>> linhas = produtos.listarTodos().stream()
                .filter(Produto::estaEmFalta)
                .map(p -> List.of(p.getNome(), p.getCategoria().getDescricao(),
                        String.valueOf(p.getQuantidadeEstoque())))
                .toList();
        return new ResultadoRelatorio(getNome(), List.of("Produto", "Categoria", "QTD"), linhas);
    }
}
