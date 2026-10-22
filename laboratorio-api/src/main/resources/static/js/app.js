window.addEventListener("error", function (e) {
    var x = document.getElementById("jsHealth");
    if (x) x.textContent = "Erro no JavaScript: " + e.message;
});
const TOPICS = {
    source: {
        title: 'Código-fonte Java',
        description: 'Arquivos .java contêm classes, métodos e instruções escritos pelo desenvolvedor.',
        code: 'public class Exemplo {\n  public static void main(String[] args) {\n    System.out.println("Olá, Java!");\n  }\n}',
        tip: 'O arquivo .java não é executado diretamente pela JVM.'
    },
    compiler: {
        title: 'Compilador javac',
        description: 'O compilador do JDK transforma código-fonte em bytecode.',
        code: 'javac Exemplo.java\n// Gera Exemplo.class',
        tip: 'O bytecode é independente da arquitetura de processador.'
    },
    bytecode: {
        title: 'Bytecode .class',
        description: 'Representação intermediária de instruções que a JVM entende.',
        code: 'javap -c Exemplo',
        tip: 'Bytecode não equivale ao código de máquina nativo.'
    },
    jvm: {
        title: 'Java Virtual Machine',
        description: 'A JVM carrega, verifica e executa classes e gerencia os recursos de execução.',
        code: 'java Exemplo\n// Olá, Java!',
        tip: 'Cada plataforma usa uma implementação de JVM compatível.'
    },
    loader: {
        title: 'Class Loader',
        description: 'Localiza e carrega classes; participa das fases de carregamento, ligação e inicialização.',
        code: 'Class<?> tipo = Class.forName("java.lang.String");',
        tip: 'Classes podem ser carregadas sob demanda.'
    },
    verifier: {
        title: 'Verificador de bytecode',
        description: 'Verifica propriedades estruturais e de segurança de tipos do bytecode.',
        code: 'javap -verbose Exemplo',
        tip: 'Não substitui toda a segurança da aplicação.'
    },
    heap: {
        title: 'Heap',
        description: 'Memória compartilhada pelas threads, onde normalmente ficam objetos e arrays.',
        code: 'Object objeto = new Object();',
        tip: 'O acesso concorrente a objetos mutáveis pode exigir sincronização.'
    },
    stack: {
        title: 'Java Stack',
        description: 'Cada thread possui sua própria pilha de frames, com variáveis locais e chamadas de métodos.',
        code: 'void calcular() {\n  int numero = 10;\n}',
        tip: 'Referências locais podem apontar para objetos compartilhados no heap.'
    },
    metaspace: {
        title: 'Method Area e Metaspace',
        description: 'A Method Area é definida pela especificação; a HotSpot armazena metadados de classes principalmente na Metaspace.',
        code: 'java -XX:MaxMetaspaceSize=256m Exemplo',
        tip: 'Metaspace é detalhe de implementação da HotSpot.'
    },
    jit: {
        title: 'Interpretador e JIT',
        description: 'O interpretador executa bytecode; o JIT pode compilar trechos frequentes em código nativo.',
        code: 'java -XX:+PrintCompilation Exemplo',
        tip: 'A compilação JIT é adaptativa.'
    },
    gc: {
        title: 'Garbage Collector',
        description: 'Recupera memória de objetos que não estão mais alcançáveis.',
        code: 'java -Xlog:gc Exemplo',
        tip: 'Não há garantia de coleta imediata.'
    },
    jdk: {
        title: 'JDK',
        description: 'Kit para desenvolver Java: compilador, ferramentas e runtime.',
        code: 'java -version\njavac -version',
        tip: 'O JRE é o conceito de ambiente de execução; distribuições modernas podem usar imagens customizadas com jlink.'
    },
    thread: {
        title: 'Thread',
        description: 'Fluxo de execução dentro de um processo; threads compartilham heap e têm stacks próprias.',
        code: 'Thread t = new Thread(() -> System.out.println("Olá"));\nt.start();',
        tip: 'start() inicia uma thread; run() chamado diretamente não cria outra.'
    },
    lifecycle: {
        title: 'Ciclo de vida',
        description: 'Estados oficiais Thread.State: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING e TERMINATED.',
        code: 'Thread.State estado = Thread.currentThread().getState();',
        tip: 'RUNNING não é um estado separado na enumeração Thread.State.'
    },
    race: {
        title: 'Race condition',
        description: 'O resultado depende da ordem de interleaving de operações concorrentes.',
        code: 'contador++; // leitura, soma e escrita: não atômico',
        tip: 'Um resultado incorreto pode ocorrer quando várias threads modificam o mesmo contador.'
    },
    sync: {
        title: 'synchronized',
        description: 'Exclusão mútua com monitor intrínseco; estabelece garantias de visibilidade entre threads.',
        code: 'synchronized void incrementar() {\n  contador++;\n}',
        tip: 'Threads concorrentes precisam sincronizar sobre o mesmo monitor.'
    },
    lock: {
        title: 'ReentrantLock e tryLock()',
        description: 'Lock explícito com opções de aquisição, como tryLock(), e liberação no finally.',
        code: 'if (lock.tryLock()) {\n  try { atualizar(); }\n  finally { lock.unlock(); }\n}',
        tip: 'tryLock() sem timeout retorna imediatamente se o lock não estiver disponível.'
    },
    volatile: {
        title: 'volatile e AtomicInteger',
        description: 'volatile oferece garantias de visibilidade; AtomicInteger suporta operações atômicas como incrementAndGet().',
        code: 'AtomicInteger contador = new AtomicInteger();\ncontador.incrementAndGet();',
        tip: 'volatile isoladamente não torna contador++ atômico.'
    },
    virtual: {
        title: 'Threads virtuais (Java 21)',
        description: 'Threads virtuais são leves e úteis para muitas tarefas concorrentes com espera por I/O.',
        code: 'Thread.startVirtualThread(() -> tarefa());',
        tip: 'Não aceleram automaticamente cálculos intensivos de CPU.'
    },
    observability: {
        title: 'Observabilidade',
        description: 'jcmd, thread dumps e Java Flight Recorder ajudam a diagnosticar execução e concorrência.',
        code: 'jcmd -l\njcmd <PID> Thread.dump_to_file -format=plain threads.txt',
        tip: 'O PID deve ser substituído pelo identificador do processo Java.'
    }
};
const STEPS = ['source', 'compiler', 'bytecode', 'loader', 'verifier', 'jit', 'jvm'];
let step = 0;

