package br.com.gymmanager.repositorio;

import br.com.gymmanager.model.Produto;
import java.util.List;

public interface ProdutoRepositorio extends Repositorio<Produto> {

    List<Produto> buscarPorNome(String trecho);
}
