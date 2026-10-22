# Laboratório API — Projeto JVM Labs

## 1. Apresentação

O **Laboratório API** é o componente executável do **Projeto JVM Labs**, um projeto educacional de Java Back End desenvolvido para estudar, implementar e observar o funcionamento da JVM e a concorrência em Java 21. A aplicação combina uma API REST com Spring Boot e uma interface web didática, permitindo executar experimentos reais e examinar seus resultados.

**Escopo:** estudo, demonstração técnica e portfólio. Este repositório não se apresenta como sistema de produção nem como benchmark científico de desempenho.

## 2. Objetivos

- Aplicar conceitos de JVM, JDK, JRE, threads e concorrência em código Java executável.
- Demonstrar diferenças entre condições de corrida e mecanismos de sincronização.
- Explorar `ReentrantLock`, `tryLock()`, `volatile`, `AtomicInteger` e Virtual Threads.
- Investigar estados e pilhas de threads com ferramentas do JDK.
- Registrar evidências de diagnóstico com `jcmd`, `jstack` e Java Flight Recorder (JFR).
- Disponibilizar resultados por endpoints HTTP/JSON e interface web.

## 3. Organização dos estudos

O projeto educacional completo está organizado em dois módulos conceituais:

| Módulo | Tema | Abordagem |
| --- | --- | --- |
| Módulo 1 | JVM | Arquitetura e execução Java; JDK e JRE; JEPs; diagnóstico e observabilidade. |
| Módulo 2 | Threads | Ciclo de vida, concorrência, condição de corrida, sincronização, locks, operações atômicas e Virtual Threads. |

O diretório `laboratorio-api` reúne a **implementação Spring Boot e os laboratórios HTTP**, incluindo a Etapa 3.16. Os materiais dos módulos conceituais são mantidos em diretórios próprios na raiz de `Projeto-jvm-labs`.

## 4. Tecnologias e requisitos

| Componente | Referência do ambiente utilizado |
| --- | --- |
| Linguagem | Java 21 (ambiente validado: OpenJDK 21.0.10) |
| Framework | Spring Boot 4.1.1 |
| Build | Maven, com Maven Wrapper (`mvnw`) |
| IDE | IntelliJ IDEA |
| API | HTTP e JSON |
| Interface | HTML, CSS e JavaScript |
| Servidor HTTP | Tomcat incorporado; porta local 8080 |
| Diagnóstico | `jcmd`, `jstack`, `jfr` (ferramentas do JDK) |

**Requisitos mínimos:** Git, JDK 21 instalado e uma IDE compatível (IntelliJ IDEA recomendado). O Maven Wrapper permite executar o build sem instalar o Maven separadamente, desde que os arquivos do wrapper estejam presentes e o ambiente consiga obter suas dependências.

## 5. Obter o projeto do GitHub

1. Instale o Git, caso ainda não esteja disponível, e verifique com `git --version`.
2. No GitHub, abra a página do **repositório efetivamente publicado** e selecione **Code > HTTPS**.
3. Copie a URL HTTPS apresentada pelo GitHub.
4. No terminal, execute (substituindo a URL ilustrativa pela URL real):

```bash
git clone <URL-HTTPS-DO-REPOSITORIO>
```

5. Entre na pasta clonada e localize `Projeto-jvm-labs/laboratorio-api`. A disposição exata dos diretórios depende de como o repositório foi publicado.

**Observação:** a URL pública do repositório não foi fornecida nesta documentação; por isso, não foi inventado um endereço de clonagem. Se o projeto ainda não estiver publicado, conclua primeiro o versionamento e o envio ao GitHub.

## 6. Preparar o Java 21

Verifique a instalação:

```bash
java -version
javac -version
```

Ambos devem indicar a versão **21**. Em macOS, Linux e Windows, instale uma distribuição confiável do JDK 21 e configure `JAVA_HOME` quando necessário. No macOS, é possível identificar os JDKs instalados com:

```bash
/usr/libexec/java_home -V
```

No IntelliJ IDEA, abra **File > Project Structure > Project** e selecione um **Project SDK Java 21**. Em **Settings/Preferences > Build, Execution, Deployment > Build Tools > Maven**, confira o JDK usado pelo Maven. Se houver incompatibilidade, configure também o JDK da configuração de execução.

## 7. Abrir e configurar no IntelliJ IDEA

