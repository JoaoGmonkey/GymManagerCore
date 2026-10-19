package br.com.gymmanager.relatorio;

/**
 * Estratégia de geração de relatório (padrão Strategy).
 * Cada relatório é uma classe; novos relatórios não exigem alterar código existente (OCP).
 */
public interface Relatorio {

    String getNome();

    ResultadoRelatorio gerar();
}
