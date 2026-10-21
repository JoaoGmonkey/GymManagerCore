package br.com.gymmanager.util;

import br.com.gymmanager.main.ContextoAplicacao;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/** Apoio aos testes: contexto limpo (repositórios em memória) com relógio fixo em 19/10/2026. */
public final class Cenario {

    public static final LocalDate HOJE = LocalDate.of(2026, 10, 19);
    public static final String CPF_A = "123.456.789-09";
    public static final String CPF_B = "987.654.321-00";

    private Cenario() { }

    public static Clock relogioFixo() {
        return Clock.fixed(Instant.parse("2026-10-19T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
    }

    public static ContextoAplicacao novo() {
        return new ContextoAplicacao(relogioFixo());
    }
}
