package br.com.gymmanager.repositorio;

import br.com.gymmanager.model.Aluno;
import java.util.List;
import java.util.Optional;

public interface AlunoRepositorio extends Repositorio<Aluno> {

    Optional<Aluno> buscarPorCpf(String cpfNormalizado);

    List<Aluno> buscarPorNome(String trecho);
}
