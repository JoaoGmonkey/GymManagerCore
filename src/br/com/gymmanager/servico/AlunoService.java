package br.com.gymmanager.servico;

import br.com.gymmanager.excecao.RecursoNaoEncontradoException;
import br.com.gymmanager.excecao.RegraNegocioException;
import br.com.gymmanager.model.Aluno;
import br.com.gymmanager.repositorio.AlunoRepositorio;
import br.com.gymmanager.validacao.Validacao;
import br.com.gymmanager.validacao.ValidadorCpf;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** Casos de uso de alunos (RF001). Substitui a lógica que estava dentro de TelaAlunos. */
public class AlunoService {

    private final AlunoRepositorio alunos;
    private final MatriculaService matriculas;
    private final Clock relogio;

    public AlunoService(AlunoRepositorio alunos, MatriculaService matriculas, Clock relogio) {
        this.alunos = alunos;
        this.matriculas = matriculas;
        this.relogio = relogio;
    }

    public Aluno cadastrar(String nome, String cpf, String telefone) {
        String cpfNormalizado = validarCpfUnico(cpf, null);
        Aluno aluno = new Aluno(Validacao.exigirTexto(nome, "Nome"), cpfNormalizado,
                telefone == null ? "" : telefone.trim(), LocalDate.now(relogio));
        alunos.salvar(aluno);
        matriculas.atualizarSituacao(aluno);
        return aluno;
    }

    public Aluno atualizar(int id, String nome, String cpf, String telefone) {
        Aluno aluno = buscarPorId(id);
        String cpfNormalizado = validarCpfUnico(cpf, aluno);
        aluno.atualizarDados(Validacao.exigirTexto(nome, "Nome"), cpfNormalizado,
                telefone == null ? "" : telefone.trim());
        return alunos.salvar(aluno);
    }

    public void excluir(int id) {
        buscarPorId(id);
        alunos.remover(id);
    }

    public Aluno buscarPorId(int id) {
        return alunos.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Aluno", id));
    }

    public List<Aluno> buscarPorNome(String trecho) {
        return alunos.buscarPorNome(Validacao.exigirTexto(trecho, "Nome para busca"));
    }

    public List<Aluno> listar() {
        return alunos.listarTodos();
    }

    private String validarCpfUnico(String cpf, Aluno aluno) {
        if (!ValidadorCpf.ehValido(cpf)) {
            throw new RegraNegocioException("CPF inválido.");
        }
        String normalizado = ValidadorCpf.normalizar(cpf);
        boolean duplicado = alunos.buscarPorCpf(normalizado)
                .filter(existente -> aluno == null || existente.getId() != aluno.getId())
                .isPresent();
        if (duplicado) {
            throw new RegraNegocioException("Já existe um aluno cadastrado com este CPF.");
        }
        return normalizado;
    }
}