1. Abra o IntelliJ IDEA e escolha **Open**.
2. Selecione a pasta `laboratorio-api` que contém o arquivo `pom.xml` (ou abra `Projeto-jvm-labs` e importe o `pom.xml` do módulo).
3. Aguarde a importação/sincronização Maven e o download das dependências.
4. Confirme que o SDK é Java 21 e que o Maven reconheceu o `pom.xml`.
5. Localize a classe principal `br.com.jvmlabs.laboratorioapi.LaboratorioApiApplication`.
6. Confira o arquivo de configuração `src/main/resources/application.yaml`. O projeto utiliza a extensão **`.yaml`**.
7. Verifique se a porta `8080` está disponível. Se estiver ocupada, libere-a ou adapte a configuração e os URLs de teste de forma consistente.

Não é necessário configurar banco de dados para os laboratórios aqui documentados.

## 8. Compilar e executar

Abra o terminal **na pasta `laboratorio-api`**, isto é, no mesmo diretório de `pom.xml` e `mvnw`.

No macOS ou Linux:

```bash
chmod +x mvnw
./mvnw clean package
./mvnw spring-boot:run
```

No Windows (Prompt de Comando):

```bat
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

Alternativamente, no IntelliJ, execute `LaboratorioApiApplication` pelo botão **Run**. Mantenha a aplicação ativa durante os testes. A mensagem de inicialização do servidor na porta 8080 confirma que o processo subiu; confirme também o acesso HTTP no navegador.

**Atenção ao diretório atual:** se o terminal estiver em `laboratorio-api`, use caminhos como `diagnosticos-jvm/etapa-3.16/`. Se estiver em `Projeto-jvm-labs`, acrescente o prefixo `laboratorio-api/`. Misturar esses contextos causa erros do tipo `No such file or directory`.

## 9. Acessar a interface

Com a aplicação iniciada, abra:

- **Interface principal:** http://localhost:8080/
- **Página auxiliar de testes:** http://localhost:8080/teste-api.html (se presente na versão utilizada)

A interface web apresenta explicações e controles para executar os laboratórios por meio das APIs Java. O endereço `localhost` funciona apenas no computador em que o serviço está acessível localmente.

## 10. Endpoints implementados

| ID | Método | Caminho | Finalidade |
| --- | --- | --- | --- |
| API-01 | GET | `/api/status` | Consultar dados da aplicação e da JVM. |
| API-02 | GET | `/api/threads/executar` | Criar e executar uma thread Java. |
| API-03 | GET | `/api/threads/race-condition` | Demonstrar condição de corrida e sincronização. |
| API-04 | GET | `/api/threads/reentrant-lock` | Demonstrar `ReentrantLock` e `tryLock()`. |
| API-05 | GET | `/api/threads/atomic-integer` | Comparar `volatile` e `AtomicInteger`. |
| API-06 | GET | `/api/threads/virtual-threads` | Comparar execução de tarefas com threads de plataforma e virtuais. |
| API-07 | GET | `/api/threads/diagnostico` | Observar estados e informações de pilhas de duas threads reais. |

Exemplo de teste no terminal:

```bash
curl -i http://localhost:8080/api/status
curl -s http://localhost:8080/api/threads/diagnostico
```

Para testar no navegador, acrescente o caminho do endpoint a `http://localhost:8080`. Para usar o cliente HTTP do IntelliJ, consulte o inventário em `testes-api/README.md`. A existência de cada arquivo `.http` deve ser conferida na cópia do projeto; o inventário original inclui nomes sugeridos, não necessariamente todos criados.

**Critérios de validação:** resposta HTTP 200 nos testes bem-sucedidos, JSON esperado, ausência de exceções inesperadas, correspondência entre API e interface e registro das variações naturais de concorrência.

## 11. Resultados técnicos dos laboratórios

### 11.1 Condição de corrida e sincronização

Os experimentos com múltiplas threads demonstram que a operação `contador++` não é atômica. O uso de sincronização protege a região crítica e evita atualizações perdidas na execução proposta.

### 11.2 `ReentrantLock` e `tryLock()`

O laboratório demonstra aquisição e liberação explícitas de lock e a tentativa de aquisição sem aguardar indefinidamente. Em aplicações reais, a liberação deve ser protegida por `finally` quando o lock for adquirido.

### 11.3 `volatile` e `AtomicInteger` — Etapa 3.14

Em uma execução documentada, com oito threads e 100.000 incrementos por thread, o valor esperado era 800.000. O contador `volatile` resultou em 416.210, enquanto o `AtomicInteger` atingiu 800.000. **Interpretação:** `volatile` fornece garantias de visibilidade, mas não transforma `contador++` em operação atômica. O resultado numérico pode variar a cada teste.

### 11.4 Virtual Threads — Etapa 3.15

