package br.com.gymmanager.repositorio.memoria;

import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.repositorio.FuncionarioRepositorio;
import java.util.Optional;

public class FuncionarioRepositorioMemoria extends RepositorioMemoria<Funcionario> implements FuncionarioRepositorio {

    @Override
    public Optional<Funcionario> buscarPorUsuario(String usuario) {
        return dados.values().stream().filter(f -> f.getUsuario().equalsIgnoreCase(usuario)).findFirst();
    }
}
