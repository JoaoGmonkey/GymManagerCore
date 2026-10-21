package br.com.gymmanager.seguranca;

import static org.junit.Assert.*;

import org.junit.Test;

/** CT-U40..U44 — proteção de senhas (RNF004). */
public class HashSenhaPbkdf2Test {

    private final HashSenha hash = new HashSenhaPbkdf2();

    @Test public void hashNaoContemASenhaOriginal() {
        assertFalse(hash.gerar("senha123").contains("senha123"));
    }

    @Test public void confereSenhaCorreta() {
        assertTrue(hash.confere("senha123", hash.gerar("senha123")));
    }

    @Test public void recusaSenhaIncorreta() {
        assertFalse(hash.confere("outra", hash.gerar("senha123")));
    }

    @Test public void mesmaSenhaGeraHashesDiferentesPorCausaDoSalt() {
        assertNotEquals(hash.gerar("senha123"), hash.gerar("senha123"));
    }

    @Test public void hashMalFormadoERecusadoSemExcecao() {
        assertFalse(hash.confere("x", "texto-sem-formato"));
    }
}
