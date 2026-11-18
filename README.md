# SantosSaneamento
Sistema Java Web (Spring Boot + Thymeleaf + JPA) para registro de chamados de problemas de esgoto
da Secretaria de Meio Ambiente de Santos.

## Como rodar
1. Abra a pasta `SantosSaneamento` (a que tem o `pom.xml`) no IntelliJ como projeto Maven.
2. Rode a classe `ChamadosApplication`.
3. Acesse http://localhost:8080

## Contas de teste (fixas, sem cadastro)
Na tela de login, clicar no quadradinho do perfil já preenche e-mail e senha.

| Perfil     | E-mail                     | Senha         | Entra em                    |
|------------|----------------------------|---------------|-----------------------------|
| Cidadão    | cidadao@santos.sp.gov.br   | cidadao123    | Registrar novo chamado      |
| Sabesp     | operador@sabesp.com.br     | sabesp123     | Fila de atendimento         |
| Prefeitura | gestor@santos.sp.gov.br    | prefeitura123 | Painel gerencial            |

## O que cada perfil faz
- **Cidadão:** registra chamado (endereço, bairro, tipo, data, hora, descrição e foto), acompanha os seus
  chamados e pode editar enquanto o status for "Novo".
- **Sabesp:** vê a fila, abre o chamado, altera o status e escreve um comentário.
- **Prefeitura:** painel gerencial, lista de todas as ocorrências com filtros (busca, status, bairro e tipo)
  e pode excluir um chamado.

Cada perfil só consegue abrir as páginas do próprio perfil (`AutenticacaoInterceptor`).

## Estrutura (mesmo padrão do exemplo da professora)
- `model/Chamado` — entidade JPA com validação (`@NotBlank`, `@Size`, `@NotNull`)
- `repository/ChamadoRepository` — `JpaRepository` com consultas por nome do método
- `service/ChamadoService` — regras (filtros, busca por id com 404)
- `controller/` — `HomeController` (login e telas de cada perfil) e `ChamadoController` (CRUD dos chamados)
- `security/` — login fixo (`LoginSimples`) e controle de acesso por perfil
- `resources/templates` — páginas Thymeleaf; `fragments/layout.html` é a barra lateral azul
- `resources/static` — CSS e JS

## Banco de dados
- Padrão: **H2 em memória** (console em http://localhost:8080/h2-console, JDBC URL `jdbc:h2:mem:chamadosdb`,
  usuário `sa`, senha em branco). Os dados voltam ao início quando a aplicação reinicia.
- **MySQL:** o driver já está no `pom.xml`; é só trocar as linhas do H2 pelas do MySQL, que estão comentadas
  no fim do `application.properties`.
- As fotos enviadas ficam na pasta `uploads/`, criada ao lado do projeto.
