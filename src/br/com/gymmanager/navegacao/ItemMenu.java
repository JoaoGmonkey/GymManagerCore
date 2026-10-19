package br.com.gymmanager.navegacao;

import br.com.gymmanager.model.Cargo;
import br.com.gymmanager.model.Permissao;
import java.util.Arrays;
import java.util.List;

/**
 * Itens do menu lateral e a permissão exigida por cada um.
 * Substitui o array de Strings + o método abrirTela() com cadeia de if/else
 * copiados em TODAS as telas do desktop (code smell: Duplicated Code / Long if-else chain).
 * Serve também de mapa de rotas para a versão web.
 */
public enum ItemMenu {
    DASHBOARD("Dashboard", Permissao.DASHBOARD_VISUALIZAR),
    ALUNOS("Alunos", Permissao.ALUNOS_VISUALIZAR),
    PAGAMENTOS("Pagamentos", Permissao.PAGAMENTOS_VISUALIZAR),
    FUNCIONARIOS("Funcionários", Permissao.FUNCIONARIOS_GERENCIAR),
    ESTOQUE("Estoque", Permissao.PRODUTOS_VISUALIZAR),
    RELATORIOS("Relatórios", Permissao.RELATORIOS_VISUALIZAR);

    private final String titulo;
    private final Permissao permissaoExigida;

    ItemMenu(String titulo, Permissao permissaoExigida) {
        this.titulo = titulo;
        this.permissaoExigida = permissaoExigida;
    }

    public String getTitulo() {
        return titulo;
    }

    public Permissao getPermissaoExigida() {
        return permissaoExigida;
    }

    public static List<ItemMenu> visiveisPara(Cargo cargo) {
        return Arrays.stream(values()).filter(i -> cargo.possui(i.permissaoExigida)).toList();
    }
}
