# Inventário de Endpoints HTTP — Projeto JVM Labs

## Identificação

**Projeto:** Projeto-jvm-labs  
**Módulo:** laboratorio-api  
**Tecnologias:** Java 21, Spring Boot, Maven e HTTP/JSON  
**Endereço-base:** [http://localhost:8080](http://localhost:8080)  
**Situação:** APIs 01 a 06 implementadas e testadas no ambiente local  
**Última etapa documentada:** 3.15 — Virtual Threads

## Endpoints implementados

| Identificação | Método | Endpoint | Objetivo |
| --- | --- | --- | --- |
| API-01 | GET | [/api/status](http://localhost:8080/api/status) | Consultar informações da JVM e da aplicação |
| API-02 | GET | [/api/threads/executar](http://localhost:8080/api/threads/executar) | Executar uma thread real |
| API-03 | GET | [/api/threads/race-condition](http://localhost:8080/api/threads/race-condition) | Demonstrar concorrência e sincronização |
| API-04 | GET | [/api/threads/reentrant-lock](http://localhost:8080/api/threads/reentrant-lock) | Demonstrar ReentrantLock e tryLock() |
| API-05 | GET | [/api/threads/atomic-integer](http://localhost:8080/api/threads/atomic-integer) | Comparar incrementos concorrentes com volatile e AtomicInteger |
| API-06 | GET | [/api/threads/virtual-threads](http://localhost:8080/api/threads/virtual-threads) | Comparar a execução de tarefas com Platform Threads e Virtual Threads |
| API-07 | GET | [http://localhost:8080/api/threads/diagnostico](http://localhost:8080/api/threads/diagnostico) | Identificar estados e informações das pilhas de duas threads reais |

> Os links utilizam `localhost` e funcionam somente quando a aplicação estiver executando no computador que recebe a requisição, na porta 8080.

## Procedimento de teste

1. Iniciar a classe `LaboratorioApiApplication` no IntelliJ IDEA.
2. Confirmar que o servidor Spring Boot está operando na porta `8080`.
3. Executar a requisição HTTP correspondente no navegador ou por meio de um arquivo `.http` no IntelliJ IDEA.
4. Verificar o código de resposta HTTP e o conteúdo JSON.
5. Comparar o resultado com o comportamento esperado para cada laboratório.
6. Registrar eventuais erros, resultados inesperados ou variações entre execuções.
7. Quando disponível, executar o mesmo laboratório na interface principal: [http://localhost:8080/](http://localhost:8080/).

## Arquivos de teste HTTP

Manter os arquivos abaixo na pasta `laboratorio-api/testes-api/`:

```text
laboratorio-api/
└── testes-api/
    ├── README.md
    ├── 01-status.http
    ├── 02-executar-thread.http
    ├── 03-race-condition.http
    ├── 04-reentrant-lock.http
    ├── 05-atomic-integer.http
    └── 06-virtual-threads.http
```

Os nomes acima representam a organização recomendada do inventário; conferir quais arquivos `.http` já foram efetivamente criados antes de completar a pasta.

### API-06 — Exemplo de requisição

Conteúdo sugerido para `06-virtual-threads.http`:

```http
### API-06 — Virtual Threads Java 21
GET http://localhost:8080/api/threads/virtual-threads
Accept: application/json
```

## Critérios de validação

- Resposta HTTP `200` nas requisições bem-sucedidas.
- JSON contendo os campos esperados para cada endpoint.
- Execução efetiva de código Java na JVM, e não apenas uma simulação visual no navegador.
- Coerência entre os dados retornados pela API e os indicadores da interface.
- Ausência de exceções inesperadas no console da aplicação.
- Repetibilidade funcional dos testes, considerando a variação natural dos resultados concorrentes.

## Registro de validação — API-05: volatile e AtomicInteger

**Endpoint:** [http://localhost:8080/api/threads/atomic-integer](http://localhost:8080/api/threads/atomic-integer)  
**Etapa:** 3.14  
**Resultado:** execução concluída com sucesso e apresentação dos indicadores na interface web.

| Indicador | Resultado observado |
| --- | ---: |
| Threads | 8 |
| Incrementos por thread | 100.000 |
| Resultado esperado | 800.000 |
| Contador volatile | 416.210 |
| Contador AtomicInteger | 800.000 |
| Atualizações perdidas no contador volatile | 383.790 |
| AtomicInteger correto | `true` |
| Execução | `Concluída` |

**Interpretação:** o modificador `volatile` estabelece garantias de visibilidade, mas não torna a expressão `contador++` atômica. Já `AtomicInteger.incrementAndGet()` realiza o incremento de forma atômica. Os valores do contador `volatile` podem variar entre execuções.

## Registro de validação — API-06: Virtual Threads

**Endpoint:** [http://localhost:8080/api/threads/virtual-threads](http://localhost:8080/api/threads/virtual-threads)  
**Etapa:** 3.15  
**Resultado:** execução concluída com sucesso e apresentação dos seis indicadores e do JSON na interface web.

| Indicador | Resultado observado |
| --- | ---: |
| Total de tarefas | 100 |
| Espera simulada por tarefa | 100 ms |
| Platform Threads (tamanho do pool) | 10 |
| Virtual Threads criadas por tarefa | 100 |
| Tempo Platform Threads | 1.037 ms |
| Tempo Virtual Threads | 110 ms |
| Execução | `Concluída` |

**Interpretação:** na execução registrada, o executor com Virtual Threads terminou aproximadamente **9,43 vezes mais rápido**. Essa diferença decorre principalmente da configuração de concorrência: o executor tradicional possui um pool fixo de 10 threads, enquanto o executor virtual cria uma thread por tarefa. Portanto, o resultado **não constitui benchmark de desempenho em condições equivalentes**. Os tempos variam entre execuções e ambientes.

## Observações técnicas

Os endpoints de laboratório utilizam o método `GET` para facilitar os testes didáticos. Como alguns deles iniciam processamento, em uma API de produção seria recomendável avaliar o uso de `POST` para operações com efeitos ou execução sob demanda.

Os resultados de experimentos concorrentes podem variar em função do escalonamento das threads, da carga da máquina e do ambiente de execução.

As Virtual Threads, recurso definitivo do Java 21 descrito na **JEP 444**, são especialmente úteis em cenários com muitas tarefas bloqueantes, mas não aceleram automaticamente operações intensivas de CPU.

**Referência técnica:** [OpenJDK — JEP 444: Virtual Threads](https://openjdk.org/jeps/444).

## Evolução do projeto

As APIs 01 a 06 estão registradas neste inventário. A próxima identificação disponível é **API-07**, a ser atribuída somente quando um novo endpoint for efetivamente implementado.

A próxima fase de estudo proposta é a **Etapa 3.16 — Diagnóstico e Observabilidade de Threads**. Essa etapa poderá incluir ferramentas de diagnóstico da JVM; não pressupõe, por si só, a criação de um novo endpoint HTTP.

Ao implementar um novo controller ou endpoint:

1. Atualizar a tabela de endpoints.
2. Criar o respectivo arquivo `.http`, quando aplicável.
3. Executar e documentar o teste.
4. Registrar os campos e resultados relevantes.
5. Preservar uma cópia de segurança dos arquivos alterados.

## Autoria e finalidade

Material didático autoral desenvolvido para estudo, experimentação técnica e composição de portfólio em Java Back-End. A utilização ou redistribuição por terceiros deve observar os direitos autorais aplicáveis e as permissões expressamente concedidas pelo titular.
