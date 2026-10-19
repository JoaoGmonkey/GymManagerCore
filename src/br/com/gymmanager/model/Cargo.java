package br.com.gymmanager.model;

import static br.com.gymmanager.model.Permissao.*;

import java.util.EnumSet;
import java.util.Set;

/**
 * Cargos do sistema e suas permissões, conforme o documento de projeto (etapa 1).
 *
 * No desktop os cargos eram listas de Strings diferentes em cada tela
 * ("Gerente", "Estagiário"...) e nem coincidiam com o documento. Agora existe
 * uma única fonte de verdade, aberta a extensão (novo cargo = nova constante).
 */
public enum Cargo {
    ADMINISTRADOR_GERAL("Administrador Geral", EnumSet.allOf(Permissao.class)),
    RECEPCIONISTA("Recepcionista", EnumSet.of(DASHBOARD_VISUALIZAR, ALUNOS_VISUALIZAR, ALUNOS_EDITAR,
            PAGAMENTOS_VISUALIZAR, PRODUTOS_VISUALIZAR)),
    INSTRUTOR("Instrutor", EnumSet.of(DASHBOARD_VISUALIZAR, ALUNOS_VISUALIZAR)),
    ESTOQUISTA("Estoquista", EnumSet.of(DASHBOARD_VISUALIZAR, PRODUTOS_VISUALIZAR, ESTOQUE_GERENCIAR)),
    FINANCEIRO("Financeiro", EnumSet.of(DASHBOARD_VISUALIZAR, ALUNOS_VISUALIZAR, PAGAMENTOS_VISUALIZAR,
            PAGAMENTOS_GERENCIAR, RELATORIOS_VISUALIZAR));

    private final String descricao;
    private final Set<Permissao> permissoes;

    Cargo(String descricao, Set<Permissao> permissoes) {
        this.descricao = descricao;
        this.permissoes = permissoes;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean possui(Permissao permissao) {
        return permissoes.contains(permissao);
    }
}
