package br.com.gymmanager.validacao;

/**
 * Validação de CPF (algoritmo dos dígitos verificadores).
 * Requisito do documento: "O sistema irá validar CPF automaticamente".
 * No desktop o CPF aceitava qualquer texto ("12344").
 */
public final class ValidadorCpf {

    private ValidadorCpf() { }

    /** Remove pontos, traços e espaços. */
    public static String normalizar(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    public static boolean ehValido(String cpf) {
        String n = normalizar(cpf);
        if (n.length() != 11 || n.chars().distinct().count() == 1) {
            return false;
        }
        return digito(n, 9) == n.charAt(9) - '0' && digito(n, 10) == n.charAt(10) - '0';
    }

    private static int digito(String n, int qtd) {
        int soma = 0;
        for (int i = 0; i < qtd; i++) {
            soma += (n.charAt(i) - '0') * (qtd + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
