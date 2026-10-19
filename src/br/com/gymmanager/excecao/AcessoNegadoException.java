package br.com.gymmanager.excecao;

/** O cargo do usuário não possui a permissão exigida (RNF001). */
public class AcessoNegadoException extends RegraNegocioException {

    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