function showTopic(key) {
    const t = TOPICS[key];
    if (!t) return;
    document.querySelector('#topicTitle').textContent = t.title;
    document.querySelector('#topicDescription').textContent = t.description;
    document.querySelector('#topicCode').textContent = t.code;
    document.querySelector('#topicTip').textContent = t.tip;
    document.querySelectorAll('[data-topic]').forEach(b => {
        const on = b.dataset.topic === key;
        b.classList.toggle('active', on);
        b.setAttribute('aria-pressed', String(on));
    });
}

function renderStep() {
    showTopic(STEPS[step]);
    const el = document.querySelector('#stepStatus');
    if (el) el.textContent = `Etapa ${step + 1} de ${STEPS.length}: ${TOPICS[STEPS[step]].title}`;
    document.querySelector('#prev').disabled = step === 0;
    document.querySelector('#next').disabled = step === STEPS.length - 1;
}

document.querySelectorAll('[data-topic]').forEach(b => b.addEventListener('click', () => showTopic(b.dataset.topic)));
document.querySelector('#prev').addEventListener('click', () => {
    step = Math.max(0, step - 1);
    renderStep()
});
document.querySelector('#next').addEventListener('click', () => {
    step = Math.min(STEPS.length - 1, step + 1);
    renderStep()
});
document.querySelector('#reset').addEventListener('click', () => {
    step = 0;
    renderStep()
});
document.querySelectorAll('[data-view]').forEach(button => button.addEventListener('click', () => {
    const view = button.dataset.view;
    document.querySelectorAll('[data-section]').forEach(section => {
        section.hidden = view !== 'all' && section.dataset.section !== view;
    });
    document.querySelectorAll('[data-view]').forEach(b => b.setAttribute('aria-pressed', String(b === button)));
    if (view === 'jvm') showTopic('loader');
    if (view === 'threads') showTopic('thread');
    if (view === 'all') renderStep();
    document.querySelector('main').scrollIntoView({behavior: 'auto', block: 'start'});
}));
renderStep();
document.getElementById("jsHealth").textContent = "Interatividade ativada. Clique nos componentes e use Próxima → para simular a execução.";


