# Projeto JVM Labs — Java 21, JVM, Threads, Bytecode Explorer e Portal Web

**Versão documental:** 28/10/2026  
**Autor:** Claudio Cavalcante de Almeida  
**Natureza:** projeto educacional e de portfólio, desenvolvido para estudo de Java Back-End; não homologado para produção.

## 1. Apresentação

O **Projeto-jvm-labs** integra laboratórios de Java 21, Java Virtual Machine (JVM), concorrência com Threads e análise de Bytecode. O back-end utiliza **Spring Boot 4.1.1** e expõe endpoints REST; a camada web utiliza HTML, CSS e JavaScript. O **Portal JVM Labs** centraliza o acesso aos três módulos de conteúdo, mantendo a página original e o Bytecode Explorer preservados.

O Bytecode Explorer apresenta instruções reais extraídas de arquivos `.class` com `javap`; a visualização da pilha e das variáveis locais é uma **interpretação didática em JavaScript**, não um depurador de execução nativa da JVM.

O **Projeto JVM Labs — Java 21, JVM, Threads e Bytecode Explorer** foi concebido e desenvolvido com o objetivo de proporcionar uma compreensão técnica, estruturada e prática sobre o funcionamento da linguagem de programação Java, com ênfase nos mecanismos internos responsáveis pela execução das aplicações.

Sua proposta é permitir que estudantes, desenvolvedores e profissionais de tecnologia compreendam não apenas como escrever códigos em Java, mas, principalmente, **como esses códigos são compilados, interpretados, executados e gerenciados pela Java Virtual Machine (JVM)**.

O projeto explora conceitos fundamentais, como JDK, JRE, carregamento de classes, gerenciamento de memória, Heap, Stack, execução de Bytecode, compilação Just-In-Time (JIT), Garbage Collector e mecanismos de diagnóstico e observabilidade da JVM.

Outro objetivo central é aprofundar o entendimento sobre **Threads e programação concorrente**, demonstrando como múltiplas tarefas podem ser executadas, coordenadas e sincronizadas. Os laboratórios abordam problemas e mecanismos importantes, como Race Condition, `synchronized`, `ReentrantLock`, `tryLock()`, `AtomicInteger`, Virtual Threads e Deadlock, relacionando os fundamentos teóricos aos comportamentos efetivamente observados durante a execução.

Por meio do **Bytecode Explorer**, o usuário também pode acompanhar instruções produzidas pelo compilador Java, compreender operações executadas pela JVM e observar representações didáticas da pilha de operandos, das variáveis locais e dos mecanismos de sincronização.

Para facilitar a aprendizagem, o projeto reúne interfaces web interativas, laboratórios executáveis e integração com APIs REST desenvolvidas em Spring Boot. O **Portal JVM Labs** centraliza a navegação entre os três módulos de estudos e seus experimentos.

**O principal benefício do Projeto JVM Labs é transformar conceitos muitas vezes abstratos em experiências práticas, visuais e verificáveis**, contribuindo para que o usuário compreenda com maior profundidade os fundamentos de Java, uma linguagem amplamente utilizada no desenvolvimento de aplicações corporativas e sistemas de Back-End.

A iniciativa também busca estimular o raciocínio técnico, a análise de problemas, a investigação do comportamento das aplicações e a adoção de boas práticas de desenvolvimento, fortalecendo a formação de profissionais que desejam compreender não apenas o resultado de um programa, mas os mecanismos que tornam sua execução possível.

Desenvolvido por **Claudio Cavalcante de Almeida**, o Projeto JVM Labs constitui um material educacional autoral e um trabalho de portfólio profissional, concebido para compartilhar conhecimento técnico e oferecer uma experiência de estudo progressiva, acessível e fundamentada em experimentos reais.

## 2. Organização dos módulos

