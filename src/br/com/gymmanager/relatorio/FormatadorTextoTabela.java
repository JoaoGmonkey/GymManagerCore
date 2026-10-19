package br.com.gymmanager.relatorio;

import java.util.ArrayList;
import java.util.List;

/**
 * Converte um ResultadoRelatorio em texto tabular (o formato que a TelaRelatorios
 * montava com Strings fixas). Apresentação separada do dado: SRP.
 */
public class FormatadorTextoTabela {

    public String formatar(ResultadoRelatorio r) {
        int n = r.colunas().size();
        int[] larg = new int[n];
        for (int i = 0; i < n; i++) {
            larg[i] = r.colunas().get(i).length();
            for (List<String> linha : r.linhas()) {
                larg[i] = Math.max(larg[i], linha.get(i).length());
            }
        }
        StringBuilder sb = new StringBuilder();
        List<String> cabecalho = r.colunas();
        sb.append(linha(cabecalho, larg)).append('\n');
        sb.append("-".repeat(linha(cabecalho, larg).length())).append('\n');
        List<String> linhas = new ArrayList<>();
        r.linhas().forEach(l -> linhas.add(linha(l, larg)));
        sb.append(String.join("\n", linhas));
        return sb.toString().stripTrailing();
    }

    private String linha(List<String> celulas, int[] larg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < celulas.size(); i++) {
            sb.append(String.format("%-" + (larg[i] + 3) + "s", celulas.get(i)));
        }
        return sb.toString().stripTrailing();
    }
}