/*
 * Etapa 3.9
 * Integração da interface principal
 * com a execução real de Threads Java 21.
 */

const botaoThreadReal =
    document.getElementById("executarThreadReal");

const statusThreadReal =
    document.getElementById("statusThreadReal");

const resultadoThreadReal =
    document.getElementById("resultadoThreadReal");

if (botaoThreadReal &&
    statusThreadReal &&
    resultadoThreadReal) {

    botaoThreadReal.addEventListener(
        "click",
        async () => {

            botaoThreadReal.disabled = true;
            statusThreadReal.textContent =
                "Executando Thread na JVM...";

            resultadoThreadReal.textContent =
                "Aguardando resposta da API.";

            try {
                const resposta = await fetch(
                    "/api/threads/executar"
                );

                if (!resposta.ok) {
                    throw new Error(
                        "Erro HTTP " + resposta.status
                    );
                }

                const dados = await resposta.json();

                resultadoThreadReal.textContent =
                    JSON.stringify(dados, null, 2);

                statusThreadReal.textContent =
                    "Execução concluída com sucesso.";

            } catch (erro) {

                statusThreadReal.textContent =
                    "Falha na execução.";

                resultadoThreadReal.textContent =
                    erro.message;

            } finally {

                botaoThreadReal.disabled = false;

            }
        }
    );
}
// ETAPA 3.11 - RACE CONDITION REAL

document.addEventListener("DOMContentLoaded", () => {

    const botao = document.getElementById(
        "executarRaceCondition"
    );

    const status = document.getElementById(
        "statusRaceCondition"
    );

    const resultado = document.getElementById(
        "resultadoRaceCondition"
    );

    const comparativo = document.getElementById(
        "comparativoRaceCondition"
    );

    if (!botao || !status || !resultado || !comparativo) {
        return;
    }

    const formatar = valor =>
        Number(valor).toLocaleString("pt-BR");

    botao.addEventListener("click", async () => {

        botao.disabled = true;
        status.textContent = "Executando threads na JVM...";
        resultado.textContent = "Aguardando resposta da API...";
        comparativo.hidden = true;

        try {

            const resposta = await fetch(
                "/api/threads/race-condition",
                { cache: "no-store" }
            );

            if (!resposta.ok) {
                throw new Error(
                    `Erro HTTP ${resposta.status}`
                );
            }

            const dados = await resposta.json();

            const esperado = Number(
                dados.resultadoEsperado
            );

            const semSync = Number(
                dados.semSincronizacao
            );

            const comSync = Number(
                dados.comSincronizacao
            );

            const perdidas = Number(
                dados.atualizacoesPerdidas
            );

            if (
                ![esperado, semSync, comSync, perdidas]
                    .every(Number.isFinite) ||
                esperado <= 0
            ) {
                throw new Error(
                    "A API retornou dados inválidos."
                );
            }

            const percentual = (
                (perdidas / esperado) * 100
            ).toFixed(2);

            document.getElementById(
                "raceEsperado"
            ).textContent = formatar(esperado);

            document.getElementById(
                "raceSemSync"
            ).textContent = formatar(semSync);

            document.getElementById(
                "raceComSync"
            ).textContent = formatar(comSync);

            document.getElementById(
                "racePerdidas"
            ).textContent = formatar(perdidas);

            document.getElementById(
                "racePercentual"
            ).textContent = percentual.replace(".", ",") + "%";

            resultado.textContent = JSON.stringify(
                dados,
                null,
                2
            );

            comparativo.hidden = false;

            status.textContent =
                "Execução concluída. Resultados reais recebidos da JVM.";

        } catch (erro) {

            status.textContent =
                "Falha ao executar Race Condition.";

            resultado.textContent = erro.message;

            console.error(
                "Erro no laboratório Race Condition:",
                erro
            );

        } finally {

            botao.disabled = false;

        }

    });

});

