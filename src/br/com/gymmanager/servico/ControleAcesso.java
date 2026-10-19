package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.AcessoNegadoException;
import br.com.gymmanager.model.Funcionario;
import br.com.gymmanager.model.Permissao;

/** Verificação de permissões por cargo (RNF001). Na fase web será usada por filtros/interceptadores. */
public class ControleAcesso {

    public boolean pode(Funcionario usuario, Permissao permissao) {
        return usuario != null && usuario.temPermissao(permissao);
    }

    public void exigir(Funcionario usuario, Permissao permissao) {
        if (!pode(usuario, permissao)) {
            throw new AcessoNegadoException("Acesso negado: o cargo não possui a permissão " + permissao + ".");
        }
    }
}
