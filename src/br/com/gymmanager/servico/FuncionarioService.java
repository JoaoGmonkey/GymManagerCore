package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.Cargo;
import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.repositorio.FuncionarioRepositorio;
import br.com.gymmanager.seguranca.HashSenha;
import br.com.gymmanager.validacao.Validacao;
import java.util.List;

/** Casos de uso de funcionários (RF002). */
public class FuncionarioService {

    private static final int TAMANHO_MINIMO_SENHA = 6;

    private final FuncionarioRepositorio funcionarios;
    private final HashSenha hashSenha;

    public FuncionarioService(FuncionarioRepositorio funcionarios, HashSenha hashSenha) {
        this.funcionarios = funcionarios;
        this.hashSenha = hashSenha;
    }

    public Funcionario cadastrar(String nome, Cargo cargo, String usuario, String senha) {
        Validacao.exigirNaoNulo(cargo, "Cargo");
        String login = Validacao.exigirTexto(usuario, "Usuário");
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            throw new RegraNegocioException("A senha deve ter ao menos " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }
        if (funcionarios.buscarPorUsuario(login).isPresent()) {
            throw new RegraNegocioException("Já existe um funcionário com o usuário '" + login + "'.");
        }
        return funcionarios.salvar(new Funcionario(Validacao.exigirTexto(nome, "Nome"), cargo, login,
                hashSenha.gerar(senha)));
    }

    public Funcionario atualizar(int id, String nome, Cargo cargo) {
        Funcionario f = buscarPorId(id);
        Validacao.exigirNaoNulo(cargo, "Cargo");
        f.atualizarDados(Validacao.exigirTexto(nome, "Nome"), cargo);
        return funcionarios.salvar(f);
    }

    public void excluir(int id) {
        buscarPorId(id);
        funcionarios.remover(id);
    }

    public Funcionario buscarPorId(int id) {
        return funcionarios.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário", id));
    }

    public List<Funcionario> buscarPorNome(String trecho) {
        String t = Validacao.exigirTexto(trecho, "Nome para busca").toLowerCase();
        return funcionarios.listarTodos().stream().filter(f -> f.getNome().toLowerCase().contains(t)).toList();
    }

    public List<Funcionario> listar() {
        return funcionarios.listarTodos();
    }
}
