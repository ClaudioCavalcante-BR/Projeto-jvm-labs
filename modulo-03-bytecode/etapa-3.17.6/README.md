# Proposta de atualização do README da raiz — Projeto-jvm-labs

> **ATENÇÃO:** este arquivo é uma proposta de seção para incorporar ao README original da raiz. Não é uma substituição integral: o `README.md` efetivo da raiz ainda precisa ser fornecido e conferido. Os READMEs disponíveis nos materiais anteriores documentam a interface e o laboratório API, mas não comprovam o conteúdo atual do README raiz.

## Projeto JVM Labs — visão dos módulos

Projeto educacional e de portfólio com experimentos de Java 21, JVM, concorrência, Bytecode e observabilidade.

| Módulo | Foco | Entregas conhecidas |
|---|---|---|
| 1 — JVM | Arquitetura, JDK/JRE, JEPs e observabilidade | Estudos e diagnóstico com ferramentas do JDK |
| 2 — Threads | Concorrência, sincronização e gerenciamento de threads | Exemplos Java e endpoints HTTP |
| 3 — Bytecode Explorer | Relação entre código Java, Bytecode e comportamento da JVM | Experimentos 3.17.1 a 3.17.6; interface `bytecode.html` |

## Módulo 3 — Experimentos

| Experimento | Tema | API configurada no JavaScript fornecido |
|---|---|---|
| 3.17.1-A | Aritmética | `/api/bytecode/aritmetica` |
| 3.17.1-B | Operações matemáticas | `/api/bytecode/matematica` |
| 3.17.2 | Decisões | `/api/bytecode/decisao` |
| 3.17.3-A | Race Condition | `/api/bytecode/race-condition` |
| 3.17.3-B | `synchronized` | `/api/bytecode/race-condition-sincronizada` |
| 3.17.4 | Objetos | `/api/bytecode/objetos` |
| 3.17.5 | Monitores | `/api/bytecode/sincronizacao` |
| 3.17.6 | Deadlock | `/api/bytecode/deadlock` |

As rotas acima foram identificadas no `bytecode.js` fornecido; **não equivalem a um novo teste HTTP de todas as rotas**.

## Experimento 3.17.6 — Deadlock

A classe `DeadlockMonitor.java` usa dois monitores (`LOCK_1`, `LOCK_2`) e duas threads que os adquirem em ordem inversa. A execução separada foi diagnosticada com `jcmd` e `jstack`. A inspeção com `javap` mostrou `monitorenter` nos offsets 5 e 36 em ambos os métodos; a segunda aquisição resulta em espera circular. O endpoint `GET /api/bytecode/deadlock` retorna o Bytecode dos métodos `executarThreadA()` e `executarThreadB()` para a interface educacional.

### Execução e verificação

A partir da raiz do projeto, com Java 21 e Maven disponíveis:

```bash
mvn -f laboratorio-api/pom.xml clean test
mvn -f laboratorio-api/pom.xml spring-boot:run -Dspring-boot.run.workingDirectory="$(pwd)"
```

A opção de diretório de trabalho é relevante porque o endpoint usa caminho relativo para localizar o arquivo `.class`. Confirme que `modulo-03-bytecode/etapa-3.17.6/DeadlockMonitor.class` existe e está compilado antes da consulta.

```bash
curl -i http://localhost:8080/api/bytecode/deadlock
```

Interface: `http://localhost:8080/bytecode.html`.

### Segurança operacional e limites

A API lê Bytecode; não executa a classe de deadlock em requisições HTTP. A visualização passo a passo é didática, não um rastreamento ao vivo do estado interno da JVM. Para reproduzir o deadlock real, use processo Java separado e encerre-o ao terminar o diagnóstico.

## Situação de validação

- Deadlock real diagnosticado com `jcmd` e `jstack`: **confirmado em testes anteriores**.
- Bytecode `javap`: **confirmado**.
- API Deadlock: **HTTP 200 confirmado em testes anteriores**.
- Interface Thread A e Thread B, passo 17 de 17: **confirmada por capturas**.
- Regressão de todos os experimentos anteriores após a última alteração: **pendente**.

## Documentos complementares

- `01_RELATORIO_TECNICO_3_17_6.md` — método, evidências, resultados e limitações.
- `03_CHECKLIST_REGRESSAO_3_17_6.md` — procedimentos de testes e registro de resultados.

## Referências

- https://docs.oracle.com/javase/specs/jvms/se21/html/
- https://docs.oracle.com/en/java/javase/21/docs/specs/man/jcmd.html
- https://docs.oracle.com/en/java/javase/21/docs/specs/man/jstack.html
- https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html

## Autoria e reutilização

O código, os textos e os materiais originais do projeto devem manter indicação de autoria e histórico de alterações. **Uma declaração de reserva de direitos não substitui a escolha e publicação de uma licença adequada.** Antes da publicação, decidir explicitamente a licença de uso, redistribuição e exploração comercial; não atribuir automaticamente uma licença a dependências ou referências de terceiros.
