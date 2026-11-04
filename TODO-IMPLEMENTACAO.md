# Todo list de implementação

## Projeto Final — Introdução ao Quarkus

Este documento organiza a implementação passo a passo do projeto de Controle
de Tarefas, conforme a divisão em cinco tasks e os 16 requisitos avaliativos
descritos em [trabalho-final.md](./trabalho-final.md).

## Legenda

- [x] Concluído
- [ ] Pendente
- **Responsável:** integrante principal pela execução
- **Dependência:** etapa que precisa estar concluída antes do início

> O planejamento e a divisão das responsabilidades já foram definidos. As
> etapas de implementação permanecem pendentes até que exista código,
> execução reproduzível ou evidência correspondente.

---

## 1. Planejamento e decisões compartilhadas

### 1.1 Consolidar o planejamento

- [x] Definir o domínio como **Controle de Tarefas**.
- [x] Definir a arquitetura `Resource → Service → Repository`.
- [x] Dividir o trabalho entre cinco integrantes.
- [x] Associar as tasks aos 16 requisitos.
- [x] Definir H2 como banco de desenvolvimento e teste.
- [x] Definir o mock HTTP local como serviço externo.
- [x] Definir a regra de negócio:
  `Tarefa CANCELADA não pode ser alterada para CONCLUIDA`.
- [x] Definir a métrica de negócio como contador de tarefas criadas.
- [x] Definir a necessidade de documentação, coleção Postman e evidências.

**Responsável:** todo o grupo  
**Status:** concluído

### 1.2 Fechar os contratos antes do código

- [ ] Definir os campos de `Tarefa`:
  `id`, `titulo`, `descricao`, `status` e `prioridade`.
- [ ] Definir os valores permitidos para `status`.
- [ ] Definir os valores permitidos para `prioridade`.
- [ ] Definir quais campos são obrigatórios.
- [ ] Definir as restrições de Bean Validation.
- [ ] Definir o formato de `TarefaRequest`.
- [ ] Definir o formato de `TarefaResponse`.
- [ ] Definir o formato da resposta de criação.
- [ ] Definir o comportamento quando o ID não existir.
- [ ] Definir o status HTTP da transição de negócio inválida.
  Recomenda-se `409 Conflict`.
- [ ] Definir o contrato de resposta do mock externo.
- [ ] Definir os status HTTP por operação:

| Operação | Sucesso | Falha esperada |
|---|---:|---:|
| `GET /tarefas` | `200` | — |
| `GET /tarefas/{id}` | `200` | `404` |
| `POST /tarefas` | `201` + `Location` | `400` |
| `PUT /tarefas/{id}` | `200` | `400` ou `404` |
| `DELETE /tarefas/{id}` | `204` | `404` |
| Transição inválida | — | `409` |

**Responsável:** todo o grupo  
**Dependência:** planejamento consolidado

### 1.3 Preparar o projeto e o fluxo de trabalho

- [ ] Confirmar Java 21 instalado.
- [ ] Confirmar Maven funcionando.
- [ ] Criar ou validar o projeto Quarkus.
- [ ] Adicionar as dependências necessárias:
  REST, JSON, CDI, REST Client, Hibernate ORM Panache, H2,
  Bean Validation, Fault Tolerance, Health e Micrometer.
- [ ] Criar a estrutura de pacotes por feature e boundary:

```text
src/main/java/br/com/equipe/projeto/
├── tarefas/
│   ├── boundary/
│   ├── domain/
│   └── repository/
├── integracao/
│   └── servicoexterno/
└── platform/
    ├── health/
    └── metrics/
```

- [ ] Criar as branches das cinco tasks.
- [ ] Criar as issues correspondentes no GitHub.
- [ ] Configurar labels e milestone.
- [ ] Definir que cada Pull Request terá testes e evidência mínima.

**Responsável:** todo o grupo  
**Dependência:** planejamento consolidado

---

## 2. Task 1 — API REST, DTOs e contrato HTTP

**Responsável:** Integrante 1  
**Requisitos principais:** 01, 03 e 04  
**Dependência:** contratos compartilhados e projeto preparado

### 2.1 Criar os DTOs

- [x] Criar `TarefaRequest`.
- [x] Criar `TarefaResponse`.
- [x] Manter DTOs separados da Entity.
- [x] Aplicar Lombok de forma consciente nos DTOs.
- [x] Evitar `@Data` indiscriminado em entidade JPA.
- [x] Definir conversões entre DTOs e objetos de domínio/Entity.

