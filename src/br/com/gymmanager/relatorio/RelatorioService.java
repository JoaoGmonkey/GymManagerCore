package br.com.gymmanager.relatorio;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Catálogo de relatórios (RF005). Recebe as estratégias por injeção; não conhece nenhuma concreta. */
public class RelatorioService {

    private final Map<String, Relatorio> relatorios = new LinkedHashMap<>();

    public RelatorioService(List<Relatorio> disponiveis) {
        disponiveis.forEach(r -> relatorios.put(r.getNome(), r));
    }

    public List<String> nomesDisponiveis() {
        return List.copyOf(relatorios.keySet());
    }

    public ResultadoRelatorio gerar(String nome) {
        Relatorio r = relatorios.get(nome);
        if (r == null) {
            throw new RecursoNaoEncontradoException("Relatório", nome);
        }
        return r.gerar();
    }
}
