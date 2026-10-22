"use strict";

// Módulo 3 — Etapa 3.17.5.5
// Bytecode real obtido da API; execução simulada em JavaScript.

document.addEventListener("DOMContentLoaded", () => {

    const experimentos = {
        soma: {
            endpoint: "/api/bytecode/aritmetica",
            identificacao: "EXPERIMENTO 3.17.1-A",
            titulo: "Variáveis e operações aritméticas",
            codigo: "int a = 1;\nint b = 2;\nint resultado = a + b;\n\nSystem.out.println(resultado);",
            variaveis: { 1: "a", 2: "b", 3: "resultado" }
        },

        matematica: {
            endpoint: "/api/bytecode/matematica",
            identificacao: "EXPERIMENTO 3.17.1-B",
            titulo: "Multiplicação e subtração",
            codigo: "int a = 4;\nint b = 3;\nint c = 2;\n\nint resultado = a * b - c;\n\nSystem.out.println(resultado);",
            variaveis: { 1: "a", 2: "b", 3: "c", 4: "resultado" }
        },


        decisao: {
            endpoint: "/api/bytecode/decisao",
            identificacao: "EXPERIMENTO 3.17.2",
            titulo: "Estruturas de decisão — if/else",
            codigo: "int a = 3;\nint b = 5;\nint resultado;\n\nif (a > b) {\n    resultado = a;\n} else {\n    resultado = b;\n}\n\nSystem.out.println(resultado);",
            variaveis: { 1: "a", 2: "b", 3: "resultado" }
        },

        race: {
            endpoint: "/api/bytecode/race-condition",
            identificacao: "EXPERIMENTO 3.17.3-A",
            titulo: "Race Condition — sem sincronização",
            codigo: "private static int contador = 0;\n\n// Executado por 4 threads, 100.000 vezes cada\ncontador++;\n\n// Método compilado: lambda$main$0",
            variaveis: { 0: "contador compartilhado (didático)" },
            modoRace: true,
            sincronizacaoEsperada: false
        },

        raceSincronizada: {
            endpoint: "/api/bytecode/race-condition-sincronizada",
            identificacao: "EXPERIMENTO 3.17.3-B",
            titulo: "Race Condition — com synchronized",
            codigo: "private static int contador = 0;\n\nprivate static synchronized void incrementar() {\n    contador++;\n}\n\n// Método compilado: incrementar",
            variaveis: { 0: "contador compartilhado (didático)" },
            modoRace: true,
            sincronizacaoEsperada: true
        },

        objetos: {
            endpoint: "/api/bytecode/objetos",
            identificacao: "EXPERIMENTO 3.17.4",
            titulo: "Criação de objetos e referências na JVM",
            codigo: 'Pessoa pessoa = new Pessoa("Claudio", 53);\npessoa.apresentar();',
            variaveis: { 0: "args", 1: "pessoa" },
            modoObjetos: true
        },


        sincronizacao: {
            endpoint: "/api/bytecode/sincronizacao",
            identificacao: "EXPERIMENTO 3.17.5",
            titulo: "Sincronização — monitorenter e monitorexit",
            codigo: "private static int contador = 0;\nprivate static final Object lock = new Object();\n\npublic static void incrementar() {\n    synchronized (lock) {\n        contador++;\n    }\n}",
            variaveis: {
                0: "lock (slot 0)",
                1: "exceção (slot 1)",
                contador: "contador compartilhado",
                monitor: "monitor lock"
            },
            modoMonitor: true
        },

        deadlock: {
            endpoint: "/api/bytecode/deadlock",
            identificacao: "EXPERIMENTO 3.17.6",
            titulo: "Diagnóstico de Deadlock na JVM",
            codigo: "// Thread A\nsynchronized (LOCK_1) {\n    BARREIRA.countDown();\n    aguardarBarreira();\n    synchronized (LOCK_2) { }\n}\n\n// Thread B\nsynchronized (LOCK_2) {\n    BARREIRA.countDown();\n    aguardarBarreira();\n    synchronized (LOCK_1) { }\n}",
            variaveis: {
                0: "monitor externo (slot 0)",
                1: "monitor interno (slot 1)",
                LOCK_1: "Estado de LOCK_1",
                LOCK_2: "Estado de LOCK_2"
            },
            modoDeadlock: true
        }

    };

    const porId = id => document.getElementById(id);

    const lista = porId("listaInstrucoes");
    const instrucaoAtual = porId("instrucaoAtual");
    const hexAtual = porId("hexAtual");
    const binAtual = porId("binAtual");
    const pilhaOperandos = porId("pilhaOperandos");
    const variaveisLocais = porId("variaveisLocais");
    const explicacao = porId("explicacaoInstrucao");
    const contador = porId("contadorPassos");

    const btnAnterior = porId("btnAnterior");
    const btnProximo = porId("btnProximo");
    const btnReiniciar = porId("btnReiniciar");

    const btnSoma = porId("btnExperimentoSoma");
    const btnMatematica = porId("btnExperimentoMatematica");

    // Este botão será criado no HTML na próxima etapa.
    const btnDecisao = porId("btnExperimentoDecisao");
    // Botões serão inseridos no HTML na etapa seguinte.
    const btnRace = porId("btnExperimentoRace");
    const btnRaceSincronizada = porId("btnExperimentoRaceSincronizada");
    const btnObjetos = porId("btnExperimentoObjetos");
    const btnSincronizacao = porId("btnExperimentoSincronizacao");

    // Experimento 3.17.6 — Deadlock
    const btnDeadlock = porId("btnExperimentoDeadlock");
    const painelDeadlock = porId("painelExperimentoDeadlock");
    const btnDeadlockThreadA = porId("btnDeadlockThreadA");
    const btnDeadlockThreadB = porId("btnDeadlockThreadB");
    const estadoDeadlock = porId("estadoDeadlock");



    const codigoFonte = porId("codigoFonteJava");
    const identificacao = porId("identificacaoExperimento");
    const titulo = porId("lab-title");
    const sequenciaHexadecimal = porId("sequenciaHexadecimal");


    const btnNavegacaoAritmetica = document.getElementById(
        "btnNavegacaoAritmetica"
    );

    if (btnNavegacaoAritmetica) {
        btnNavegacaoAritmetica.addEventListener("click", () => {
            carregarExperimento("soma");
        });
    }


    let experimentoAtual = "soma";
    let passos = [];
    let indiceAtual = 0;
    let requisicaoAtual = 0;
    let dadosDeadlock = null;
    let threadDeadlockAtual = "A";

    // ==========================================================
    // LEITURA DOS OPERANDOS
    // ==========================================================

    function numeroOperando(item) {

        const primeiro = String(item.operandos ?? "")
            .trim()
            .split(/\s+/)[0];

        if (!/^-?\d+$/.test(primeiro)) {
            throw new Error(
                `Operando numérico inválido: ${item.opcode} ${item.operandos}`
            );
        }

        return Number(primeiro);
    }

    function indiceLocal(item) {

        const sufixo = /^(?:iload|istore)_([0-3])$/
            .exec(item.opcode);

        const indice = sufixo
            ? Number(sufixo[1])
            : numeroOperando(item);

        if (!Number.isInteger(indice)
            || indice < 0
            || indice > 255) {

            throw new Error(
                `Índice local inválido: ${indice}`
            );
        }

        return indice;
    }


    function nomeInstrucao(item) {

        const opcode = item.opcode ?? item.instrucao;

        if (!opcode) {
            return "Instrução não identificada";
        }

        if ([
            "iload",
            "istore",
            "bipush",
            "if_icmple",
            "goto"
        ].includes(opcode)) {

            return `${opcode} ${numeroOperando(item)}`;
        }

        return opcode;
    }


    // ==========================================================
    // INTERPRETADOR DIDÁTICO DO BYTECODE
    // ==========================================================

    function interpretar(instrucoes) {

        const offsets = new Map();

        instrucoes.forEach((item, indice) => {

            if (!Number.isInteger(item.offset)
                || offsets.has(item.offset)) {

                throw new Error(
                    `Offset inválido ou duplicado: ${item.offset}`
                );
            }

            offsets.set(item.offset, indice);
        });

        const pilha = [];
        const locais = {};
        const historico = [];

        let saida = null;
        let posicao = 0;
        let terminou = false;

        // Proteção contra ciclos infinitos.
        const limite = 10000;

        while (posicao < instrucoes.length && !terminou) {

            if (historico.length >= limite) {

                throw new Error(
                    "Limite de passos excedido; possível loop no bytecode."
                );
            }

            const item = instrucoes[posicao];
            const opcode = item.opcode;
            const operandos = String(item.operandos ?? "");

            let descricao = "";
            let proximaPosicao = posicao + 1;

            // --------------------------------------------------
            // CONSTANTES INTEIRAS
            // --------------------------------------------------

            if (
                opcode === "iconst_m1"
                || /^iconst_[0-5]$/.test(opcode)
            ) {

                const valor = opcode === "iconst_m1"
                    ? -1
                    : Number(opcode.split("_")[1]);

                pilha.push(valor);

                descricao = `Empilha o inteiro ${valor}.`;

            } else if (opcode === "bipush") {

                const valor = numeroOperando(item);

                if (valor < -128 || valor > 127) {

                    throw new Error(
                        "bipush fora do intervalo de 8 bits."
                    );
                }

                pilha.push(valor);

                descricao =
                    `Empilha o inteiro ${valor} (bipush).`;

                // --------------------------------------------------
                // VARIÁVEIS LOCAIS — ARMAZENAMENTO
                // --------------------------------------------------

            } else if (
                /^istore_[0-3]$/.test(opcode)
                || opcode === "istore"
            ) {

                const indice = indiceLocal(item);

                if (
                    !pilha.length
                    || typeof pilha[pilha.length - 1] !== "number"
                ) {

                    throw new Error(
                        `Pilha incompatível com ${opcode}.`
                    );
                }

                const valor = pilha.pop();

                locais[indice] = valor;

                descricao =
                    `Armazena ${valor} no slot ${indice}.`;

                // --------------------------------------------------
                // VARIÁVEIS LOCAIS — CARREGAMENTO
                // --------------------------------------------------

            } else if (
                /^iload_[0-3]$/.test(opcode)
                || opcode === "iload"
            ) {

                const indice = indiceLocal(item);

                if (
                    !Object.prototype.hasOwnProperty.call(
                        locais,
                        indice
                    )
                ) {

                    throw new Error(
                        `Slot ${indice} não inicializado.`
                    );
                }

                pilha.push(locais[indice]);

                descricao =
                    `Carrega ${locais[indice]} do slot ${indice}.`;

                // --------------------------------------------------
                // OPERAÇÕES ARITMÉTICAS
                // --------------------------------------------------

            } else if (
                ["iadd", "isub", "imul"].includes(opcode)
            ) {

                if (
                    pilha.length < 2
                    || typeof pilha[pilha.length - 1] !== "number"
                    || typeof pilha[pilha.length - 2] !== "number"
                ) {

                    throw new Error(
                        `Pilha incompatível com ${opcode}.`
                    );
                }

                const b = pilha.pop();
                const a = pilha.pop();

                const valor = opcode === "iadd"
                    ? (a + b) | 0
                    : opcode === "isub"
                        ? (a - b) | 0
                        : Math.imul(a, b);

                pilha.push(valor);

                const operador = {
                    iadd: "+",
                    isub: "-",
                    imul: "×"
                }[opcode];

                descricao =
                    `Calcula ${a} ${operador} ${b} = ${valor} ` +
                    "e empilha o resultado.";

                // --------------------------------------------------
                // DESVIO CONDICIONAL — if_icmple
                // --------------------------------------------------

            } else if (opcode === "if_icmple") {

                if (
                    pilha.length < 2
                    || typeof pilha[pilha.length - 1] !== "number"
                    || typeof pilha[pilha.length - 2] !== "number"
                ) {

                    throw new Error(
                        "Pilha incompatível com if_icmple."
                    );
                }

                const b = pilha.pop();
                const a = pilha.pop();

                const destino = numeroOperando(item);

                if (a <= b) {

                    if (!offsets.has(destino)) {

                        throw new Error(
                            `Destino de desvio inexistente: ${destino}`
                        );
                    }

                    proximaPosicao = offsets.get(destino);

                    descricao =
                        `${a} <= ${b}: verdadeiro. ` +
                        `Desvia para o offset ${destino}.`;

                } else {

                    descricao =
                        `${a} <= ${b}: falso. ` +
                        "Continua na próxima instrução.";
                }

                // --------------------------------------------------
                // DESVIO INCONDICIONAL — goto
                // --------------------------------------------------

            } else if (opcode === "goto") {

                const destino = numeroOperando(item);

                if (!offsets.has(destino)) {

                    throw new Error(
                        `Destino de goto inexistente: ${destino}`
                    );
                }

                proximaPosicao = offsets.get(destino);

                descricao =
                    `Desvio incondicional para o offset ${destino}.`;

                // --------------------------------------------------
                // ACESSO A System.out
                // --------------------------------------------------

            } else if (opcode === "getstatic") {

                if (
                    !operandos.includes("java/lang/System.out")
                ) {

                    throw new Error(
                        "Referência getstatic não suportada."
                    );
                }

                pilha.push({
                    referencia: "System.out"
                });

                descricao =
                    "Carrega a referência System.out.";

                // --------------------------------------------------
                // CHAMADA DE println(int)
                // --------------------------------------------------

            } else if (opcode === "invokevirtual") {

                if (
                    !operandos.includes(
                        "java/io/PrintStream.println:(I)V"
                    )
                ) {

                    throw new Error(
                        "Método invokevirtual não suportado."
                    );
                }

                if (
                    pilha.length < 2
                    || typeof pilha[pilha.length - 1] !== "number"
                    || pilha[pilha.length - 2]?.referencia
                    !== "System.out"
                ) {

                    throw new Error(
                        "Pilha incompatível com println(int)."
                    );
                }

                const valor = pilha.pop();

                pilha.pop();

                saida = valor;

                descricao =
                    `Simula System.out.println(${valor}).`;

                // --------------------------------------------------
                // FINALIZAÇÃO DO MÉTODO
                // --------------------------------------------------

            } else if (opcode === "return") {

                descricao =
                    "Finaliza a execução do método main.";

                terminou = true;

            } else {

                throw new Error(
                    `Instrução ainda não suportada: ` +
                    `${opcode} (offset ${item.offset}).`
                );
            }

            // Registra somente as instruções executadas.
            historico.push({
                offset: item.offset,
                instrucao: opcode,
                operandos,
                hex: item.hexadecimal,
                bin: item.binario,
                pilha: [...pilha],
                locais: { ...locais },
                explicacao: descricao,
                saida
            });

            posicao = proximaPosicao;
        }

        if (!terminou) {

            throw new Error(
                "Método terminou sem instrução return."
            );
        }

        return historico;
    }

    // ==========================================================
    // RACE CONDITION — UM INCREMENTO DIDÁTICO
    // ==========================================================
    // Não executa threads nem reproduz as 100.000 iterações.
    // As instruções são extraídas do .class real pela API.
    function interpretarIncrementoDidatico(instrucoes, sincronizado) {
        const inicio = instrucoes.findIndex(item =>
            item.opcode === "getstatic"
            && String(item.operandos).includes("Field contador:I")
        );

        if (inicio < 0) {
            throw new Error("Leitura do contador não encontrada no Bytecode.");
        }

        const trecho = instrucoes.slice(inicio, inicio + 4);
        const opcodes = trecho.map(item => item.opcode);

        if (opcodes.join(",") !== "getstatic,iconst_1,iadd,putstatic") {
            throw new Error("Sequência de incremento inesperada no Bytecode.");
        }

        const pilha = [];
        const locais = { 0: 0 };
        const historico = [];
        const contexto = sincronizado
            ? "Método protegido por ACC_SYNCHRONIZED. "
            : "Método sem ACC_SYNCHRONIZED. ";

        trecho.forEach(item => {
            let descricao;
            switch (item.opcode) {
                case "getstatic":
                    pilha.push(locais[0]);
                    descricao = "Lê o contador compartilhado (valor didático inicial: 0).";
                    break;
                case "iconst_1":
                    pilha.push(1);
                    descricao = "Empilha a constante inteira 1.";
                    break;
                case "iadd": {
                    const b = pilha.pop();
                    const a = pilha.pop();
                    pilha.push((a + b) | 0);
                    descricao = `Calcula ${a} + ${b} e empilha o resultado.`;
                    break;
                }
                case "putstatic":
                    locais[0] = pilha.pop();
                    descricao = `Grava ${locais[0]} no contador compartilhado.`;
                    break;
            }

            historico.push({
                offset: item.offset,
                instrucao: item.opcode,
                operandos: item.operandos ?? "",
                hex: item.hexadecimal,
                bin: item.binario,
                pilha: [...pilha],
                locais: { ...locais },
                explicacao: contexto + descricao
                    + " Demonstração de uma única iteração, sem threads reais.",
                saida: null
            });
        });

        return historico;
    }

    // ==========================================================
    // OBJETOS — EXECUÇÃO DIDÁTICA DO main() DE Pessoa
    // ==========================================================
    // A sequência e os bytes são reais, obtidos da API.
    // O heap e as referências são modelos didáticos, não
    // uma inspeção da memória real da JVM.
    function interpretarObjetos(instrucoes) {
        const esperados = [
            "new", "dup", "ldc", "bipush", "invokespecial",
            "astore_1", "aload_1", "invokevirtual", "return"
        ];

        if (instrucoes.length !== esperados.length ||
            instrucoes.some((item, i) => item.opcode !== esperados[i])) {
            throw new Error("Sequência de Pessoa.main diferente da esperada.");
        }

        if (!String(instrucoes[0].operandos).includes("class Pessoa") ||
            !String(instrucoes[4].operandos).includes('Method "<init>"') ||
            !String(instrucoes[7].operandos).includes("apresentar:()V")) {
            throw new Error("Referências de classe ou métodos inesperadas.");
        }

        const textoString = String(instrucoes[2].operandos);
        const nomeEncontrado = /\/\/ String (.*)$/.exec(textoString);
        if (!nomeEncontrado) {
            throw new Error("Constante String não identificada no ldc.");
        }
        const nome = nomeEncontrado[1];
        const idade = numeroOperando(instrucoes[3]);

        const referencia = { referencia: "Pessoa@1", inicializado: false };
        const pilha = [];
        const locais = { 0: "String[] args" };
        const historico = [];
        let saida = null;

        const mostrar = valor =>
            valor && typeof valor === "object" ? valor.referencia : String(valor);

        instrucoes.forEach(item => {
            let descricao = "";

            switch (item.opcode) {
                case "new":
                    pilha.push(referencia);
                    descricao = "Reserva uma instância Pessoa no heap didático e empilha uma referência ainda não inicializada.";
                    break;
                case "dup":
                    pilha.push(pilha[pilha.length - 1]);
                    descricao = "Duplica a referência no topo da pilha; ambas apontam para o mesmo objeto.";
                    break;
                case "ldc":
                    pilha.push(nome);
                    descricao = `Carrega a String \"${nome}\" do Constant Pool.`;
                    break;
                case "bipush":
                    pilha.push(idade);
                    descricao = `Empilha o argumento inteiro ${idade}.`;
                    break;
                case "invokespecial": {
                    const argumentoIdade = pilha.pop();
                    const argumentoNome = pilha.pop();
                    const alvo = pilha.pop();
                    if (alvo !== referencia || argumentoNome !== nome || argumentoIdade !== idade) {
                        throw new Error("Pilha inválida na chamada do construtor.");
                    }
                    referencia.inicializado = true;
                    descricao = `Executa Pessoa(\"${nome}\", ${idade}); inicializa os campos nome e idade. Uma referência permanece na pilha.`;
                    break;
                }
                case "astore_1":
                    locais[1] = pilha.pop();
                    descricao = "Armazena a referência Pessoa@1 na variável local pessoa (slot 1).";
                    break;
                case "aload_1":
                    if (locais[1] !== referencia) {
                        throw new Error("Referência pessoa não encontrada no slot 1.");
                    }
                    pilha.push(locais[1]);
                    descricao = "Carrega a referência pessoa do slot 1 para a pilha.";
                    break;
                case "invokevirtual":
                    if (pilha.pop() !== referencia || !referencia.inicializado) {
                        throw new Error("Objeto inválido na chamada apresentar().");
                    }
                    saida = `Nome: ${nome} | Idade: ${idade}`;
                    descricao = `Invoca Pessoa.apresentar(). Saída simulada: ${saida}`;
                    break;
                case "return":
                    descricao = "Finaliza o método main().";
                    break;
            }

            historico.push({
                offset: item.offset,
                instrucao: item.opcode,
                operandos: item.operandos ?? "",
                hex: item.hexadecimal,
                bin: item.binario,
                pilha: pilha.map(mostrar),
                locais: Object.fromEntries(
                    Object.entries(locais).map(([chave, valor]) => [chave, mostrar(valor)])
                ),
                explicacao: descricao,
                saida
            });
        });

        return historico;
    }

    // ==========================================================
    // MONITORES — UMA EXECUÇÃO NORMAL DE incrementar()
    // ==========================================================
    // Simula uma única invocação sem concorrência real.
    // O fluxo excepcional consta no bytecode, mas não é executado.
    function interpretarMonitor(instrucoes) {
        const porOffset = new Map(instrucoes.map(item => [item.offset, item]));
        const esperado = [0, 3, 4, 5, 6, 9, 10, 11, 14, 15, 16, 24];
        const esperadoOpcodes = [
            "getstatic", "dup", "astore_0", "monitorenter",
            "getstatic", "iconst_1", "iadd", "putstatic",
            "aload_0", "monitorexit", "goto", "return"
        ];
        if (instrucoes.length !== 17 || esperado.some((offset, i) =>
            porOffset.get(offset)?.opcode !== esperadoOpcodes[i])) {
            throw new Error("Bytecode de incrementar() diferente da sequência esperada.");
        }
        if (porOffset.get(16)?.operandos?.trim() !== "24" ||
            !String(porOffset.get(0).operandos).includes("Field lock:") ||
            !String(porOffset.get(6).operandos).includes("Field contador:I") ||
            !String(porOffset.get(11).operandos).includes("Field contador:I")) {
            throw new Error("Referências ou desvio do monitor incompatíveis.");
        }

        const referencia = "Object@lock";
        const pilha = [];
        const locais = { contador: 0, monitor: "livre" };
        const historico = [];
        let monitorAdquirido = false;
        let pc = 0;
        let finalizado = false;

        while (!finalizado) {
            const item = porOffset.get(pc);
            if (!item || historico.length > instrucoes.length) {
                throw new Error(`Fluxo de sincronização inválido no offset ${pc}.`);
            }
            let proximo = null;
            let descricao = "";
            switch (pc) {
                case 0:
                    pilha.push(referencia);
                    descricao = "Carrega a referência do objeto lock (monitor compartilhado).";
                    proximo = 3;
                    break;
                case 3:
                    pilha.push(pilha[pilha.length - 1]);
                    descricao = "Duplica a referência lock para preservar uma cópia no slot local.";
                    proximo = 4;
                    break;
                case 4:
                    locais[0] = pilha.pop();
                    descricao = "Armazena a referência lock no slot local 0 para liberá-la depois.";
                    proximo = 5;
                    break;
                case 5:
                    if (pilha.pop() !== referencia || monitorAdquirido) {
                        throw new Error("Estado incompatível ao adquirir monitor.");
                    }
                    monitorAdquirido = true;
                    locais.monitor = "adquirido";
                    descricao = "monitorenter: adquire o monitor do objeto lock; inicia a região crítica.";
                    proximo = 6;
                    break;
                case 6:
                    if (!monitorAdquirido) throw new Error("Contador acessado sem monitor.");
                    pilha.push(locais.contador);
                    descricao = "Lê contador compartilhado (valor didático inicial: 0).";
                    proximo = 9;
                    break;
                case 9:
                    pilha.push(1);
                    descricao = "Empilha a constante inteira 1.";
                    proximo = 10;
                    break;
                case 10: {
                    const b = pilha.pop();
                    const a = pilha.pop();
                    pilha.push((a + b) | 0);
                    descricao = `Soma ${a} + ${b} e empilha o resultado.`;
                    proximo = 11;
                    break;
                }
                case 11:
                    if (!monitorAdquirido) throw new Error("Escrita sem monitor.");
                    locais.contador = pilha.pop();
                    descricao = `Atualiza contador compartilhado para ${locais.contador}.`;
                    proximo = 14;
                    break;
                case 14:
                    pilha.push(locais[0]);
                    descricao = "Recupera a referência lock do slot local 0.";
                    proximo = 15;
                    break;
                case 15:
                    if (!monitorAdquirido || pilha.pop() !== referencia) {
                        throw new Error("Liberação de monitor inválida.");
                    }
                    monitorAdquirido = false;
                    locais.monitor = "livre";
                    descricao = "monitorexit: libera o monitor na execução normal.";
                    proximo = 16;
                    break;
                case 16:
                    descricao = "goto 24: salta o tratamento de exceções (offsets 19–23).";
                    proximo = 24;
                    break;
                case 24:
                    if (monitorAdquirido || pilha.length) {
                        throw new Error("Método finalizado com monitor ou pilha pendente.");
                    }
                    descricao = "return: encerra incrementar(). O contador didático vale 1.";
                    finalizado = true;
                    break;
                default:
                    throw new Error(`Offset não previsto: ${pc}`);
            }
            historico.push({
                offset: item.offset,
                instrucao: item.opcode,
                operandos: item.operandos ?? "",
                hex: item.hexadecimal,
                bin: item.binario,
                pilha: [...pilha],
                locais: { ...locais },
                explicacao: descricao + " Simulação de uma única chamada; não executa threads reais.",
                saida: null
            });
            pc = proximo;
        }
        return historico;
    }



    // ==========================================================
    // EXPERIMENTO 3.17.6 — INTERPRETADOR DIDÁTICO DE DEADLOCK
    // ==========================================================
    // Os offsets, opcodes e bytes são obtidos da API Java.
    // A espera circular é reproduzida didaticamente.
    // Nenhuma thread real é criada no navegador.

    function interpretarDeadlock(instrucoes, thread) {

        const primeiraTrava = thread === "A" ? "LOCK_1" : "LOCK_2";
        const segundaTrava = thread === "A" ? "LOCK_2" : "LOCK_1";

        if (!["A", "B"].includes(thread)) {
            throw new Error("Thread inválida.");
        }

        const porOffset = new Map(
            instrucoes.map(item => [item.offset, item])
        );

        const sequencia = [
            [0, "getstatic"],
            [3, "dup"],
            [4, "astore_0"],
            [5, "monitorenter"],
            [14, "getstatic"],
            [17, "invokevirtual"],
            [20, "invokestatic"],
            [31, "getstatic"],
            [34, "dup"],
            [35, "astore_1"],
            [36, "monitorenter"]
        ];

        for (const [offset, opcode] of sequencia) {
            if (porOffset.get(offset)?.opcode !== opcode) {
                throw new Error(
                    `Bytecode inesperado no offset ${offset}: ${opcode}`
                );
            }
        }

        if (
            !String(porOffset.get(0).operandos).includes(primeiraTrava) ||
            !String(porOffset.get(31).operandos).includes(segundaTrava)
        ) {
            throw new Error("Ordem de aquisição dos monitores incompatível.");
        }

        const pilha = [];

        const locais = {
            LOCK_1: "livre",
            LOCK_2: "livre"
        };


        const historico = [];

        const caminho = [
            0, 3, 4, 5, 6, 9, 11, 14, 17, 20,
            23, 26, 28, 31, 34, 35, 36
        ];

        for (const offset of caminho) {

            const item = porOffset.get(offset);

            if (!item) {
                throw new Error(`Instrução ausente no offset ${offset}.`);
            }

            let descricao = "";

            switch (offset) {
                case 0:
                    pilha.push(primeiraTrava);
                    descricao = `Carrega a referência de ${primeiraTrava}.`;
                    break;

                case 3:
                    pilha.push(pilha[pilha.length - 1]);
                    descricao = "Duplica a referência do primeiro monitor.";
                    break;

                case 4:
                    locais[0] = pilha.pop();
                    descricao = "Armazena a referência no slot local 0.";
                    break;

                case 5:
                    pilha.pop();
                    locais[primeiraTrava] = `adquirido pela Thread ${thread}`;
                    descricao = `monitorenter: Thread ${thread} adquire ${primeiraTrava}.`;
                    break;

                case 6:
                case 23:
                    pilha.push("System.out");
                    descricao = "Carrega a referência System.out.";
                    break;

                case 9:
                    pilha.push(`mensagem Thread ${thread}`);
                    descricao = "Carrega uma mensagem do Constant Pool.";
                    break;

                case 11:
                case 28:
                    pilha.pop();
                    pilha.pop();
                    descricao = "Executa println(String) didaticamente.";
                    break;

                case 14:
                    pilha.push("CountDownLatch");
                    descricao = "Carrega a barreira de sincronização.";
                    break;

                case 17:
                    pilha.pop();
                    descricao = "Executa CountDownLatch.countDown().";
                    break;

                case 20:
                    locais[segundaTrava] =
                        `adquirido pela Thread ${thread === "A" ? "B" : "A"}`;

                    descricao =
                        "As duas threads alcançaram a barreira. " +
                        `A outra thread já mantém ${segundaTrava}.`;
                    break;

                case 26:
                    pilha.push(`aguardando ${segundaTrava}`);
                    descricao = "Carrega a mensagem de espera.";
                    break;

                case 31:
                    pilha.push(segundaTrava);
                    descricao = `Carrega a referência de ${segundaTrava}.`;
                    break;

                case 34:
                    pilha.push(pilha[pilha.length - 1]);
                    descricao = "Duplica a referência do segundo monitor.";
                    break;

                case 35:
                    locais[1] = pilha.pop();
                    descricao = "Armazena a referência no slot local 1.";
                    break;

                case 36:
                    // monitorenter bloqueado: a referência permanece na pilha
                    // e a outra thread continua proprietária do monitor.
                    locais[segundaTrava] =
                        `adquirido pela Thread ${thread === "A" ? "B" : "A"} (aguardado)`;
                    descricao =
                        `DEADLOCK: Thread ${thread} aguarda ${segundaTrava}, ` +
                        `enquanto mantém ${primeiraTrava}. ` +
                        "A outra thread apresenta a dependência inversa.";
                    break;
            }

            historico.push({
                offset: item.offset,
                instrucao: item.opcode,
                operandos: item.operandos ?? "",
                hex: item.hexadecimal,
                bin: item.binario,
                pilha: [...pilha],
                locais: { ...locais },
                explicacao: descricao,
                saida: null
            });
        }

        return historico;
    }



    // ==========================================================
    // RENDERIZAÇÃO DO EXPERIMENTO
    // ==========================================================

    function renderizar() {

        if (!passos.length) {
            return;
        }

        const passo = passos[indiceAtual];
        const config = experimentos[experimentoAtual];

        instrucaoAtual.textContent =
            nomeInstrucao(passo);

        hexAtual.textContent =
            passo.hex || "—";

        binAtual.textContent =
            passo.bin || "—";

        explicacao.textContent =
            passo.explicacao;

        contador.textContent =
            `Passo ${indiceAtual + 1} de ${passos.length}`;

        // Destaca a instrução executada.
        [...lista.children].forEach((elemento, indice) => {

            elemento.classList.toggle(
                "active",
                indice === indiceAtual
            );
        });

        // Representação da pilha de operandos.
        pilhaOperandos.replaceChildren();

        passo.pilha.forEach(valor => {

            const elemento =
                document.createElement("div");

            elemento.className = "stack-value";

            elemento.textContent =
                typeof valor === "object"
                    ? valor.referencia
                    : String(valor);

            pilhaOperandos.prepend(elemento);
        });

        // Representação das variáveis locais.
        variaveisLocais.replaceChildren();

        Object.entries(config.variaveis)
            .forEach(([indice, nome]) => {

                const elemento =
                    document.createElement("p");

                const valor =
                    Object.prototype.hasOwnProperty.call(
                        passo.locais,
                        indice
                    )
                        ? passo.locais[indice]
                        : "—";

                elemento.textContent =
                    `${nome}: ${valor}`;

                variaveisLocais.appendChild(elemento);
            });

        btnAnterior.disabled =
            indiceAtual === 0;

        btnProximo.disabled =
            indiceAtual === passos.length - 1;

        btnReiniciar.disabled = false;
    }

    // ==========================================================
    // LIMPEZA DA INTERFACE
    // ==========================================================

    function limparVisualizacao() {

        passos = [];
        indiceAtual = 0;

        lista.replaceChildren();
        pilhaOperandos.replaceChildren();
        variaveisLocais.replaceChildren();

        instrucaoAtual.textContent = "—";
        hexAtual.textContent = "—";
        binAtual.textContent = "—";

        contador.textContent =
            "Aguardando carregamento";

        btnAnterior.disabled = true;
        btnProximo.disabled = true;
        btnReiniciar.disabled = true;

        sequenciaHexadecimal.textContent =
            "Aguardando carregamento...";
    }

    // ==========================================================
    // SELEÇÃO DOS EXPERIMENTOS
    // ==========================================================

    function atualizarSelecao() {

        [
            [btnSoma, "soma"],
            [btnMatematica, "matematica"],
            [btnDecisao, "decisao"],
            [btnRace, "race"],
            [btnRaceSincronizada, "raceSincronizada"],
            [btnObjetos, "objetos"],
            [btnSincronizacao, "sincronizacao"],
            [btnDeadlock, "deadlock"]
        ].forEach(([botao, chave]) => {

            if (botao) {

                botao.setAttribute(
                    "aria-pressed",
                    String(experimentoAtual === chave)
                );
            }
        });
    }



    function atualizarNavegacaoVisual(tipoExperimento) {

        // O laboratório de Aritmética possui dois subexperimentos.
        const aritmeticaAtiva = ["soma", "matematica"].includes(tipoExperimento);

        // Somente o botão correspondente permanece destacado.
        const navegacao = [
            [btnNavegacaoAritmetica, aritmeticaAtiva],
            [btnDecisao, tipoExperimento === "decisao"],
            [btnRace, tipoExperimento === "race"],
            [btnRaceSincronizada, tipoExperimento === "raceSincronizada"],
            [btnObjetos, tipoExperimento === "objetos"],
            [btnSincronizacao, tipoExperimento === "sincronizacao"],
            [btnDeadlock, tipoExperimento === "deadlock"]
        ];

        navegacao.forEach(([botao, ativo]) => {
            if (!botao) return;
            botao.classList.toggle("active", ativo);
            botao.setAttribute("aria-pressed", String(ativo));
        });

        // Os botões Soma e Multiplicação são exclusivos de Aritmética.
        // Localiza o painel existente sem exigir alteração do HTML.
        const painelAritmetico = btnSoma?.closest(".execution-panel");
        if (painelAritmetico) {
            painelAritmetico.hidden = !aritmeticaAtiva;
            // A propriedade display garante a ocultação mesmo se
            // alguma regra CSS sobrescrever o atributo hidden.
            painelAritmetico.style.display = aritmeticaAtiva ? "" : "none";
        }

        if (painelDeadlock) {
            const ativo = tipoExperimento === "deadlock";
            painelDeadlock.hidden = !ativo;
            painelDeadlock.style.display = ativo ? "" : "none";
        }
    }


    // ==========================================================
    // DEADLOCK — MÉTODOS, SELEÇÃO E RENDERIZAÇÃO
    // ==========================================================

    function obterMetodoDeadlock(dados, thread) {
        if (!Array.isArray(dados?.metodos)) {
            throw new Error("A API não retornou a coleção de métodos do Deadlock.");
        }
        const nome = thread === "A" ? "executarThreadA" : "executarThreadB";
        const metodo = dados.metodos.find(item =>
            item.nome === nome || item.nomeMetodo === nome || item.metodo === nome
        );
        if (!metodo || !Array.isArray(metodo.instrucoesEstruturadas) ||
            !metodo.instrucoesEstruturadas.length) {
            throw new Error(`Instruções de ${nome} não encontradas na API.`);
        }
        return metodo;
    }

    function apresentarPassos(novosPassos, instrucoes) {
        passos = novosPassos;
        indiceAtual = 0;
        lista.replaceChildren();
        passos.forEach(passo => {
            const item = document.createElement("li");
            item.textContent = nomeInstrucao(passo);
            lista.appendChild(item);
        });
        sequenciaHexadecimal.textContent = instrucoes
            .map(item => item.hexadecimal)
            .filter(valor => typeof valor === "string" && valor.trim())
            .join(" ") || "Não disponível";
        renderizar();
    }

    function atualizarControlesDeadlock() {
        const ativo = experimentoAtual === "deadlock";
        for (const [botao, thread] of [
            [btnDeadlockThreadA, "A"], [btnDeadlockThreadB, "B"]
        ]) {
            if (!botao) continue;
            botao.disabled = !ativo || !dadosDeadlock;
            botao.setAttribute("aria-pressed", String(ativo && threadDeadlockAtual === thread));
            botao.classList.toggle("active", ativo && threadDeadlockAtual === thread);
        }
        if (estadoDeadlock && ativo) {
            estadoDeadlock.textContent =
                `Thread ${threadDeadlockAtual}: demonstração didática da espera circular; ` +
                "os monitores e o bloqueio foram diagnosticados na JVM com jcmd e jstack.";
        }
    }

    function selecionarThreadDeadlock(thread) {
        if (experimentoAtual !== "deadlock" || !dadosDeadlock) return;
        try {
            const metodo = obterMetodoDeadlock(dadosDeadlock, thread);
            const novosPassos = interpretarDeadlock(metodo.instrucoesEstruturadas, thread);
            threadDeadlockAtual = thread;
            apresentarPassos(novosPassos, metodo.instrucoesEstruturadas);
            atualizarControlesDeadlock();
        } catch (erro) {
            console.error("Erro ao selecionar thread do Deadlock:", erro);
            explicacao.textContent = `Falha ao selecionar Thread ${thread}: ${erro.message}`;
        }
    }

    // ==========================================================
    // CONSULTA À API SPRING BOOT
    // ==========================================================

    async function carregarExperimento(chave) {

        if (
            !Object.prototype.hasOwnProperty.call(
                experimentos,
                chave
            )
        ) {
            return;
        }

        const identificador = ++requisicaoAtual;

        experimentoAtual = chave;

        const config = experimentos[chave];

        limparVisualizacao();
        atualizarSelecao();
        atualizarNavegacaoVisual(chave);
        if (chave !== "deadlock") dadosDeadlock = null;
        atualizarControlesDeadlock();

        identificacao.textContent =
            config.identificacao;

        titulo.textContent =
            config.titulo;

        codigoFonte.textContent =
            config.codigo;

        explicacao.textContent =
            "Carregando instruções reais da API Java...";

        try {

            const resposta = await fetch(
                config.endpoint,
                { cache: "no-store" }
            );

            if (!resposta.ok) {

                throw new Error(
                    `HTTP ${resposta.status}`
                );
            }

            const dados = await resposta.json();

            // Evita que uma requisição antiga
            // substitua o experimento selecionado.
            if (identificador !== requisicaoAtual) {
                return;
            }

            let instrucoes;
            let novosPassos;

            if (config.modoDeadlock) {
                dadosDeadlock = dados;
                threadDeadlockAtual = "A";
                const metodo = obterMetodoDeadlock(dados, "A");
                instrucoes = metodo.instrucoesEstruturadas;
                novosPassos = interpretarDeadlock(instrucoes, "A");
            } else {
                dadosDeadlock = null;
                if (!Array.isArray(dados.instrucoesEstruturadas) ||
                    !dados.instrucoesEstruturadas.length) {
                    throw new Error("A API não retornou instruções estruturadas.");
                }
                instrucoes = dados.instrucoesEstruturadas;

                if (config.modoRace &&
                    dados.sincronizado !== config.sincronizacaoEsperada) {
                    throw new Error("Flag de sincronização incompatível com o experimento.");
                }
                if (config.modoMonitor &&
                    (dados.sincronizado !== false ||
                        dados.tipoSincronizacao !== "BLOCO_SYNCHRONIZED")) {
                    throw new Error("Metadados de sincronização incompatíveis.");
                }
                novosPassos = config.modoRace
                    ? interpretarIncrementoDidatico(instrucoes, dados.sincronizado)
                    : config.modoObjetos
                        ? interpretarObjetos(instrucoes)
                        : config.modoMonitor
                            ? interpretarMonitor(instrucoes)
                            : interpretar(instrucoes);
            }

            apresentarPassos(novosPassos, instrucoes);
            atualizarControlesDeadlock();

        } catch (erro) {

            if (identificador !== requisicaoAtual) {
                return;
            }

            console.error(
                "Erro no Bytecode Explorer:",
                erro
            );

            limparVisualizacao();

            explicacao.textContent =
                "Não foi possível carregar ou interpretar " +
                "o Bytecode: " + erro.message;

            contador.textContent =
                "Execução indisponível";
            if (config.modoDeadlock) dadosDeadlock = null;
            atualizarControlesDeadlock();
        }
    }

    // ==========================================================
    // BOTÕES DE NAVEGAÇÃO
    // ==========================================================

    btnProximo.addEventListener("click", () => {

        if (indiceAtual < passos.length - 1) {

            indiceAtual++;
            renderizar();
        }
    });

    btnAnterior.addEventListener("click", () => {

        if (indiceAtual > 0) {

            indiceAtual--;
            renderizar();
        }
    });

    btnReiniciar.addEventListener("click", () => {

        indiceAtual = 0;
        renderizar();
    });

    // ==========================================================
    // BOTÕES DOS EXPERIMENTOS
    // ==========================================================

    btnSoma.addEventListener("click", () => {
        carregarExperimento("soma");
    });

    btnMatematica.addEventListener("click", () => {
        carregarExperimento("matematica");
    });

    if (btnDecisao) {

        btnDecisao.addEventListener("click", () => {
            carregarExperimento("decisao");
        });
    }

    if (btnRace) {
        btnRace.addEventListener("click", () => carregarExperimento("race"));
    }

    if (btnRaceSincronizada) {
        btnRaceSincronizada.addEventListener("click", () =>
            carregarExperimento("raceSincronizada")
        );
    }

    if (btnObjetos) {
        btnObjetos.addEventListener("click", () =>
            carregarExperimento("objetos")
        );
    }

    if (btnSincronizacao) {
        btnSincronizacao.addEventListener("click", () =>
            carregarExperimento("sincronizacao")
        );
    }

    if (btnDeadlock) {
        btnDeadlock.addEventListener("click", () => carregarExperimento("deadlock"));
    }
    if (btnDeadlockThreadA) {
        btnDeadlockThreadA.addEventListener("click", () => selecionarThreadDeadlock("A"));
    }
    if (btnDeadlockThreadB) {
        btnDeadlockThreadB.addEventListener("click", () => selecionarThreadDeadlock("B"));
    }

    // Carregamento inicial.
    carregarExperimento("soma");
});