### 2.2 Criar o Resource

- [x] Criar `TarefaResource`.
- [x] Usar `@Path`.
- [x] Usar `@GET`, `@POST`, `@PUT` e `@DELETE`.
- [x] Configurar `@Produces(MediaType.APPLICATION_JSON)`.
- [x] Configurar `@Consumes(MediaType.APPLICATION_JSON)`.
- [x] Injetar o Service via CDI.
- [x] Não acessar o Repository diretamente.
- [x] Não colocar regra de negócio no Resource.

### 2.3 Disponibilizar as rotas

- [x] Implementar `GET /tarefas`.
- [x] Implementar `GET /tarefas/{id}`.
- [x] Implementar `POST /tarefas`.
- [x] Implementar `PUT /tarefas/{id}`.
- [x] Implementar `DELETE /tarefas/{id}`.
- [x] Confirmar que as URLs não possuem verbos.
- [x] Confirmar que todas as respostas possuem JSON quando aplicável.

### 2.4 Demonstrar o requisito 01

- [x] Executar o GET de coleção.
- [x] Executar o POST de uma tarefa.
- [x] Executar o GET por ID.
- [x] Executar o PUT da tarefa.
- [x] Executar o GET novamente para confirmar a alteração.
- [x] Executar o DELETE.
- [ ] Registrar as requisições na coleção Postman.
- [ ] Registrar as respostas em `docs/evidencias.md`.

> O requisito 01 está completamente previsto no planejamento, mas só deve
> ser marcado como concluído depois que as cinco operações forem executadas
> contra a aplicação.

---

## 3. Task 2 — CDI, regra de negócio e validação

**Responsável:** Integrante 2  
**Requisitos principais:** 05 e 14  
**Dependência:** DTOs, Resource e contratos compartilhados

### 3.1 Criar o Service

- [x] Criar `TarefaService`.
- [x] Anotar o Service com `@ApplicationScoped`.
- [ ] Injetar o Repository no Service.
- [x] Fazer o Resource delegar as operações ao Service.
- [x] Centralizar as regras de domínio no Service.

### 3.2 Implementar a regra de negócio

- [x] Identificar a tarefa existente antes da alteração.
- [x] Verificar a transição de status.
- [x] Impedir `CANCELADA → CONCLUIDA`.
- [x] Criar uma exceção específica para a transição inválida.
- [x] Definir o status HTTP da exceção, preferencialmente `409`.
- [x] Criar teste unitário da regra.
- [ ] Criar uma requisição Postman para demonstrar a regra.

### 3.3 Implementar Bean Validation

- [x] Adicionar `@Valid` nos endpoints de entrada.
- [x] Usar pelo menos duas restrições distintas.
- [x] Usar, por exemplo, `@NotBlank` no título.
- [x] Usar, por exemplo, `@Size` na descrição ou no título.
- [x] Confirmar que payload inválido retorna `400`.
- [x] Registrar o cenário de validação nos testes.
- [ ] Registrar o cenário na coleção Postman.

---

## 4. Task 3 — Persistência, CRUD e transações

**Responsável:** Integrante 3  
**Requisitos principais:** 02, 11, 12 e 13  
**Dependência:** contratos, Service e projeto preparado

### 4.1 Criar a persistência

- [ ] Criar `TarefaEntity`.
- [ ] Adicionar `@Entity`.
- [ ] Definir o identificador da entidade.
- [ ] Mapear os campos da tarefa.
- [ ] Manter a Entity separada dos DTOs.
- [ ] Criar `TarefaRepository`.
- [ ] Implementar `PanacheRepository`.
- [ ] Configurar Hibernate ORM.
- [ ] Configurar o banco H2.
- [ ] Confirmar que o banco inicia sem instalação externa.

### 4.2 Implementar o CRUD

- [ ] Implementar criação.
- [ ] Implementar consulta de coleção.
- [ ] Implementar consulta por ID.
- [ ] Implementar atualização.
- [ ] Implementar exclusão.
- [ ] Tratar entidade não encontrada.
- [ ] Integrar Resource, Service e Repository.

### 4.3 Configurar transações e contratos HTTP

- [ ] Aplicar `@Transactional` na criação.
- [ ] Aplicar `@Transactional` na atualização.
- [ ] Aplicar `@Transactional` na exclusão.
- [ ] Retornar `200` nas consultas.
- [ ] Retornar `201` na criação.
- [ ] Informar o header `Location` na criação.
- [ ] Retornar `204` na exclusão.
- [ ] Retornar `404` para recurso ausente.
- [ ] Confirmar o PUT com uma nova consulta.
- [ ] Confirmar o DELETE com uma consulta retornando `404`.

