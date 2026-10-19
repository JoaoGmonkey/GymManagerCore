package br.com.gymmanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pagamento de mensalidade. Agora é só um registro imutável.
 * Antes, {@code registrarPagamento()} alterava o status do aluno dentro desta
 * classe (acoplamento e responsabilidade extra); esse efeito passou para o
 * PagamentoService/PoliticaMatricula.
 */
public class Pagamento implements Entidade {

    private int id;
    private final Aluno aluno;
    private final BigDecimal valor;
    private final LocalDate dataPagamento;

    public Pagamento(Aluno aluno, BigDecimal valor, LocalDate dataPagamento) {
        this.aluno = aluno;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public Aluno getAluno() { return aluno; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getDataPagamento() { return dataPagamento; }
}