- **Módulo 1 — JVM:** JDK/JRE, compilação, carregamento de classes, memória, interpretação/JIT, GC, JEPs e observabilidade com `jcmd`, `jstack` e JFR.
- **Módulo 2 — Threads:** ciclo de vida, `start`, `run`, race condition, `synchronized`, `ReentrantLock`, `tryLock`, `AtomicInteger`, Virtual Threads e diagnóstico.
- **Módulo 3 — Bytecode Explorer:** 3.17.1 aritmética; 3.17.2 decisões; 3.17.3-A/B race/synchronized; 3.17.4 objetos; 3.17.5 monitores; 3.17.6 deadlock.

### 2.1. Portal Web e integração REST

Para centralizar o acesso aos três módulos, desenvolvi um **Portal Web** utilizando **HTML, CSS e JavaScript**, com os arquivos estáticos servidos pela aplicação **Spring Boot**.

O **back-end Java 21 / Spring Boot 4.1.1** disponibiliza endpoints REST para consulta de informações e execução dos laboratórios implementados. O Portal centraliza a navegação, enquanto as interfaces dos laboratórios utilizam as APIs para conectar os conceitos estudados às funcionalidades da aplicação.

**Portal JVM Labs:** interface web desenvolvida com HTML, CSS e JavaScript, integrada ao back-end Java 21 / Spring Boot 4.1.1 por meio de **APIs REST**.

O Portal Web é um componente complementar aos **Módulos 1, 2 e 3** e **não constitui um quarto módulo de estudos**.

## 3. Ambiente e pré-requisitos

- **Java:** JDK 21 (ambiente validado: 21.0.10).
- **Back-end:** Spring Boot 4.1.1, Maven, módulo `laboratorio-api`.
- **Desenvolvimento:** IntelliJ IDEA e macOS no ambiente de validação; navegador atualizado.
- **Opcional:** Node.js para `node --check` dos arquivos JavaScript.
- **Porta HTTP local:** `8080`, conforme `application.yaml`.

## 4. Organização do repositório