### 4.4 Executar o roteiro do CRUD

- [ ] POST cria a tarefa.
- [ ] GET recupera a tarefa.
- [ ] PUT altera a tarefa.
- [ ] Segundo GET comprova a alteração.
- [ ] DELETE remove a tarefa.
- [ ] GET final retorna `404`.
- [ ] Registrar todas as respostas como evidência.

---

## 5. Task 4 — REST Client, profiles e tolerância a falhas

**Responsável:** Integrante 4  
**Requisitos principais:** 06, 07 e 09  
**Dependência:** contratos e projeto preparado

### 5.1 Criar a integração HTTP

- [ ] Criar a interface do REST Client.
- [ ] Usar as anotações do Quarkus REST Client.
- [ ] Definir o contrato de prioridade.
- [ ] Configurar a URL por propriedade.
- [ ] Não deixar URL fixa em classe Java.
- [ ] Criar ou disponibilizar o mock HTTP local.
- [ ] Documentar como iniciar o mock.
- [ ] Confirmar que a comunicação é HTTP real.

### 5.2 Configurar profiles

- [ ] Configurar o profile `dev`.
- [ ] Configurar o profile `prod` ou `staging`.
- [ ] Externalizar a URL do serviço externo.
- [ ] Demonstrar sobrescrita por variável de ambiente.
- [ ] Documentar os comandos de execução de cada profile.

### 5.3 Implementar resiliência

- [ ] Aplicar `@Retry`.
- [ ] Definir quantidade e intervalo de tentativas.
- [ ] Aplicar `@Fallback` ou mecanismo equivalente.
- [ ] Definir uma resposta de fallback controlada.
- [ ] Executar o cenário com o mock disponível.
- [ ] Desligar o mock.
- [ ] Confirmar as tentativas de retry.
- [ ] Confirmar a resposta de fallback.
- [ ] Registrar os cenários UP e DOWN no Postman ou em `docs/evidencias.md`.

---

## 6. Task 5 — Health, métricas, erros e evidências

**Responsável:** Integrante 5  
**Requisitos principais:** 02, 08, 10, 15 e 16  
**Dependência:** validação, CRUD e contratos HTTP integrados

### 6.1 Implementar health checks

- [ ] Criar o health check de liveness.
- [ ] Criar o health check de readiness.
- [ ] Garantir finalidades distintas.
- [ ] Confirmar `/q/health/live`.
- [ ] Confirmar `/q/health/ready`.
- [ ] Criar condição controlável para readiness ficar `DOWN`.
- [ ] Documentar quando cada health check deve ficar `DOWN`.

### 6.2 Implementar métrica de negócio

- [ ] Adicionar Micrometer.
- [ ] Criar contador de tarefas criadas ou métrica equivalente.
- [ ] Consultar `/q/metrics` antes da operação.
- [ ] Criar uma tarefa.
- [ ] Consultar `/q/metrics` depois da operação.
- [ ] Confirmar que o valor variou.
- [ ] Registrar a evidência antes/depois.

### 6.3 Padronizar erros

- [ ] Criar o modelo de resposta de erro.
- [ ] Incluir `codigo`.
- [ ] Incluir `mensagem`.
- [ ] Incluir `timestamp`.
- [ ] Criar ExceptionMapper para a exceção de negócio.
- [ ] Criar tratamento para recurso não encontrado.
- [ ] Tratar erros de Bean Validation.
- [ ] Evitar `try/catch` repetido nos Resources.
- [ ] Demonstrar resposta `400`.
- [ ] Demonstrar resposta `404`.
- [ ] Confirmar que os dois erros seguem formato consistente.

### 6.4 Consolidar evidências

- [ ] Receber evidências de cada integrante.
- [ ] Atualizar `docs/evidencias.md`.
- [ ] Associar cada requisito a código, teste ou requisição.
- [ ] Atualizar o checklist final dos 16 requisitos.
- [ ] Confirmar que nenhuma evidência depende de configuração não documentada.

---

## 7. Integração e validação final

### 7.1 Testes

