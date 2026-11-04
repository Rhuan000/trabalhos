# PROJETO FINAL | INTRODUÇÃO AO QUARKUS | EQUIPE COM 5 INTEGRANTES

## PLANO DE EXECUÇÃO
**Projeto Final**  
**Introdução ao Quarkus**  
Divisão revisada para 5 integrantes, integração e cadastro das tasks no GitHub

### Decisão recomendada
Manter o domínio **Controle de Tarefas**, banco H2 e mock HTTP local. Com cinco integrantes, a antiga task de qualidade foi separada: um integrante cuida de observabilidade e resiliência operacional, enquanto outro assume validação, erros e consolidação das evidências.

---

## 1. Estratégia da nova divisão

O trabalho continua contendo 16 requisitos avaliativos. Como 16 não é divisível igualmente por 5, a divisão não deve ser feita apenas pela quantidade de requisitos. O equilíbrio abaixo considera esforço, dependências e risco de integração. Cada integrante possui uma área principal clara e deve entregar código, testes, requisições no Postman e evidências.

### Por que esta divisão é melhor
Ela separa contrato HTTP, domínio, persistência, integração externa e qualidade operacional. Isso reduz alterações simultâneas nos mesmos arquivos e permite que todos contribuam com uma parte demonstrável.

---

## 2. Arquitetura recomendada

| Elemento | Decisão |
| :--- | :--- |
| **Domínio** | Controle de Tarefas |
| **Campos** | `id`, `titulo`, `descricao`, `status` e `prioridade` |
| **Banco** | H2 em desenvolvimento e teste |
| **Regra central** | Tarefa CANCELADA não pode mudar para CONCLUIDA |
| **Integração** | Mock HTTP local de prioridade |
| **Métrica** | Contador de tarefas criadas |
| **Erro padrão** | `codigo`, `mensagem`, `timestamp` e detalhes quando necessário |

---

## 3. Resumo da divisão para 5 pessoas

| Task | Responsabilidade | Requisitos principais | Carga |
| :--- | :--- | :--- | :--- |
| **1** | API REST, DTOs e contrato HTTP | 01, 03, 04 | Média |
| **2** | CDI, serviço, regra de negócio e validação | 05, 14 | Média/alta |
| **3** | Persistência, CRUD, transações e status | 11, 12, 13 + parte do 02 | Alta |
| **4** | REST Client, profiles e tolerância a falhas | 06, 07, 09 | Alta |
| **5** | Health, métricas, erros e evidências | 08, 10, 15, 16 + fechamento do 02 | Alta |

### Observação sobre equilíbrio
A **Task 2** possui menos requisitos numéricos, mas concentra a lógica central e os testes de regra. A **Task 5** possui mais requisitos, porém são componentes transversais menores e inclui a responsabilidade organizacional de consolidar evidências, não de produzi-las sozinho.

---

## Detalhamento das Tasks

### TASK 1 | API REST, DTOs e contrato HTTP
**Integrante 1 | Requisitos 01, 03 e 04**

#### Objetivo
Definir o contrato público da API que será consumido pelo Postman e utilizado pelas demais camadas.

#### Escopo
- Criar `TarefaRequest` e `TarefaResponse`, separados da Entity.
- Criar `TarefaResource` com Jakarta REST.
- Disponibilizar `GET /tarefas`, `GET /tarefas/{id}`, `POST /tarefas`, `PUT /tarefas/{id}` e `DELETE /tarefas/{id}`.
- Configurar consumo e produção de JSON.
- Aplicar Lombok conscientemente nos DTOs.
- Definir exemplos de payload para o Postman.

#### Critérios de aceite
- [ ] Rotas REST sem verbos.
- [ ] Resource sem regra de negócio e sem acesso direto ao banco.
- [ ] Contratos compartilhados com os integrantes 2 e 3.
- [ ] Endpoints compilando e devolvendo JSON.

#### Dependência principal
A Task 1 publica os contratos primeiro. Mudanças posteriores em DTOs ou rotas devem ser alinhadas com Tasks 2, 3 e 5.

---

### TASK 2 | CDI, regra de negócio e validação
**Integrante 2 | Requisitos 05 e 14**

#### Objetivo
Implementar a camada de domínio, centralizar as regras e validar entradas antes da persistência.

#### Escopo
- Criar `TarefaService` como bean CDI, por exemplo `@ApplicationScoped`.
- Fazer o Resource delegar operações ao Service.
- Implementar a regra: tarefa CANCELADA não pode ser CONCLUIDA.
- Criar exceção de negócio para transição inválida, em alinhamento com a Task 5.
- Aplicar `@Valid` nos endpoints de entrada.
- Usar pelo menos duas restrições distintas, como `@NotBlank` e `@Size`.
- Criar testes da regra e das validações.

