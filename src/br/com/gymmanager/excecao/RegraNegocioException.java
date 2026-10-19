package br.com.gymmanager.excecao;

/**
 * Violação de uma regra de negócio (dado inválido, duplicidade, permissão...).
 *
 * Substitui os JOptionPane.showMessageDialog que, no sistema desktop, faziam
 * o papel de "tratamento de erro" dentro das telas. Agora quem decide como
 * exibir a mensagem (Swing, página web, API REST) é a camada de apresentação.
 */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
