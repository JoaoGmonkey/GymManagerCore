package br.com.gymmanager.servico;

import br.com.gymmanager.model.Aluno;
import java.util.List;

/** Dados do painel inicial (antes escritos "no braço" na tela: 120, 20, 5). */
public record ResumoDashboard(int totalAlunos, int alunosRegulares, int alunosIrregulares,
                              int produtosEmFalta, List<Aluno> irregulares) { }
