package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.repositorio.FuncionarioRepositorio;
import br.com.gymmanager.seguranca.HashSenha;

/**
 * Autenticação (RNF004). No desktop o botão "Entrar" abria o Dashboard sem
 * conferir nada; agora o login realmente valida usuário e senha.
 */
public class AutenticacaoService {

    private static final String MENSAGEM = "Usuário ou senha inválidos.";

    private final FuncionarioRepositorio funcionarios;
    private final HashSenha hashSenha;

    public AutenticacaoService(FuncionarioRepositorio funcionarios, HashSenha hashSenha) {
        this.funcionarios = funcionarios;
        this.hashSenha = hashSenha;
    }

    public Funcionario autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) {
            throw new RegraNegocioException(MENSAGEM);
        }
        // mesma mensagem para usuário inexistente e senha errada (não revela qual dos dois falhou)
        return funcionarios.buscarPorUsuario(usuario.trim())
                .filter(f -> hashSenha.confere(senha, f.getSenhaHash()))
                .orElseThrow(() -> new RegraNegocioException(MENSAGEM));
    }
}