- [ ] Executar testes unitários da regra de negócio.
- [ ] Executar testes de validação.
- [ ] Executar testes do Repository.
- [ ] Executar testes dos endpoints.
- [ ] Executar o fluxo completo de CRUD.
- [ ] Testar recurso inexistente.
- [ ] Testar transição de negócio inválida.
- [ ] Testar payload inválido.
- [ ] Testar integração externa disponível.
- [ ] Testar integração externa indisponível.
- [ ] Testar retry e fallback.
- [ ] Testar liveness e readiness.
- [ ] Testar métrica antes e depois.
- [ ] Corrigir falhas e repetir os testes afetados.

### 7.2 Checklist dos 16 requisitos

- [ ] 01 — Recursos e métodos HTTP.
- [ ] 02 — Códigos de status e contratos.
- [ ] 03 — Lombok aplicado conscientemente.
- [ ] 04 — API Quarkus/JAX-RS.
- [ ] 05 — CDI e regra de negócio.
- [ ] 06 — Consumo de API externa.
- [ ] 07 — Configurações e profiles.
- [ ] 08 — Liveness e readiness.
- [ ] 09 — Tolerância a falhas.
- [ ] 10 — Métrica de negócio.
- [ ] 11 — Entidade JPA.
- [ ] 12 — CRUD com Panache Repository.
- [ ] 13 — Transações e atualização.
- [ ] 14 — Bean Validation.
- [ ] 15 — ExceptionMapper personalizado.
- [ ] 16 — Resposta de erro padronizada.

---

## 8. Documentação e entrega

- [ ] Criar ou atualizar `README.md`.
- [ ] Informar nome e integrantes da equipe.
- [ ] Descrever o problema resolvido.
- [ ] Informar versões de Java e Quarkus.
- [ ] Documentar como iniciar a API.
- [ ] Documentar como iniciar o mock.
- [ ] Documentar o banco H2.
- [ ] Documentar profiles e variáveis de ambiente.
- [ ] Documentar todos os endpoints.
- [ ] Incluir exemplos de requisições.
- [ ] Incluir a tabela de evidências dos 16 requisitos.
- [ ] Criar `docs/evidencias.md`.
- [ ] Criar `postman/projeto-final.postman_collection.json`.
- [ ] Conferir `pom.xml`.
- [ ] Conferir `src/main`.
- [ ] Conferir `src/test`.
- [ ] Conferir `.gitignore`.
- [ ] Manter `docker-compose.yml` apenas se necessário.
- [ ] Verificar que não existem segredos no repositório.
- [ ] Verificar que não existem URLs indevidamente fixas.

---

## 9. Ensaio da apresentação

- [ ] Apresentar o domínio escolhido.
- [ ] Mostrar as camadas `Resource → Service → Repository`.
- [ ] Demonstrar o CRUD.
- [ ] Demonstrar os status HTTP.
- [ ] Demonstrar a regra de negócio via CDI.
- [ ] Demonstrar consumo do serviço externo.
- [ ] Desligar o mock e demonstrar retry/fallback.
- [ ] Demonstrar profiles e variável de ambiente.
- [ ] Demonstrar liveness.
- [ ] Demonstrar readiness normal e `DOWN`.
- [ ] Demonstrar métrica antes e depois.
- [ ] Enviar entrada inválida.
- [ ] Demonstrar `ExceptionMapper`.
- [ ] Demonstrar respostas JSON padronizadas.
- [ ] Confirmar que todas as evidências estão disponíveis.

---

## 10. Ordem resumida de execução

1. Fechar os contratos e decisões compartilhadas.
2. Preparar o projeto Quarkus e o fluxo de trabalho.
3. Implementar Resource, DTOs e rotas da Task 1.
4. Implementar Service, regra de negócio e validação da Task 2.
5. Implementar Entity, Repository, CRUD e transações da Task 3.
6. Implementar REST Client, mock, profiles e resiliência da Task 4.
7. Implementar health, métricas e tratamento de erros da Task 5.
8. Integrar todas as tasks.
9. Executar testes e o roteiro completo do Postman.
10. Consolidar as evidências dos 16 requisitos.
11. Finalizar README e arquivos de entrega.
12. Executar o ensaio final da apresentação.

## Critério de conclusão do projeto

O projeto só deve ser considerado concluído quando:

- os 16 requisitos estiverem marcados como validados;
- o CRUD estiver reproduzível;
- os testes estiverem passando;
- a integração externa tiver cenários UP e DOWN;
- health, métricas, validação e erros estiverem demonstráveis;
- `README.md`, coleção Postman e `docs/evidencias.md` estiverem atualizados;
- o ensaio completo puder ser executado por qualquer integrante da equipe.