#### Critérios de aceite
- [ ] Regra reproduzível por teste ou Postman.
- [ ] Não há lógica de domínio no Resource.
- [ ] Payload inválido é rejeitado.
- [ ] Service integrado ao Repository da Task 3.

---

### TASK 3 | Persistência, CRUD e transações
**Integrante 3 | Requisitos 11, 12, 13 e parte do 02**

#### Objetivo
Garantir CRUD real sobre banco relacional e consistência das operações de escrita.

#### Escopo
- Criar `TarefaEntity` com `@Entity` e identificador.
- Criar `TarefaRepository` com `PanacheRepository`.
- Configurar H2 e Hibernate ORM.
- Implementar consultas, criação, atualização e exclusão.
- Aplicar `@Transactional` nas operações de escrita.
- Apoiar status 200, 201, 204 e 404 e Location na criação.
- Demonstrar POST, GET, PUT, GET, DELETE e GET 404.

#### Critérios de aceite
- [ ] DTO e Entity separados.
- [ ] PUT confirmado por nova consulta.
- [ ] DELETE confirmado por 404.
- [ ] Banco sobe sem instalação externa.

#### Roteiro obrigatório
POST cria; GET recupera; PUT altera; segundo GET comprova; DELETE remove; GET final retorna 404.

---

### TASK 4 | REST Client, profiles e tolerância a falhas
**Integrante 4 | Requisitos 06, 07 e 09**

#### Objetivo
Entregar integração HTTP reproduzível e comportamento controlado quando o serviço externo falhar.

#### Escopo
- Criar interface Quarkus REST Client para prioridade.
- Disponibilizar mock HTTP local reproduzível.
- Externalizar a URL no `application.properties`.
- Configurar `%dev` e `%prod` ou `%staging`.
- Demonstrar sobrescrita via variável de ambiente.
- Aplicar `@Retry` e `@Fallback`.
- Demonstrar serviço externo UP e DOWN.

#### Critérios de aceite
- [ ] Integração usa HTTP real.
- [ ] Nenhuma URL fica fixa em classe Java.
- [ ] Fallback responde com o mock desligado.
- [ ] Cenários estão documentados no Postman ou `evidencias.md`.

---

### TASK 5 | Health, métricas, erros e evidências
**Integrante 5 | Requisitos 08, 10, 15, 16 e fechamento do 02**

#### Objetivo
Padronizar operação e erros da API, medir comportamento de negócio e coordenar a validação final.

#### Escopo
- Criar liveness e readiness com finalidades distintas.
- Criar condição controlável para readiness ficar DOWN.
- Adicionar métrica Micrometer e demonstrar variação em `/q/metrics`.
- Criar Exception Mapper para exceção de negócio e recurso não encontrado.
- Padronizar JSON com `codigo`, `mensagem` e `timestamp`.
- Demonstrar padrão em pelo menos 400 e 404.
- Validar status 200, 201, 204, 400 e 404.
- Consolidar `docs/evidencias.md` e collection do Postman recebidos do grupo.

#### Critérios de aceite
- [ ] Health endpoints respondem e a equipe sabe explicar o resultado.
- [ ] Métrica muda após a operação correspondente.
- [ ] Erros 400 e 404 compartilham o mesmo formato.
- [ ] Cada requisito 01 a 16 possui evidência associada.

#### Importante
O integrante 5 consolida evidências, mas cada integrante continua responsável por fornecer registros, prints ou comandos de demonstração da própria task.

---

## 4. Ordem de execução e integração

1. Alinhamento de 30 a 45 minutos para fechar modelo Tarefa, DTOs, status e contrato do mock.
2. Criar repositório, projeto Quarkus, labels, milestone, projeto e cinco issues.
3. Task 1 publica os contratos HTTP.
4. Tasks 2 e 3 integram Service, regra, Entity e Repository.
5. Task 4 integra o serviço externo sem bloquear o CRUD.
6. Task 5 adiciona health, métricas e tratamento transversal após os contratos estabilizarem.
7. Abrir pull requests pequenos e integrar progressivamente.
8. Executar ensaio completo no Postman antes da apresentação.

### Branches sugeridas
- `feature/task-01-api-contratos`
- `feature/task-02-negocio-validacao`
- `feature/task-03-persistencia-crud`
- `feature/task-04-integracao-resiliencia`
- `feature/task-05-observabilidade-erros`

### Regra de integração
Cada pull request deve incluir `Closes #N`, testes da alteração, evidência mínima e apenas mudanças relacionadas à issue.

---

## 5. Como cadastrar as 5 tasks no GitHub

