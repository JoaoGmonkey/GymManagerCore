package br.com.gymmanager.repositorio;

import br.com.gymmanager.model.Entidade;
import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico de persistência (padrão Repository).
 * Na fase web basta criar implementações JDBC/MySQL; os serviços não mudam (DIP/OCP).
 */
public interface Repositorio<T extends Entidade> {

    T salvar(T entidade);

    Optional<T> buscarPorId(int id);

    List<T> listarTodos();

    void remover(int id);
}
