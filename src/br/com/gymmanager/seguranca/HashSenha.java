package br.com.gymmanager.seguranca;

/** Abstração para proteção de senhas (DIP): serviços dependem disto, não de um algoritmo. */
public interface HashSenha {

    String gerar(String senhaPura);

    boolean confere(String senhaPura, String hashArmazenado);
}
