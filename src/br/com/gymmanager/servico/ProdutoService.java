package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.CategoriaProduto;
import br.com.gymmanager.model.Produto;
import br.com.gymmanager.repositorio.ProdutoRepositorio;
import br.com.gymmanager.validacao.Validacao;
import java.math.BigDecimal;
import java.util.List;

/** Casos de uso de produtos e estoque (RF003). A soma do estoque deixou de ser feita lendo células de JTable. */
public class ProdutoService {

    private final ProdutoRepositorio produtos;

    public ProdutoService(ProdutoRepositorio produtos) {
        this.produtos = produtos;
    }

    public Produto cadastrar(String nome, CategoriaProduto categoria, int quantidade, BigDecimal preco) {
        Validacao.exigirNaoNulo(categoria, "Categoria");
        if (quantidade < 0) {
            throw new RegraNegocioException("A quantidade não pode ser negativa.");
        }
        validarPreco(preco);
        return produtos.salvar(new Produto(Validacao.exigirTexto(nome, "Nome"), categoria, quantidade, preco));
    }

    public Produto atualizar(int id, String nome, CategoriaProduto categoria, BigDecimal preco) {
        Produto produto = buscarPorId(id);
        Validacao.exigirNaoNulo(categoria, "Categoria");
        validarPreco(preco);
        produto.atualizarDados(Validacao.exigirTexto(nome, "Nome"), categoria, preco);
        return produtos.salvar(produto);
    }

    public void excluir(int id) {
        buscarPorId(id);
        produtos.remover(id);
    }

    public Produto registrarEntrada(int id, int quantidade) {
        Produto produto = buscarPorId(id);
        produto.adicionarEstoque(quantidade);
        return produtos.salvar(produto);
    }

    public Produto registrarSaida(int id, int quantidade) {
        Produto produto = buscarPorId(id);
        produto.removerEstoque(quantidade);
        return produtos.salvar(produto);
    }

    public Produto buscarPorId(int id) {
        return produtos.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    public List<Produto> buscarPorNome(String trecho) {
        return produtos.buscarPorNome(Validacao.exigirTexto(trecho, "Produto para busca"));
    }

    public List<Produto> listar() {
        return produtos.listarTodos();
    }

    public List<Produto> listarEmFalta() {
        return produtos.listarTodos().stream().filter(Produto::estaEmFalta).toList();
    }

    public int totalDeUnidades() {
        return produtos.listarTodos().stream().mapToInt(Produto::getQuantidadeEstoque).sum();
    }

    private void validarPreco(BigDecimal preco) {
        Validacao.exigirNaoNulo(preco, "Preço");
        if (preco.signum() < 0) {
            throw new RegraNegocioException("O preço não pode ser negativo.");
        }
    }
}