### 5.1 Preparar Issues, labels e milestone
1. No repositório, abra **Issues**. Se não estiver disponível, habilite em **Settings > General > Features**.
2. Em **Issues > Labels**, crie: `backend`, `database`, `integration`, `observability`, `validation`, `documentation` e `priority: high`.
3. Em **Issues > Milestones**, crie **Projeto Final - Quarkus** e informe a data de entrega.
4. Na descrição do milestone, registre: *Conclusão dos 16 requisitos e preparação da demonstração.*

### 5.2 Criar o projeto em quadro
1. Abra **Projects** no repositório ou na organização.
2. Crie um projeto com o modelo **Board**.
3. Nomeie como **Projeto Final - Quarkus**.
4. Configure os estados: **Backlog**, **Ready**, **In progress**, **Review** e **Done**.
5. Adicione as cinco issues ao quadro.

### 5.3 Criar e atribuir as issues
1. Clique em **New issue**.
2. Use o título e o texto pronto da seção 6.
3. Selecione o responsável em **Assignees**.
4. Aplique as labels recomendadas.
5. Associe o milestone **Projeto Final - Quarkus**.
6. Adicione a issue ao **Project**.
7. Clique em **Submit new issue** e repita para as cinco tasks.

---

## 6. Textos prontos para copiar nas Issues

```text
TASK 1 - API REST, DTOs e contrato HTTP

Objetivo: Definir o contrato público da API que será consumido pelo Postman e utilizado pelas demais camadas.

Escopo:
- [ ] Criar TarefaRequest e TarefaResponse, separados da Entity.
- [ ] Criar TarefaResource com Jakarta REST.
- [ ] Disponibilizar GET /tarefas, GET /tarefas/{id}, POST /tarefas, PUT /tarefas/{id} e DELETE /tarefas/{id}.
- [ ] Configurar consumo e produção de JSON.
- [ ] Aplicar Lombok conscientemente nos DTOs.
- [ ] Definir exemplos de payload para o Postman.

Critérios de aceite:
- [ ] Rotas REST sem verbos.
- [ ] Resource sem regra de negócio e sem acesso direto ao banco.
- [ ] Contratos compartilhados com os integrantes 2 e 3.
- [ ] Endpoints compilando e devolvendo JSON.

Labels: backend, priority: high
Branch: feature/task-01-api-contratos
```

```text
TASK 2 - CDI, regra de negócio e validação

Objetivo: Implementar a camada de domínio, centralizar as regras e validar entradas antes da persistência.

Escopo:
- [ ] Criar TarefaService como bean CDI, por exemplo @ApplicationScoped.
- [ ] Fazer o Resource delegar operações ao Service.
- [ ] Implementar a regra: tarefa CANCELADA não pode ser CONCLUIDA.
- [ ] Criar exceção de negócio para transição inválida, em alinhamento com a Task 5.
- [ ] Aplicar @Valid nos endpoints de entrada.
- [ ] Usar pelo menos duas restrições distintas, como @NotBlank e @Size.
- [ ] Criar testes da regra e das validações.

Critérios de aceite:
- [ ] Regra reproduzível por teste ou Postman.
- [ ] Não há lógica de domínio no Resource.
- [ ] Payload inválido é rejeitado.
- [ ] Service integrado ao Repository da Task 3.

Labels: backend, validation
Branch: feature/task-02-negocio-validacao
```

```text
TASK 3 - Persistência, CRUD e transações

Objetivo: Garantir CRUD real sobre banco relacional e consistência das operações de escrita.

Escopo:
- [ ] Criar TarefaEntity com @Entity e identificador.
- [ ] Criar TarefaRepository com PanacheRepository.
- [ ] Configurar H2 e Hibernate ORM.
- [ ] Implementar consultas, criação, atualização e exclusão.
- [ ] Aplicar @Transactional nas operações de escrita.
- [ ] Apoiar status 200, 201, 204 e 404 e Location na criação.
- [ ] Demonstrar POST, GET, PUT, GET, DELETE e GET 404.

Critérios de aceite:
- [ ] DTO e Entity separados.
- [ ] PUT confirmado por nova consulta.
- [ ] DELETE confirmado por 404.
- [ ] Banco sobe sem instalação externa.

Labels: database, backend
Branch: feature/task-03-persistencia-crud
```

```text
TASK 4 - REST Client, profiles e tolerância a falhas

Objetivo: Entregar integração HTTP reproduzível e comportamento controlado quando o serviço externo falhar.

Escopo:
- [ ] Criar interface Quarkus REST Client para prioridade.
- [ ] Disponibilizar mock HTTP local reproduzível.
- [ ] Externalizar a URL no application.properties.
- [ ] Configurar %dev e %prod ou %staging.
- [ ] Demonstrar sobrescrita via variável de ambiente.
- [ ] Aplicar @Retry e @Fallback.
- [ ] Demonstrar serviço externo UP e DOWN.

Critérios de aceite:
- [ ] Integração usa HTTP real.
- [ ] Nenhuma URL fica fixa em classe Java.
- [ ] Fallback responde com o mock desligado.
- [ ] Cenários estão documentados no Postman ou evidencias.md.

Labels: integration, priority: high
Branch: feature/task-04-integracao-resiliencia
```