// NAVEGAÇÃO PARA OS LABORATÓRIOS REAIS

document.addEventListener("DOMContentLoaded", () => {

    const botao = document.getElementById(
        "irParaLaboratorios"
    );

    const destino = document.getElementById(
        "laboratorio-real"
    );

    if (!botao || !destino) {
        return;
    }

    botao.addEventListener("click", () => {

        // Exibe todas as seções caso algum módulo
        // tenha sido selecionado anteriormente.
        const mapa = document.querySelector(
            '[data-view="all"]'
        );

        if (mapa) {
            mapa.click();
        }

        // Navega até o primeiro laboratório real.
        destino.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

    });

});

/* =====================================================
   ETAPA 3.13 — REENTRANTLOCK / TRYLOCK
   Navegação e execução real via Spring Boot
   ===================================================== */

(function iniciarLaboratorioReentrantLock() {

    function configurar() {

        // BOTÃO SUPERIOR — NAVEGAÇÃO
        const botaoNavegacao =
            document.getElementById("irParaReentrantLock");

        const painel =
            document.getElementById("laboratorio-lock");

        if (botaoNavegacao && painel) {

            botaoNavegacao.addEventListener("click", function () {

                painel.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });

            });
        }

        // BOTÃO DE EXECUÇÃO REAL
        const botaoExecutar =
            document.getElementById("executarLockReal");

        const status =
            document.getElementById("statusLockReal");

        const resultado =
            document.getElementById("resultadoLockReal");

        const comparativo =
            document.getElementById("comparativoLock");

        if (!botaoExecutar || !status ||
            !resultado || !comparativo) {
            return;
        }

        botaoExecutar.addEventListener("click", async function () {

            botaoExecutar.disabled = true;
            status.textContent = "Executando ReentrantLock na JVM...";

            try {

                const resposta = await fetch(
                    "/api/threads/reentrant-lock",
                    { cache: "no-store" }
                );

                if (!resposta.ok) {
                    throw new Error("Erro HTTP: " + resposta.status);
                }

                const dados = await resposta.json();

                // INDICADORES
                document.getElementById("lockPrimeira").textContent =
                    String(dados.primeiraTentativa);

                document.getElementById("lockSegunda").textContent =
                    String(dados.segundaTentativa);

                document.getElementById("lockLiberado").textContent =
                    String(dados.lockLiberado);

                document.getElementById("lockExecucao").textContent =
                    dados.execucao ?? "Não informado";

                comparativo.hidden = false;

                // RESPOSTA JSON COMPLETA
                resultado.textContent =
                    JSON.stringify(dados, null, 2);

                status.textContent =
                    "Execução concluída com sucesso.";

            } catch (erro) {

                status.textContent =
                    "Falha na execução: " + erro.message;

                resultado.textContent =
                    "Não foi possível consultar a API.";

                comparativo.hidden = true;

                console.error(
                    "Erro no laboratório ReentrantLock:",
                    erro
                );

            } finally {

                botaoExecutar.disabled = false;

            }

        });

    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", configurar);
    } else {
        configurar();
    }

})();


/* =====================================================
   ETAPA 3.14 — VOLATILE E ATOMICINTEGER
   Navegação e execução real via Spring Boot
   ===================================================== */

