package br.com.gymmanager.model;

import java.time.LocalDate;

/**
 * Aluno da academia. Classe de domínio pura: sem Swing, sem banco de dados.
 * A validação de CPF e a regra dos 30 dias ficam em classes próprias
 * (ValidadorCpf e PoliticaMatricula) para manter a responsabilidade única.
 */
public class Aluno implements Entidade {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private final LocalDate dataIngresso;
    private StatusMatricula statusMatricula;

    public Aluno(String nome, String cpf, String telefone, LocalDate dataIngresso) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataIngresso = dataIngresso;
        this.statusMatricula = StatusMatricula.IRREGULAR; // sem pagamento => irregular (RN do documento)
    }

    public void atualizarDados(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public void atualizarStatus(StatusMatricula novoStatus) {
        this.statusMatricula = novoStatus;
    }

    public boolean estaRegular() {
        return statusMatricula == StatusMatricula.REGULAR;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public LocalDate getDataIngresso() { return dataIngresso; }
    public StatusMatricula getStatusMatricula() { return statusMatricula; }
}
