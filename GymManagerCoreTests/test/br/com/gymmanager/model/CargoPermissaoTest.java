package br.com.gymmanager.model;

import static org.junit.Assert.*;

import org.junit.Test;

/** CT-U30..U35 — permissões por cargo (RNF001), conforme tabela do documento de projeto. */
public class CargoPermissaoTest {

    @Test public void administradorPossuiTodasAsPermissoes() {
        for (Permissao p : Permissao.values()) {
            assertTrue("Administrador deveria ter " + p, Cargo.ADMINISTRADOR_GERAL.possui(p));
        }
    }

    @Test public void instrutorSoVisualizaAlunos() {
        assertTrue(Cargo.INSTRUTOR.possui(Permissao.ALUNOS_VISUALIZAR));
        assertFalse(Cargo.INSTRUTOR.possui(Permissao.ALUNOS_EDITAR));
        assertFalse(Cargo.INSTRUTOR.possui(Permissao.PAGAMENTOS_VISUALIZAR));
        assertFalse(Cargo.INSTRUTOR.possui(Permissao.FUNCIONARIOS_GERENCIAR));
    }

    @Test public void recepcionistaCadastraAlunosMasNaoGerenciaPagamentos() {
        assertTrue(Cargo.RECEPCIONISTA.possui(Permissao.ALUNOS_EDITAR));
        assertTrue(Cargo.RECEPCIONISTA.possui(Permissao.PAGAMENTOS_VISUALIZAR));
        assertFalse(Cargo.RECEPCIONISTA.possui(Permissao.PAGAMENTOS_GERENCIAR));
    }

    @Test public void estoquistaGerenciaEstoqueMasNaoAlunos() {
        assertTrue(Cargo.ESTOQUISTA.possui(Permissao.ESTOQUE_GERENCIAR));
        assertFalse(Cargo.ESTOQUISTA.possui(Permissao.ALUNOS_VISUALIZAR));
    }

    @Test public void financeiroGerenciaPagamentosEVeRelatorios() {
        assertTrue(Cargo.FINANCEIRO.possui(Permissao.PAGAMENTOS_GERENCIAR));
        assertTrue(Cargo.FINANCEIRO.possui(Permissao.RELATORIOS_VISUALIZAR));
        assertFalse(Cargo.FINANCEIRO.possui(Permissao.ESTOQUE_GERENCIAR));
    }

    @Test public void funcionarioDelegaPermissaoParaOCargo() {
        Funcionario f = new Funcionario("Rita", Cargo.RECEPCIONISTA, "rita", "hash");
        assertTrue(f.temPermissao(Permissao.ALUNOS_EDITAR));
        f.atualizarDados("Rita", Cargo.INSTRUTOR);
        assertFalse(f.temPermissao(Permissao.ALUNOS_EDITAR));
    }
}