```text
TASK 5 - Health, métricas, erros e evidências

Objetivo: Padronizar operação e erros da API, medir comportamento de negócio e coordenar a validação final.

Escopo:
- [ ] Criar liveness e readiness com finalidades distintas.
- [ ] Criar condição controlável para readiness ficar DOWN.
- [ ] Adicionar métrica Micrometer e demonstrar variação em /q/metrics.
- [ ] Criar Exception Mapper para exceção de negócio e recurso não encontrado.
- [ ] Padronizar JSON com codigo, mensagem e timestamp.
- [ ] Demonstrar padrão em pelo menos 400 e 404.
- [ ] Validar status 200, 201, 204, 400 e 404.
- [ ] Consolidar docs/evidencias.md e collection do Postman recebidos do grupo.

Critérios de aceite:
- [ ] Health endpoints respondem e a equipe sabe explicar o resultado.
- [ ] Métrica muda após a operação correspondente.
- [ ] Erros 400 e 404 compartilham o mesmo formato.
- [ ] Cada requisito 01 a 16 possui evidência associada.

Labels: observability, validation, documentation
Branch: feature/task-05-observabilidade-erros
```

---

## 7. Fluxo de trabalho de cada integrante

1. Mover a issue para **In progress**.
2. Atualizar `main` local e criar a branch da task.
3. Implementar somente o escopo da issue.
4. Executar testes e requisições correspondentes no Postman.
5. Fazer commits pequenos e descritivos.
6. Enviar a branch e abrir Pull Request para `main`.
7. Incluir `Closes #N` na descrição do Pull Request.
8. Solicitar revisão de pelo menos um colega.
9. Corrigir comentários e atualizar a evidência.
10. Após o merge, confirmar o fechamento da issue e mover para **Done**.

### Checklist do Pull Request
- [ ] Projeto compila.
- [ ] Testes passam.
- [ ] Mudanças estão limitadas à issue.
- [ ] Postman foi atualizado quando aplicável.
- [ ] Evidência foi adicionada.
- [ ] Não existem segredos ou URLs fixas.
- [ ] README foi atualizado quando necessário.
- [ ] Issue foi referenciada com `Closes #N`.

---

## 8. Checklist final dos 16 requisitos

| Req. | Evidência mínima | Validado |
| :--- | :--- | :---: |
| **01** | Métodos GET, POST, PUT e DELETE | [ ] |
| **02** | Status 200, 201, 204, 400, 404 e Location | [ ] |
| **03** | Lombok aplicado conscientemente | [ ] |
| **04** | Quarkus REST/JAX-RS e JSON | [ ] |
| **05** | CDI e regra de negócio | [ ] |
| **06** | REST Client consumindo HTTP | [ ] |
| **07** | Configuração externalizada e dois profiles | [ ] |
| **08** | Liveness e readiness distintos | [ ] |
| **09** | Retry e fallback demonstrados | [ ] |
| **10** | Métrica de negócio variando | [ ] |
| **11** | Entity separada do DTO | [ ] |
| **12** | CRUD com Panache Repository | [ ] |
| **13** | Transações e PUT confirmado por GET | [ ] |
| **14** | `@Valid` e duas restrições | [ ] |
| **15** | Exceção de negócio e ExceptionMapper | [ ] |
| **16** | Erro JSON padronizado em dois cenários | [ ] |

---

## 9. Matriz de integração entre os integrantes

| Entrega de | Recebida por | O que precisa ser alinhado |
| :--- | :--- | :--- |
| **Task 1** | Tasks 2, 3 e 5 | DTOs, rotas e códigos HTTP |
| **Task 2** | Tasks 3 e 5 | Assinaturas do Service, exceções e validações |
| **Task 3** | Tasks 1, 2 e 5 | Repository, Entity, transações e 404 |
| **Task 4** | Tasks 2 e 5 | Contrato do cliente, fallback e configuração |
| **Task 5** | Todo o grupo | Formato de erro, health, métricas e evidências |

---

## 10. Estrutura final da entrega

- `pom.xml`
- `README.md`
- `src/main`
- `src/test`
- `docs/evidencias.md`
- `postman/projeto-final.postman_collection.json`
- `.gitignore`
- `docker-compose.yml` *(somente se necessário)*

---

### Resultado esperado
Os cinco integrantes terão responsabilidades próprias, branches independentes e critérios de aceite objetivos. A equipe continuará cobrindo os 16 requisitos e terá menor risco de conflito durante a integração.