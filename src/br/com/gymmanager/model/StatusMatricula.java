package br.com.gymmanager.model;

/**
 * Situação da matrícula do aluno.
 * Substitui as Strings "Regular"/"Irregular" espalhadas pelo código desktop
 * (code smell: Magic Strings / Primitive Obsession).
 */
public enum StatusMatricula {
    REGULAR("Regular"),
    IRREGULAR("Irregular");

    private final String descricao;

    StatusMatricula(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
