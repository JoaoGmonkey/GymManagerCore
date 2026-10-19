package br.com.gymmanager.repositorio.memoria;

import br.com.gymmanager.model.Produto;
import br.com.gymmanager.repositorio.ProdutoRepositorio;
import java.util.List;

public class ProdutoRepositorioMemoria extends RepositorioMemoria<Produto> implements ProdutoRepositorio {

    @Override
    public List<Produto> buscarPorNome(String trecho) {
        String t = trecho.toLowerCase();
        return dados.values().stream().filter(p -> p.getNome().toLowerCase().contains(t)).toList();
    }
}
