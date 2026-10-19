package br.com.gymmanager.relatorio;

import java.util.List;

/** Resultado neutro de um relatório (dados, sem formatação): serve a Swing, HTML, JSON ou PDF. */
public record ResultadoRelatorio(String titulo, List<String> colunas, List<List<String>> linhas) { }