(function iniciarLaboratorioAtomicInteger() {

    function configurar() {

        // BOTÃO SUPERIOR — NAVEGAÇÃO
        const botaoNavegacao =
            document.getElementById("irParaAtomicInteger");

        const painel =
            document.getElementById("laboratorio-atomic");

        if (botaoNavegacao && painel) {
            botaoNavegacao.addEventListener("click", function () {
                painel.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });
            });
        }

        // ELEMENTOS DO LABORATÓRIO
        const botaoExecutar =
            document.getElementById("executarAtomicInteger");

        const status =
            document.getElementById("statusAtomicInteger");

        const resultado =
            document.getElementById("resultadoAtomicInteger");

        const comparativo =
            document.getElementById("comparativoAtomicInteger");

        if (!botaoExecutar || !status ||
            !resultado || !comparativo) {
            return;
        }

        botaoExecutar.addEventListener("click", async function () {

            botaoExecutar.disabled = true;
            status.textContent = "Executando AtomicInteger na JVM...";

            try {
                const resposta = await fetch(
                    "/api/threads/atomic-integer",
                    { cache: "no-store" }
                );

                if (!resposta.ok) {
                    throw new Error("Erro HTTP: " + resposta.status);
                }

                const dados = await resposta.json();

                // INDICADORES
                document.getElementById("atomicEsperado").textContent =
                    dados.resultadoEsperado.toLocaleString("pt-BR");

                document.getElementById("atomicVolatile").textContent =
                    dados.contadorVolatile.toLocaleString("pt-BR");

                document.getElementById("atomicResultado").textContent =
                    dados.contadorAtomicInteger.toLocaleString("pt-BR");

                document.getElementById("atomicPerdidas").textContent =
                    dados.atualizacoesPerdidasVolatile.toLocaleString("pt-BR");

                document.getElementById("atomicCorreto").textContent =
                    String(dados.atomicIntegerCorreto);

                comparativo.hidden = false;

                // JSON COMPLETO
                resultado.textContent =
                    JSON.stringify(dados, null, 2);

                status.textContent =
                    "Execução concluída com sucesso.";

            } catch (erro) {
                status.textContent =
                    "Falha na execução: " + erro.message;

                resultado.textContent =
                    "Não foi possível consultar a API.";

                comparativo.hidden = true;

                console.error(
                    "Erro no laboratório AtomicInteger:",
                    erro
                );

            } finally {
                botaoExecutar.disabled = false;
            }
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", configurar);
    } else {
        configurar();
    }

})();


/* =====================================================
   ETAPA 3.15 — VIRTUAL THREADS JAVA 21
   Navegação e execução real via Spring Boot
   ===================================================== */

(function iniciarLaboratorioVirtualThreads() {

    function configurar() {

        // 1. BOTÃO SUPERIOR — NAVEGAÇÃO
        const botaoNavegacao =
            document.getElementById("irParaVirtualThreads");

        const painel =
            document.getElementById("laboratorio-virtual");

        if (botaoNavegacao && painel) {

            botaoNavegacao.addEventListener("click", function () {

                painel.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });

            });
        }

        // 2. ELEMENTOS DO LABORATÓRIO
        const botaoExecutar =
            document.getElementById("executarVirtualThreads");

        const status =
            document.getElementById("statusVirtualThreads");

        const resultado =
            document.getElementById("resultadoVirtualThreads");

        const comparativo =
            document.getElementById("comparativoVirtualThreads");

        if (!botaoExecutar || !status ||
            !resultado || !comparativo) {
            return;
        }

        // 3. EXECUÇÃO DA API
        botaoExecutar.addEventListener("click", async function () {

            botaoExecutar.disabled = true;

            status.textContent =
                "Executando Platform Threads e Virtual Threads na JVM...";

            try {

                const resposta = await fetch(
                    "/api/threads/virtual-threads",
                    { cache: "no-store" }
                );

                if (!resposta.ok) {
                    throw new Error("Erro HTTP: " + resposta.status);
                }

                const dados = await resposta.json();

                // 4. INDICADORES
                document.getElementById("virtualTarefas").textContent =
                    dados.tarefas.toLocaleString("pt-BR");

                document.getElementById("virtualEspera").textContent =
                    dados.esperaPorTarefaMs + " ms";

                document.getElementById("virtualPlatform").textContent =
                    dados.platformThreads;

                document.getElementById("virtualQuantidade").textContent =
                    dados.virtualThreads;

                document.getElementById("virtualTempoPlatform").textContent =
                    dados.tempoPlatformMs.toLocaleString("pt-BR") + " ms";

                document.getElementById("virtualTempoVirtual").textContent =
                    dados.tempoVirtualMs.toLocaleString("pt-BR") + " ms";

                // 5. EXIBIR INDICADORES
                comparativo.hidden = false;

                // 6. EXIBIR JSON COMPLETO
                resultado.textContent =
                    JSON.stringify(dados, null, 2);

                status.textContent =
                    "Execução concluída com sucesso.";

            } catch (erro) {

                status.textContent =
                    "Falha na execução: " + erro.message;

                resultado.textContent =
                    "Não foi possível consultar a API.";

                comparativo.hidden = true;

                console.error(
                    "Erro no laboratório Virtual Threads:",
                    erro
                );

            } finally {

                botaoExecutar.disabled = false;

            }

        });

    }

    // 7. INICIALIZAÇÃO SEGURA
    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            configurar
        );

    } else {

        configurar();

    }

})();


