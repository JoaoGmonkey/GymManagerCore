package br.com.gymmanager.repositorio.memoria;

import br.com.gymmanager.model.Entidade;
import br.com.gymmanager.repositorio.Repositorio;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Base comum dos repositórios em memória: elimina o código repetido de CRUD (code smell: Duplicated Code). */
public abstract class RepositorioMemoria<T extends Entidade> implements Repositorio<T> {

    protected final Map<Integer, T> dados = new LinkedHashMap<>();
    private int proximoId = 1;

    @Override
    public T salvar(T entidade) {
        if (entidade.getId() == 0) {
            entidade.setId(proximoId++);
        }
        dados.put(entidade.getId(), entidade);
        return entidade;
    }

    @Override
    public Optional<T> buscarPorId(int id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public void remover(int id) {
        dados.remove(id);
    }
}
