package br.com.gymmanager.excecao;

/** Lançada quando um registro buscado por id/chave não existe. */
public class RecursoNaoEncontradoException extends RegraNegocioException {

    public RecursoNaoEncontradoException(String recurso, Object chave) {
        super(recurso + " não encontrado(a): " + chave);
    }
}
