package br.com.gymmanager.model;

import br.com.gymmanager.excecao.RegraNegocioException;
import java.math.BigDecimal;

/** Produto do estoque (suplementos, vestuário...). */
public class Produto implements Entidade {

    /** Quantidade a partir da qual o produto é considerado "em falta" (regra antes implícita nos dados da tela). */
    public static final int ESTOQUE_MINIMO = 5;

    private int id;
    private String nome;
    private CategoriaProduto categoria;
    private int quantidadeEstoque;
    private BigDecimal preco;

    public Produto(String nome, CategoriaProduto categoria, int quantidadeEstoque, BigDecimal preco) {
        this.nome = nome;
        this.categoria = categoria;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
    }

    public void atualizarDados(String nome, CategoriaProduto categoria, BigDecimal preco) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    /** Entrada de estoque. */
    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de entrada deve ser maior que zero.");
        }
        this.quantidadeEstoque += quantidade;
    }

    /** Saída de estoque; nunca deixa o saldo negativo. */
    public void removerEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de saída deve ser maior que zero.");
        }
        if (quantidade > quantidadeEstoque) {
            throw new RegraNegocioException("Estoque insuficiente de '" + nome + "': disponível "
                    + quantidadeEstoque + ", solicitado " + quantidade + ".");
        }
        this.quantidadeEstoque -= quantidade;
    }

    public boolean estaEmFalta() {
        return quantidadeEstoque <= ESTOQUE_MINIMO;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public CategoriaProduto getCategoria() { return categoria; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public BigDecimal getPreco() { return preco; }
}
