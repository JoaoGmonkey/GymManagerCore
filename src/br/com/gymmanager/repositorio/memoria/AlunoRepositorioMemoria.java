package br.com.gymmanager.repositorio.memoria;

import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.repositorio.AlunoRepositorio;
import java.util.List;
import java.util.Optional;

public class AlunoRepositorioMemoria extends RepositorioMemoria<Aluno> implements AlunoRepositorio {

    @Override
    public Optional<Aluno> buscarPorCpf(String cpfNormalizado) {
        return dados.values().stream().filter(a -> a.getCpf().equals(cpfNormalizado)).findFirst();
    }

    @Override
    public List<Aluno> buscarPorNome(String trecho) {
        String t = trecho.toLowerCase();
        return dados.values().stream().filter(a -> a.getNome().toLowerCase().contains(t)).toList();
    }
}
