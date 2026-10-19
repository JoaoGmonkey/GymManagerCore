package br.com.gymmanager.excecao;

/** O cargo do usuário não possui a permissão exigida (RNF001). */
public class AcessoNegadoException extends RegraNegocioException {

    private static final long serialVersionUID = 1L;

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