Na execução registrada, 100 tarefas com espera simulada de 100 ms apresentaram aproximadamente 1.037 ms usando pool de 10 Platform Threads e 110 ms usando uma Virtual Thread por tarefa. **Ressalva:** os graus de concorrência são diferentes; portanto, o experimento ilustra a execução de tarefas bloqueantes, mas não constitui benchmark comparativo em condições equivalentes.

### 11.5 Diagnóstico de threads — Etapa 3.16

A API-07 cria duas threads, observa os estados `NEW`, `WAITING` e `TERMINATED` e obtém informações de suas pilhas. O diagnóstico externo foi realizado com as ferramentas do JDK, conforme a seção seguinte.

## 12. Etapa 3.16 — Diagnóstico e observabilidade da JVM

### 12.1 Identificar o processo Java

```bash
jcmd -l
jcmd <PID> VM.command_line
```

O PID muda quando a aplicação é reiniciada. Nos testes documentados, foi utilizado o PID `4876`, **apenas como exemplo histórico**. Sempre identifique o PID atual antes de executar comandos.

### 12.2 Gerar Thread Dumps

Os comandos abaixo devem ser executados **a partir da raiz `Projeto-jvm-labs`**:

```bash
mkdir -p laboratorio-api/diagnosticos-jvm/etapa-3.16
jcmd <PID> Thread.print -l > laboratorio-api/diagnosticos-jvm/etapa-3.16/thread-dump-jcmd.txt
jstack -l <PID> > laboratorio-api/diagnosticos-jvm/etapa-3.16/thread-dump-jstack.txt
grep 'java.lang.Thread.State:' laboratorio-api/diagnosticos-jvm/etapa-3.16/thread-dump-jstack.txt | sort | uniq -c
```

Na captura documentada, foram contabilizados **29 estados de threads**: 12 `RUNNABLE`, 12 `WAITING` e 5 `TIMED_WAITING`. Não houve ocorrência de `BLOCKED` naquela captura. Trata-se de uma fotografia temporal, não de prova de ausência de contenção em todos os momentos.

### 12.3 Gravar eventos com Java Flight Recorder

A partir da raiz `Projeto-jvm-labs`:

```bash
jcmd <PID> JFR.start name=JVM_LABS_316 settings=profile duration=60s filename=laboratorio-api/diagnosticos-jvm/etapa-3.16/gravacao-jvm-labs.jfr
```

O caminho de saída do JFR pode depender do diretório de trabalho da JVM. Confira o caminho absoluto informado pelo comando. Aguarde o término da gravação e execute requisições às APIs durante o intervalo para gerar atividade.

### 12.4 Extrair e examinar os eventos

```bash
jfr summary laboratorio-api/diagnosticos-jvm/etapa-3.16/gravacao-jvm-labs.jfr > laboratorio-api/diagnosticos-jvm/etapa-3.16/resumo-jfr.txt
jfr print --events jdk.ThreadPark,jdk.ThreadSleep laboratorio-api/diagnosticos-jvm/etapa-3.16/gravacao-jvm-labs.jfr > laboratorio-api/diagnosticos-jvm/etapa-3.16/eventos-threads-jfr.txt
```

**Resultados da gravação analisada:** duração de 60 segundos, arquivo JFR de aproximadamente 614 KB, 323 eventos `jdk.ThreadSleep`, 168 `jdk.ThreadPark`, 44 `jdk.ThreadStart`, 68 `jdk.ThreadEnd` e 10 `jdk.ExecutionSample`. O arquivo apresentou zero eventos `jdk.JavaMonitorEnter` e `jdk.GarbageCollection` registrados sob aquela configuração.

Nos eventos examinados, a thread `File Watcher` do Spring Boot DevTools apresentou `ThreadSleep` de 405 ms, e as threads `Catalina-utility-1` e `Catalina-utility-2` apresentaram `ThreadPark` de aproximadamente 996 ms. Essas esperas são compatíveis com o funcionamento de componentes de infraestrutura ociosos. Não permitem, isoladamente, afirmar que toda a aplicação está livre de gargalos, deadlocks ou problemas de memória.

### 12.5 Arquivos de evidência

```text
diagnosticos-jvm/
└── etapa-3.16/
    ├── thread-dump-jcmd.txt
    ├── thread-dump-jstack.txt
    ├── gravacao-jvm-labs.jfr
    ├── resumo-jfr.txt
    └── eventos-threads-jfr.txt
```

**Privacidade:** Thread Dumps, arquivos JFR e logs podem conter nomes de classes, caminhos locais, argumentos de inicialização e outros metadados. Revise-os antes de publicar no GitHub; se necessário, mantenha evidências sensíveis fora do controle de versão.

