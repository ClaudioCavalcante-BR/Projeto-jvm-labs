# Laboratório Interativo — Java 21, JVM e Threads

## Objetivo
Material didático complementar aos módulos `modulo-01-jvm` e `modulo-02-threads` do projeto `Projeto-jvm-labs`.

## Estrutura
- `index.html`: interface e navegação.
- `css/style.css`: apresentação visual.
- `js/app.js`: conteúdo dos tópicos, eventos e simulação conceitual.
- `backup/`: cópia integral da versão independente previamente funcional.

## Como incorporar ao IntelliJ IDEA
1. Faça backup da pasta `material-interativo` existente.
2. Copie `index.html`, `css/`, `js/` e `backup/` para a pasta `material-interativo` na raiz do `Projeto-jvm-labs`.
3. Abra `material-interativo/index.html` no Chrome ou use a opção **Open in Browser** no IntelliJ.
4. Teste a seleção de `Class Loader`, a navegação para `Módulo 2 — Threads` e os botões `Próxima` e `Reiniciar`.
5. Caso necessário, restaure o HTML da pasta `backup/`.

## Sequência de execução do Laboratório Interativo

### 1. Preparação no IntelliJ IDEA
1. Abra o **IntelliJ IDEA** no MacBook e carregue o projeto `Projeto-jvm-labs`.
2. No painel **Project**, expanda a pasta `material-interativo`.
3. Confirme a presença dos arquivos `index.html`, `css/style.css` e `js/app.js`. Mantenha a estrutura de pastas: os caminhos relativos são necessários para carregar a apresentação e a interatividade.
4. Clique com o botão direito em `index.html` e selecione **Open in Browser → Chrome**, quando disponível. Como alternativa, abra o mesmo arquivo no Google Chrome pelo Finder.
5. Se o IntelliJ abrir um endereço iniciado por `http://localhost:63342/`, isso é esperado: trata-se do servidor local da IDE. O número da porta pode variar.

### 2. Roteiro de estudo no navegador
1. Na página **Java, JVM e Threads — Laboratório Interativo**, selecione **Mapa interativo** para visualizar os conteúdos.
2. Clique em **Módulo 1 — JVM** e estude, nesta ordem sugerida: código-fonte `.java`, compilador `javac`, bytecode `.class`, JVM e JDK/JRE; em seguida, Class Loader, verificador, Heap, Stack, Method Area/Metaspace, interpretador/JIT e Garbage Collector.
3. Selecione os cartões dos componentes para ler a explicação, examinar o exemplo de código e observar a ressalva técnica apresentada no painel de detalhes.
4. Use **Próxima →**, **← Anterior** e **Reiniciar** para percorrer o fluxo conceitual de execução Java. Essa sequência é independente do cartão escolhido manualmente.
5. Clique em **Módulo 2 — Threads** e estude: criação de threads, estados oficiais, Race Condition, `synchronized`, `ReentrantLock`/`tryLock()`, `volatile`/`AtomicInteger`, threads virtuais e diagnóstico/JFR.
6. Ao concluir, retorne ao **Mapa interativo** para revisar os conceitos dos dois módulos.

### 3. Relação com os laboratórios Java reais
1. Para reproduzir os experimentos, volte ao IntelliJ e abra as classes correspondentes nas pastas `modulo-01-jvm` e `modulo-02-threads`.
2. Execute cada classe Java pela configuração de execução apropriada da IDE, seguindo as instruções específicas do respectivo laboratório.
3. Compare a saída do console e, quando aplicável, as evidências de ferramentas como `jcmd`, thread dumps e JFR com as explicações do HTML.
4. **Não confunda os resultados:** o navegador apresenta uma simulação didática; a execução efetiva de bytecode, threads e ferramentas de diagnóstico ocorre no processo Java iniciado pelo JDK.

### 4. Verificação rápida de funcionamento
| Teste | Ação | Resultado esperado |
| --- | --- | --- |
| HTML e CSS | Abrir `index.html` no Chrome | Interface com títulos, cartões e estilos aplicados |
| Navegação | Selecionar **Módulo 2 — Threads** | Exibição da seção de Threads |
| JavaScript | Selecionar **Race Condition** | Painel com explicação e exemplo `contador++` |
| Simulação | Clicar em **Próxima →** e **Reiniciar** | Avanço de etapa e retorno à primeira etapa |

Se a página aparecer sem estilos, confira `css/style.css`. Se os botões não responderem, confira `js/app.js` e abra o Console do Chrome com **⌘ + ⌥ + J**. O laboratório não exige executar um servidor Java ou iniciar os módulos para usar a interface educativa.

## Limites técnicos
O laboratório **não executa bytecode Java**: seus exemplos são explicativos e a navegação é uma simulação didática. Testes reais de JVM, concorrência, JFR, jcmd e thread dumps continuam nos módulos Java.

## Verificação de conteúdo
A interface aborda fundamentos de JVM e threads, mas não constitui auditoria integral do código-fonte dos dois módulos. Para declarar aderência completa, é preciso comparar cada classe e teste Java do projeto com os tópicos e exemplos da interface.

## Cuidados conceituais
- `RUNNING` não é estado oficial de `Thread.State` (faz parte de `RUNNABLE` no modelo da API).
- `volatile` não torna `contador++` atômico.
- `tryLock()` sem timeout retorna imediatamente se não conseguir adquirir o lock.
- A Metaspace é uma característica da implementação HotSpot; a especificação descreve a Method Area.

## Referências oficiais
- JVM Specification Java SE 21: https://docs.oracle.com/javase/specs/jvms/se21/html/
- Thread API Java SE 21: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html
- Java Flight Recorder: https://docs.oracle.com/en/java/javase/21/jfapi/
- JEP 444 (Virtual Threads): https://openjdk.org/jeps/444
