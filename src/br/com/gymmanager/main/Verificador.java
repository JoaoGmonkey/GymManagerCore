package br.com.gymmanager.main;

import java.util.Objects;

/**
 * Mini-framework de testes para o main() (sem JUnit, como pede a atividade).
 * Imprime [OK]/[FALHA] por verificação e acumula o total.
 */
public class Verificador {

    private int ok;
    private int falhas;

    public void verdadeiro(String descricao, boolean condicao) {
        if (condicao) {
            ok++;
            System.out.println("  [OK]    " + descricao);
        } else {
            falhas++;
            System.out.println("  [FALHA] " + descricao);
        }
    }

    public void igual(String descricao, Object esperado, Object atual) {
        boolean iguais = Objects.equals(esperado, atual);
        verdadeiro(descricao + (iguais ? "" : " (esperado=" + esperado + ", atual=" + atual + ")"), iguais);
    }

    /** Verifica que a ação lança a exceção esperada (e devolve a mensagem para conferência). */
    public void lanca(String descricao, Class<? extends RuntimeException> tipo, Runnable acao) {
        try {
            acao.run();
            verdadeiro(descricao + " (nenhuma exceção lançada)", false);
        } catch (RuntimeException e) {
            verdadeiro(descricao + " -> \"" + e.getMessage() + "\"", tipo.isInstance(e));
        }
    }

    public void secao(String titulo) {
        System.out.println("\n== " + titulo + " ==");
    }

    public boolean passou() {
        return falhas == 0;
    }

    public void resumo() {
        System.out.println("\n----------------------------------------");
        System.out.println("Verificações aprovadas: " + ok + " | Falhas: " + falhas);
        System.out.println(passou() ? "RESULTADO: TUDO FUNCIONANDO" : "RESULTADO: HÁ FALHAS");
    }
}