## 13. Estrutura de diretórios

Estrutura principal conhecida do módulo (algumas pastas de backup e arquivos auxiliares foram omitidos):

```text
laboratorio-api/
├── README.md                     # este documento
├── pom.xml                       # configuração Maven
├── mvnw                          # Maven Wrapper macOS/Linux
├── mvnw.cmd                      # Maven Wrapper Windows
├── .mvn/                         # suporte ao wrapper
├── src/
│   └── main/
│       ├── java/br/com/jvmlabs/laboratorioapi/
│       │   ├── LaboratorioApiApplication.java
│       │   └── controller/        # controllers das APIs
│       └── resources/
│           ├── application.yaml
│           └── static/
│               ├── index.html
│               ├── css/style.css
│               └── js/app.js
├── testes-api/                   # inventário e testes HTTP
│   └── README.md
└── diagnosticos-jvm/
    └── etapa-3.16/               # arquivos de observabilidade
```

## 14. Solução de problemas

| Sintoma | Verificação recomendada |
| --- | --- |
| `java` ou `javac` não encontrado | Instalar/configurar JDK 21 e conferir `PATH` e `JAVA_HOME`. |
| Versão Java incompatível | Ajustar Project SDK, JDK do Maven e configuração de execução no IntelliJ. |
| Falha na importação Maven | Confirmar conexão, arquivos `.mvn/` e `pom.xml`; sincronizar novamente. |
| Porta 8080 em uso | Encerrar o processo conflitante ou ajustar a porta e os URLs. |
| `Connection refused` no navegador | Confirmar que `LaboratorioApiApplication` está em execução. |
| `No such file or directory` | Confirmar diretório atual com `pwd` e corrigir o prefixo dos caminhos. |
| `jcmd` ou `jstack` não encontrado | Verificar se o terminal usa o JDK, e não apenas um runtime Java. |
| PID inválido | Executar `jcmd -l` novamente; o PID pode ter mudado. |
| Arquivo JFR não aparece | Verificar a mensagem do `JFR.start` e o caminho absoluto de saída. |

## 15. Segurança e limitações

- Os endpoints são **didáticos** e usam `GET` para facilitar a demonstração; operações que disparam processamento podem exigir `POST` em APIs de produção.
- Os laboratórios não incluem, por si só, autenticação, autorização, limitação de requisições ou outras proteções exigidas para exposição pública.
- Não exponha a aplicação diretamente à internet sem uma revisão específica de segurança.
- Tempos, estados e contagens de eventos podem variar conforme hardware, sistema operacional, carga e configurações da JVM.
- O laboratório de Virtual Threads não deve ser apresentado como benchmark rigoroso de desempenho.

## 16. Referências técnicas

- [Oracle — Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Oracle — JDK 21 Tools Reference (`jcmd`, `jstack`, `jfr`)](https://docs.oracle.com/en/java/javase/21/docs/specs/man/)
- [OpenJDK — JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [OpenJDK — JEP 425: Virtual Threads (Preview)](https://openjdk.org/jeps/425)
- [Spring Boot — Documentation](https://docs.spring.io/spring-boot/)
- [Apache Maven — Maven Wrapper](https://maven.apache.org/wrapper/)
- [JetBrains — IntelliJ IDEA Documentation](https://www.jetbrains.com/help/idea/)
- [Git — Documentation](https://git-scm.com/doc)

## 17. Autoria, uso e direitos

**Projeto autoral de caráter educacional**, destinado a estudo, demonstração de competências e portfólio em Java Back End.

Os direitos autorais sobre textos e implementações originais pertencem a seus respectivos titulares. A disponibilidade pública de um repositório **não equivale automaticamente à autorização para exploração comercial, cópia ou redistribuição**. As condições efetivas de uso por terceiros dependem de licença expressamente adotada e das normas legais aplicáveis. Bibliotecas e ferramentas de terceiros permanecem sujeitas às próprias licenças.

**Antes da publicação:** definir uma política de licença coerente com a finalidade desejada, revisar eventuais materiais de terceiros e verificar o conteúdo versionado.

## 18. Estado do projeto e manutenção

O escopo didático planejado dos laboratórios e da Etapa 3.16 foi concluído e validado em ambiente local. O README principal documenta o **módulo executável**; os materiais conceituais dos Módulos 1 e 2 continuam em seus respectivos diretórios.

Para manutenção futura: manter o inventário `testes-api/README.md` sincronizado com os endpoints realmente implementados; registrar alterações de API, dependências, requisitos de execução e novos resultados; preservar evidências técnicas revisadas antes da publicação.
