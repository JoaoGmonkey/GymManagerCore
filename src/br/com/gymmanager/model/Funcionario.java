package br.com.gymmanager.model;

/**
 * Funcionário/usuário do sistema.
 *
 * Mudança importante: a classe não guarda mais a senha em texto puro nem
 * compara senhas (antes: {@code senha.equals(senhaDigitada)}). Ela guarda
 * apenas o hash; quem confere é o AutenticacaoService com um HashSenha.
 */
public class Funcionario implements Entidade {

    private int id;
    private String nome;
    private Cargo cargo;
    private final String usuario;
    private final String senhaHash;

    public Funcionario(String nome, Cargo cargo, String usuario, String senhaHash) {
        this.nome = nome;
        this.cargo = cargo;
        this.usuario = usuario;
        this.senhaHash = senhaHash;
    }

    public void atualizarDados(String nome, Cargo cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    public boolean temPermissao(Permissao permissao) {
        return cargo.possui(permissao);
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public Cargo getCargo() { return cargo; }
    public String getUsuario() { return usuario; }
    public String getSenhaHash() { return senhaHash; }
}
