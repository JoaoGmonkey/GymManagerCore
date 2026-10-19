package br.com.gymmanager.repositorio;

import br.com.gymmanager.model.Funcionario;
import java.util.Optional;

public interface FuncionarioRepositorio extends Repositorio<Funcionario> {

    Optional<Funcionario> buscarPorUsuario(String usuario);
}
