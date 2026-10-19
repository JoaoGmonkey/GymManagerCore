package br.com.gymmanager.validacao;

import br.com.gymmanager.excecao.RegraNegocioException;

/**
 * Validações de campo reutilizáveis. No desktop o mesmo
 * {@code if (x == null || x.trim().isEmpty()) return;} se repetia dezenas de vezes.
 */
public final class Validacao {

    private Validacao() { }

    public static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new RegraNegocioException("O campo '" + campo + "' é obrigatório.");
        }
        return valor.trim();
    }

    public static void exigirNaoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new RegraNegocioException("O campo '" + campo + "' é obrigatório.");
        }
    }
}
