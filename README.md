# Gym Manager — Core (PI, fase 2)

Núcleo de negócio do Gym Manager, refatorado a partir do sistema desktop (Java Swing) para ser reutilizado
no sistema web. **Não há nenhuma dependência de Swing, JDBC ou servidor**: só Java puro (JDK 17+).

## Estrutura
| Pacote | Responsabilidade |
|---|---|
| `model` | Entidades e enums do domínio (Aluno, Funcionario, Produto, Pagamento, Cargo, Permissao...) |
| `servico` | Casos de uso / regras de negócio (um serviço por assunto) |
| `repositorio` / `repositorio.memoria` | Contratos de persistência + implementação em memória (trocável por JDBC/MySQL) |
| `relatorio` | Relatórios (padrão Strategy) e formatação |
| `navegacao` | Menu e permissões por item (reuso como mapa de rotas na web) |
| `seguranca`, `validacao`, `excecao` | Hash de senha, validação de CPF/campos, exceções de negócio |
| `main` | `ContextoAplicacao` (injeção de dependência manual), `Verificador` e `Main` (testes) |

## Como executar (NetBeans)
1. *File > Open Project* e selecione a pasta `GymManagerCore`.
2. *Run Project* (F6). O `Main` executa a bateria de testes e imprime `[OK]`/`[FALHA]`.

Por linha de comando: `javac -encoding UTF-8 -d out $(find src -name "*.java")` e `java -cp out br.com.gymmanager.main.Main`.
