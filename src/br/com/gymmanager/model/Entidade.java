package br.com.gymmanager.model;

/** Contrato mínimo de um objeto persistível: possui identificador numérico. */
public interface Entidade {

    int getId();

    void setId(int id);
}
