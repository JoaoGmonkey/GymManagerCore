package br.com.gymmanager.model;

/** Categorias de produto (antes eram Strings digitadas/escolhidas na tela). */
public enum CategoriaProduto {
    SUPLEMENTO("Suplemento"),
    ROUPAS("Roupas"),
    ACESSORIO("Acessório");

    private final String descricao;

    CategoriaProduto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
