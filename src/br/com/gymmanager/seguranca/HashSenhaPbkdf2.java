package br.com.gymmanager.seguranca;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Implementação com PBKDF2 + salt aleatório (apenas API padrão do JDK). Formato: iteracoes:salt:hash. */
public class HashSenhaPbkdf2 implements HashSenha {

    private static final int ITERACOES = 65_536;
    private static final int TAMANHO_BITS = 256;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String gerar(String senhaPura) {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hash = derivar(senhaPura, salt, ITERACOES);
        return ITERACOES + ":" + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public boolean confere(String senhaPura, String hashArmazenado) {
        String[] partes = hashArmazenado.split(":");
        if (partes.length != 3) {
            return false;
        }
        byte[] salt = Base64.getDecoder().decode(partes[1]);
        byte[] esperado = Base64.getDecoder().decode(partes[2]);
        byte[] calculado = derivar(senhaPura, salt, Integer.parseInt(partes[0]));
        return MessageDigest.isEqual(esperado, calculado); // comparação em tempo constante
    }

    private byte[] derivar(String senha, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, TAMANHO_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Algoritmo de hash indisponível", e);
        }
    }
}