/* =====================================================
   ETAPA 3.16 — DIAGNOSTICO DE THREADS JAVA 21
   Navegacao e execucao real via Spring Boot
   ===================================================== */

(function iniciarLaboratorioDiagnostico() {

    function configurar() {

        // 1. BOTAO SUPERIOR — NAVEGACAO

        const botaoNavegacao =
            document.getElementById("irParaDiagnostico");

        const painel =
            document.getElementById("laboratorio-diagnostico");

        if (botaoNavegacao && painel) {

            botaoNavegacao.addEventListener("click", function () {

                painel.scrollIntoView({
                    behavior: "smooth",
                    block: "start"
                });

            });
        }

        // 2. ELEMENTOS DO LABORATORIO

        const botaoExecutar =
            document.getElementById("executarDiagnostico");

        const status =
            document.getElementById("statusDiagnostico");

        const resultado =
            document.getElementById("resultadoDiagnostico");

        const comparativo =
            document.getElementById("comparativoDiagnostico");

        if (!botaoExecutar || !status ||
            !resultado || !comparativo) {
            return;
        }

        // 3. EXECUCAO DA API

        botaoExecutar.addEventListener("click", async function () {

            botaoExecutar.disabled = true;

            status.textContent =
                "Executando diagnostico de threads na JVM...";

            try {

                const resposta = await fetch(
                    "/api/threads/diagnostico",
                    { cache: "no-store" }
                );

                if (!resposta.ok) {
                    throw new Error("Erro HTTP: " + resposta.status);
                }

                const dados = await resposta.json();

                // 4. PREENCHIMENTO DOS INDICADORES

                const indicadores = {
                    diagnosticoThreads: dados.threadsCriadas,
                    diagnosticoInicialA: dados.estadoInicialA,
                    diagnosticoInicialB: dados.estadoInicialB,
                    diagnosticoDuranteA: dados.estadoDuranteA,
                    diagnosticoDuranteB: dados.estadoDuranteB,
                    diagnosticoFinalA: dados.estadoFinalA,
                    diagnosticoFinalB: dados.estadoFinalB,
                    diagnosticoPilhaA: dados.elementosPilhaA,
                    diagnosticoPilhaB: dados.elementosPilhaB,
                    diagnosticoEspera: String(dados.esperaConfirmada)
                };

                for (const [id, valor] of Object.entries(indicadores)) {

                    const elemento = document.getElementById(id);

                    if (elemento) {
                        elemento.textContent = valor ?? "—";
                    }
                }

                // 5. EXIBIR INDICADORES

                comparativo.hidden = false;

                // 6. EXIBIR JSON COMPLETO

                resultado.textContent =
                    JSON.stringify(dados, null, 2);

                status.textContent =
                    "Execução concluída com sucesso.";

            } catch (erro) {

                status.textContent =
                    "Falha na execução: " + erro.message;

                resultado.textContent =
                    "Não foi possível consultar a API.";

                comparativo.hidden = true;

                console.error(
                    "Erro no laboratorio de diagnostico:",
                    erro
                );

            } finally {

                botaoExecutar.disabled = false;

            }

        });

    }

    // 7. INICIALIZACAO SEGURA

    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            configurar
        );

    } else {

        configurar();

    }

})();
