package br.com.gymmanager.servico;

import br.com.gymmanager.model.StatusMatricula;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Regra do documento de projeto: "matrícula irregular = ausência de pagamento
 * nos últimos 30 dias". Isolada numa classe pequena e testável (SRP);
 * se a regra mudar (ex.: 45 dias), só esta classe é alterada.
 */
public class PoliticaMatricula {

    public static final int DIAS_DE_TOLERANCIA = 30;

    public StatusMatricula calcular(Optional<LocalDate> ultimoPagamento, LocalDate hoje) {
        boolean pagouRecentemente = ultimoPagamento
                .map(data -> !data.isBefore(hoje.minusDays(DIAS_DE_TOLERANCIA)))
                .orElse(false);
        return pagouRecentemente ? StatusMatricula.REGULAR : StatusMatricula.IRREGULAR;
    }
}