A estrutura comentada do projeto, com a função de cada pasta e arquivo ao lado, está na **[seção 13 — Estrutura de pastas e responsabilidades](#13-estrutura-de-pastas-e-responsabilidades)**. A documentação destinada ao público e aos desenvolvedores está organizada em `docs/`, conforme as seções 11, 12 e 14.

## 5. Inicialização e navegação web

### 5.1. Build e execução por terminal

Na raiz do repositório:

```bash
java -version
mvn -f laboratorio-api/pom.xml clean test
mvn -f laboratorio-api/pom.xml spring-boot:run -Dspring-boot.run.workingDirectory="$(pwd)"
```

### 5.2. Endereços web

| Área | Endereço | Ação |
|---|---|---|
| Portal JVM Labs | `http://localhost:8080/portal.html` | Acesso centralizado aos três módulos |
| Módulo 1 — JVM | `http://localhost:8080/?modulo=jvm` | Seleção automática da seção JVM |
| Módulo 2 — Threads | `http://localhost:8080/?modulo=threads` | Seleção automática da seção Threads |
| Módulo 3 — Bytecode | `http://localhost:8080/bytecode.html` | Laboratórios 3.17.1–3.17.6 |
| Página inicial histórica | `http://localhost:8080/` | Mapa interativo sem filtro inicial |

Os Módulos 1 e 2 compartilham `index.html` e são selecionados pelos parâmetros `?modulo=jvm` e `?modulo=threads`. A opção `/` preserva o mapa original. Em todas as páginas integradas, há navegação de retorno ao Portal.

A antiga referência a `/teste-api.html` não integra a lista de acessos homologados nesta rodada; sua disponibilidade atual não foi retestada.

### 5.3. Duas configurações do IntelliJ IDEA

As duas configurações usam **a mesma classe principal** `br.com.jvmlabs.laboratorioapi.LaboratorioApiApplication`, o **JDK 21** e o módulo `laboratorio-api`.

| Configuração | VM options | Comportamento |
|---|---|---|
| `JVM Labs — Portal Automático` | `-Dlaboratorio.portal.auto-open=true` | Inicia o Spring Boot e solicita abertura do Portal no navegador padrão do macOS. |
| `JVM Labs — Servidor Manual` | `-Dlaboratorio.portal.auto-open=false` | Inicia somente o servidor, sem solicitar abertura do navegador. |

As configurações podem ser salvas com **Store as project file** em `.run/`; conferir antes do commit se estão efetivamente versionadas.

A configuração base permanece:

```yaml
spring:
  application:
    name: laboratorio-api
server:
  port: 8080
laboratorio:
  portal:
    auto-open: false
    path: /portal.html
```

A abertura automática no macOS é efetuada pela classe `PortalBrowserLauncher`, via comando nativo `open` quando a propriedade está ativa. Fora desse ambiente, a implementação possui fallback baseado em `Desktop`, sujeito às capacidades gráficas disponíveis. As duas configurações não devem ser executadas simultaneamente na mesma porta.

### 5.4. Identidade visual, imagem de fundo e rodapé autoral do Portal

O Portal JVM Labs (`/portal.html`) utiliza a imagem **`everest-bg.png`**, armazenada em `laboratorio-api/src/main/resources/static/images/`, como plano de fundo. A composição visual representa a jornada de aprendizagem e conquista técnica. O arquivo `static/css/portal.css` aplica um gradiente escuro sobre a imagem para manter o contraste e a legibilidade dos três cartões e dos acessos rápidos.

O rodapé, implementado em `static/portal.html` e estilizado no `portal.css`, identifica o projeto e sua autoria. Seu conteúdo aprovado é:

```text
PROJETO JVM LABS · JAVA 21 · SPRING BOOT
Desenvolvido por Claudio Cavalcante de Almeida
© 2026 Claudio Cavalcante de Almeida. Todos os direitos reservados.
Versão 1.0.0 · Projeto educacional e de portfólio
```

**Controle de versões:** a identificação **1.0.0** é a versão de apresentação do Portal exibida no rodapé. Ela **não altera nem substitui** a versão do artefato Maven, registrada durante os testes como `laboratorio-api 0.0.1-SNAPSHOT`. Uma eventual sincronização das versões deverá ser decidida e testada separadamente.

**Direitos autorais:** o aviso do rodapé refere-se às contribuições originais do autor, respeitando as licenças de ferramentas, bibliotecas e outros componentes de terceiros. As condições de uso estão detalhadas na seção 10.

## 6. Inventário de endpoints REST

**Base:** `http://localhost:8080` · **Método:** `GET` · **Situação:** 15 de 15 rotas responderam HTTP 200 no teste informado em 21/10/2026.

| Método | Rota | Finalidade |
|---|---|---|
| GET | `/api/status` | Estado da aplicação e versão do Java |
| GET | `/api/threads/executar` | Execução de thread real |
| GET | `/api/threads/race-condition` | Concorrência e race condition |
| GET | `/api/threads/reentrant-lock` | Lock e tryLock |
| GET | `/api/threads/atomic-integer` | Operações atômicas |
| GET | `/api/threads/virtual-threads` | Comparação de threads |
| GET | `/api/threads/diagnostico` | Estados e diagnóstico |
| GET | `/api/bytecode/aritmetica` | Soma |
| GET | `/api/bytecode/matematica` | Multiplicação e subtração |
| GET | `/api/bytecode/decisao` | Desvios condicionais |
| GET | `/api/bytecode/race-condition` | Bytecode da concorrência |
| GET | `/api/bytecode/race-condition-sincronizada` | Método sincronizado |
| GET | `/api/bytecode/objetos` | Criação de objetos |
| GET | `/api/bytecode/sincronizacao` | Monitores |
| GET | `/api/bytecode/deadlock` | Dois métodos e espera circular |

Exemplos:

```bash
curl -i http://localhost:8080/api/status
curl -i http://localhost:8080/api/bytecode/deadlock
```

Os endpoints de Threads executam laboratórios reais ao serem chamados; faça esses testes em ambiente local controlado. **HTTP 200 prova a resposta da rota, não uma validação semântica completa do JSON ou de todos os cenários de concorrência.**

## 7. Deadlock — experimento 3.17.6

A Thread A entra em `LOCK_1` e aguarda `LOCK_2`; a Thread B entra em `LOCK_2` e aguarda `LOCK_1`. Um `CountDownLatch(2)` sincroniza a segunda tentativa de aquisição. O deadlock foi reproduzido em **processo Java separado** e diagnosticado por `jcmd` e `jstack`; `javap` confirmou `monitorenter` nos offsets 5 e 36, com opcode `C2`.

```bash
javac modulo-03-bytecode/etapa-3.17.6/DeadlockMonitor.java
java -cp modulo-03-bytecode/etapa-3.17.6 DeadlockMonitor
# Em outro terminal, identificar o PID do processo de laboratório:
jcmd -l
jcmd 93417 Thread.print -l  # exemplo historico do laboratorio de Deadlock
jstack -l 93417              # exemplo historico; consultar PID atual com jcmd -l
javap -c -p modulo-03-bytecode/etapa-3.17.6/DeadlockMonitor.class
```

As duas threads foram validadas na interface até o passo **17/17**. A API `/api/bytecode/deadlock` expõe Bytecode; **não executa um deadlock novo no processo Spring Boot**.

## 8. Testes, regressão e evidências

Validações registradas em 21/10/2026, a partir de testes executados e informados pelo autor:

| Conjunto | Evidência | Resultado |
|---|---|---|
| Módulo 1 — JVM | JVM-01 a JVM-09 | 9/9 aprovados |
| Módulo 2 — Threads | THR-01 a THR-08 | 8/8 aprovados |
| Módulo 3 — Bytecode | BC-01 a BC-09 | 9/9 aprovados |
| API de status | GET `/api/status` | HTTP 200 |
| APIs Threads | 6 rotas | 6/6 HTTP 200 |
| APIs Bytecode | 8 rotas | 8/8 HTTP 200 |
| Maven | `mvn -f laboratorio-api/pom.xml clean test` | BUILD SUCCESS |
| JUnit | `LaboratorioApiApplicationTests` | 1 teste, 0 falhas, 0 erros, 0 ignorados |

O Maven compilou **14 arquivos Java do código principal** e **1 arquivo de testes**, conforme saída registrada. O teste automatizado validou a subida do contexto Spring, mas **não** constitui uma suíte abrangente de integração dos 15 endpoints. Mockito/Byte Buddy emitiram avisos sobre carregamento dinâmico de agentes para futuras versões de JDK; não impediram a execução dos testes.

Verificação adicional opcional do JavaScript:

```bash
node --check laboratorio-api/src/main/resources/static/js/portal.js
node --check laboratorio-api/src/main/resources/static/js/navegacao-modulos.js
node --check laboratorio-api/src/main/resources/static/js/bytecode.js
```

## 9. Referências técnicas oficiais

- [Java Virtual Machine Specification — Java SE 21](https://docs.oracle.com/javase/specs/jvms/se21/html/)
- [Java Thread API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [jcmd — JDK 21](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jcmd.html)
- [jstack — JDK 21](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jstack.html)
- [javap — JDK 21](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html)
- [JEP 444 — Virtual Threads](https://openjdk.org/jeps/444)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/reference/)

## 10. Autoria, propriedade intelectual e condições de uso

### 10.1. Identificação do autor

**Projeto:** Projeto JVM Labs — Java 21, JVM, Threads, Bytecode Explorer e Portal Web  
**Autor:** Claudio Cavalcante de Almeida  
**Ano:** 2026  
**Perfil GitHub:** [ClaudioCavalcante-BR](https://github.com/ClaudioCavalcante-BR)

O Projeto JVM Labs foi concebido e desenvolvido pelo autor como trabalho técnico e educacional de portfólio, reunindo estudos, organização didática, experimentos, integrações e documentação sobre JVM, concorrência e Bytecode Java.

### 10.2. Direitos sobre as contribuições originais

A autoria declarada abrange as contribuições originais do autor, incluindo a estrutura didática própria, os textos, os diagramas e os códigos originais elaborados para o projeto, sem reivindicar direitos sobre conceitos técnicos, especificações públicas, bibliotecas ou materiais de terceiros.

**Copyright © 2026 Claudio Cavalcante de Almeida. Todos os direitos reservados sobre as contribuições autorais originais.**

### 10.3. Consulta pública e portfólio

O repositório pode ser consultado publicamente para fins de estudo, avaliação técnica e apresentação profissional. A mera disponibilidade pública dos arquivos não significa que o projeto esteja em domínio público ou que exista autorização geral para copiá-lo, redistribuí-lo, modificá-lo ou explorá-lo comercialmente.

### 10.4. Reprodução, atribuição e exploração comercial

Salvo autorização expressa do autor ou licença aplicável, não se concede permissão para reproduzir ou redistribuir substancialmente os componentes autorais, oferecê-los comercialmente, incorporá-los a produtos ou cursos comerciais ou apresentá-los como criação de terceiros. Permanecem resguardadas as utilizações permitidas pela legislação e os direitos e licenças de terceiros.

Ao mencionar este trabalho, utilize a referência:

> ALMEIDA, Claudio Cavalcante de. *Projeto JVM Labs — Java 21, JVM, Threads, Bytecode Explorer e Portal Web*. 2026. Repositório: https://github.com/ClaudioCavalcante-BR.

A atribuição da autoria, por si só, não substitui autorização quando esta for juridicamente necessária.

### 10.5. Tecnologias de terceiros e licenciamento

Java, OpenJDK, Spring Boot, Maven, IntelliJ IDEA e outras tecnologias utilizadas permanecem sob os direitos, marcas e licenças de seus respectivos titulares. Os arquivos de terceiros mantêm suas condições próprias de uso.

Este README expressa a posição do autor sobre suas contribuições originais, mas **não substitui um arquivo `LICENSE` formal**, caso o autor deseje conceder permissões específicas. Na ausência de licença expressa, não se presume uma licença aberta para reutilização do código original.

### 10.6. Solicitações de autorização

Solicitações de reprodução, adaptação, distribuição ou uso comercial das contribuições originais devem ser encaminhadas ao autor pelos canais profissionais divulgados em seu perfil GitHub.


## 11. Pasta `docs/` — documentação de instalação, operação e estudos

A pasta `docs/` reúne os documentos editáveis do projeto e deve ser mantida **na raiz do repositório**, separada do código-fonte e dos arquivos estáticos servidos pelo Spring Boot. Não é necessário colocar esses documentos em `src/main/resources/static`.

| Arquivo em `docs/` | Conteúdo | Destinatário |
|---|---|---|
| [`03_Manual_Instalacao_Execucao_Navegacao.docx`](docs/03_Manual_Instalacao_Execucao_Navegacao.docx) | Preparação do ambiente, execução A/B, navegação, testes e solução de problemas | Desenvolvedor e usuário |
| [`04_Guia_Diagnostico_JVM_Deadlock_Passo_a_Passo.docx`](docs/04_Guia_Diagnostico_JVM_Deadlock_Passo_a_Passo.docx) | Reprodução controlada do Deadlock, PID histórico 93417, comandos `jcmd`, `jstack`, `javap`, evidências e interpretação | Desenvolvedor e avaliador técnico |
| [`05_Glossario_Completo_Projeto_JVM_Labs.docx`](docs/05_Glossario_Completo_Projeto_JVM_Labs.docx) | Glossário progressivo: conceito, significado, exemplo prático e trecho de código/comando | Estudo e entrevistas |

O manual original produzido na Etapa 4.7 foi preservado como documento de referência. Os documentos do repositório devem registrar sua revisão sempre que houver alteração de rotas, arquivos ou configuração de inicialização.

### 11.1. Configuração e uso do manual de instalação e navegação

1. Instale e selecione **JDK 21** no IntelliJ; abra a pasta raiz `Projeto-jvm-labs`.
2. Verifique `laboratorio-api/pom.xml` e `laboratorio-api/src/main/resources/application.yaml` (`server.port: 8080`, `laboratorio.portal.path: /portal.html`).
3. Em **Run > Edit Configurations**, mantenha as configurações `JVM Labs — Portal Automático` (`-Dlaboratorio.portal.auto-open=true`) e `JVM Labs — Servidor Manual` (`-Dlaboratorio.portal.auto-open=false`). Ambas usam `br.com.jvmlabs.laboratorioapi.LaboratorioApiApplication` e JDK 21.
4. Execute **somente uma configuração por vez**. A primeira solicita ao macOS a abertura de `http://localhost:8080/portal.html` via comando nativo `open`; a segunda inicia o servidor sem abrir o navegador.
5. Acesse o Portal, escolha JVM, Threads ou Bytecode e utilize os links de retorno. Para consultar serviços HTTP, use os endpoints da seção 6.
6. Para testes técnicos, execute `mvn -f laboratorio-api/pom.xml clean test`; consulte o manual em [`docs/03_Manual_Instalacao_Execucao_Navegacao.docx`](docs/03_Manual_Instalacao_Execucao_Navegacao.docx).

A confirmação no macOS e o `BUILD SUCCESS` são evidências dos testes executados em 21/10/2026, não uma garantia de execução automática em todos os sistemas operacionais.

## 12. Diagnóstico técnico reproduzível: JVM, Threads e Deadlock

O guia em [`docs/04_Guia_Diagnostico_JVM_Deadlock_Passo_a_Passo.docx`](docs/04_Guia_Diagnostico_JVM_Deadlock_Passo_a_Passo.docx) documenta um diagnóstico **efetivamente realizado** durante o experimento 3.17.6, sem provocar deadlock no servidor Spring Boot.

**PID de referência registrado no experimento:** `93417` (histórico, não permanente). Os comandos abaixo reproduzem o procedimento **para aquele PID, apenas se o processo ainda existir**. Para uma nova execução, primeiro use `jcmd -l` para identificar o PID atual.

```bash
javac modulo-03-bytecode/etapa-3.17.6/DeadlockMonitor.java
java -cp modulo-03-bytecode/etapa-3.17.6 DeadlockMonitor
# Em outro terminal (exemplo historico: PID 93417)
jcmd -l
jcmd 93417 Thread.print -l
jstack -l 93417
javap -c -p modulo-03-bytecode/etapa-3.17.6/DeadlockMonitor.class
```

Na validação histórica, as ferramentas identificaram **um deadlock de nível Java** e `javap` revelou `monitorenter` nos offsets **5 e 36** dos dois métodos, com opcode hexadecimal **C2**. A Thread A mantém `LOCK_1` e espera `LOCK_2`; a Thread B mantém `LOCK_2` e espera `LOCK_1`. A interface foi validada até 17/17 passos por thread. Logs originais são evidências de laboratório e podem ficar em pasta própria; não os confunda com um dump produzido pela API.

## 13. Estrutura de pastas e responsabilidades

Padrão de documentação inspirado na organização de árvores comentadas utilizada nos projetos INFNET: à direita de cada caminho está sua finalidade. **Esta é a estrutura consolidada comprovada pelos arquivos apresentados e pelos registros do projeto; não é um inventário exaustivo do disco.** Elementos condicionais são identificados como tal. Pastas de backup podem ter outros nomes e arquivos.

```text
Projeto-jvm-labs/                                  # Raiz do repositório e do portfólio
├── README.md                                      # Apresentação geral, acessos, testes e autoria
├── docs/                                          # Documentação técnica e de apoio (pasta proposta nesta revisão)
│   ├── 03_Manual_Instalacao_Execucao_Navegacao.docx # Instalação, execução e navegação
│   ├── 04_Guia_Diagnostico_JVM_Deadlock_Passo_a_Passo.docx # Diagnóstico reproduzível com PID histórico
│   └── 05_Glossario_Completo_Projeto_JVM_Labs.docx # Vocabulário progressivo e exemplos
├── modulo-01-jvm/                                 # Estudos e exercícios sobre a JVM
├── modulo-02-threads/                             # Estudos e exercícios de concorrência Java
├── modulo-03-bytecode/                            # Classes Java e experimentos analisados por javap
│   └── etapa-3.17.6/                              # Exemplo de deadlock com dois monitores
│       └── DeadlockMonitor.java                    # Thread A, Thread B, LOCK_1 e LOCK_2
├── laboratorio-api/                               # Aplicação Spring Boot que serve APIs e páginas
│   ├── pom.xml                                     # Dependências Maven, plugins e parâmetros de build
│   ├── .mvn/                                       # Configuração do Maven Wrapper (presença observada)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/jvmlabs/laboratorioapi/ # Pacote base Java da aplicação
│   │   │   │   ├── LaboratorioApiApplication.java   # Ponto de entrada do Spring Boot
│   │   │   │   ├── PortalBrowserLauncher.java       # Abre o Portal opcionalmente no macOS
│   │   │   │   ├── controller/                       # Controllers e rotas REST
│   │   │   │   ├── service/                          # Serviços e lógica de laboratório
│   │   │   │   └── bytecode/                         # Apoio ao processamento de Bytecode
│   │   │   └── resources/
│   │   │       ├── application.yaml                  # Porta e opções de abertura do Portal
│   │   │       └── static/                           # Conteúdo web servido diretamente
│   │   │           ├── index.html                    # Módulos 1 e 2 e mapa original
│   │   │           ├── bytecode.html                 # Módulo 3 — Bytecode Explorer
│   │   │           ├── portal.html                   # Portal JVM Labs — acesso aos três módulos
│   │   │           ├── images/                          # Imagens de apresentação do Portal
│   │   │           │   └── everest-bg.png               # Fundo de montanha, símbolo de conquista
│   │   │           ├── css/
│   │   │           │   ├── style.css                  # Estilos da página JVM/Threads
│   │   │           │   ├── bytecode.css               # Estilos do Bytecode Explorer
│   │   │           │   └── portal.css                 # Estilos específicos do Portal
│   │   │           └── js/
│   │   │               ├── app.js                     # Interatividade JVM/Threads
│   │   │               ├── bytecode.js                # Interpretação didática do Bytecode
│   │   │               ├── portal.js                  # Configuração dos links do Portal
│   │   │               └── navegacao-modulos.js      # Seleção por parametro ?modulo=
│   │   └── test/java/.../LaboratorioApiApplicationTests.java # Teste de contexto Spring
│   ├── target/                                    # Saída gerada pelo Maven; não versionar
│   └── backup-etapa-3.*                           # Diretórios históricos de backup observados; revisar antes de publicar
└── .run/                                          # Configurações IntelliJ compartilhadas, SE salvas como project file
```

### 13.1. Inventário completo da sua cópia local

Para obter uma listagem **literal e completa** — inclusive arquivos não enviados para esta revisão — execute na raiz do projeto (macOS):

```bash
find . -print | LC_ALL=C sort > inventario-arquivos-completo.txt
```

Para uma listagem mais útil à publicação, excluindo arquivos gerados, dependências, pastas IDE e backups:

```bash
find . \
  -path '*/.git' -prune -o \
  -path '*/.idea' -prune -o \
  -path '*/target' -prune -o \
  -path '*/backup-*' -prune -o \
  -print | LC_ALL=C sort > inventario-arquivos-publicacao.txt
```

O arquivo gerado é uma **fotografia do estado local**. Compare-a com a árvore acima para identificar classes adicionais, notas de laboratório, arquivos de build e outros recursos antes de declarar o inventário completo. Revise esses arquivos antes de publicá-los, pois podem conter informações locais.

## 14. Glossário técnico do Projeto JVM Labs

Consulte [`docs/05_Glossario_Completo_Projeto_JVM_Labs.docx`](docs/05_Glossario_Completo_Projeto_JVM_Labs.docx). O documento reúne conceitos básicos de programação Java, estruturas da JVM, estados e sincronização de Threads, Bytecode e opcodes, comunicação HTTP/REST, Spring Boot, Maven, IntelliJ, diagnóstico e observabilidade. Cada termo possui **definição objetiva, exemplo didático e referência de código ou comando**. Os exemplos explicativos não devem ser confundidos com transcrições literais de todos os arquivos do projeto.
