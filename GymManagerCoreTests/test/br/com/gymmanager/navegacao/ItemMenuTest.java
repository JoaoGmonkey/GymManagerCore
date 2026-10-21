package br.com.gymmanager.navegacao;

import static org.junit.Assert.*;

import br.com.gymmanager.model.Cargo;
import java.util.List;
import org.junit.Test;

/** CT-U36..U38 — itens de menu exibidos conforme o cargo. */
public class ItemMenuTest {

    @Test public void administradorVeTodosOsItens() {
        assertEquals(ItemMenu.values().length, ItemMenu.visiveisPara(Cargo.ADMINISTRADOR_GERAL).size());
    }

    @Test public void instrutorNaoVePagamentosFuncionariosEstoqueNemRelatorios() {
        List<ItemMenu> itens = ItemMenu.visiveisPara(Cargo.INSTRUTOR);
        assertEquals(List.of(ItemMenu.DASHBOARD, ItemMenu.ALUNOS), itens);
    }

    @Test public void estoquistaVeDashboardEEstoque() {
        assertEquals(List.of(ItemMenu.DASHBOARD, ItemMenu.ESTOQUE), ItemMenu.visiveisPara(Cargo.ESTOQUISTA));
    }

    @Test public void tituloDoItemFuncionariosTemAcento() {
        assertEquals("Funcionários", ItemMenu.FUNCIONARIOS.getTitulo());
    }
}
