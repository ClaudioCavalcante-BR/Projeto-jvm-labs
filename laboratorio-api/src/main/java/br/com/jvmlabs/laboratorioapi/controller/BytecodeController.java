
package br.com.jvmlabs.laboratorioapi.controller;

import br.com.jvmlabs.laboratorioapi.service.BytecodeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/bytecode")
public class BytecodeController {

    private final BytecodeService bytecodeService;

    public BytecodeController(BytecodeService bytecodeService) {
        this.bytecodeService = bytecodeService;
    }

    // ==========================================================
    // EXPERIMENTO 3.17.1-A — ADIÇÃO
    // Classe: OperacaoAritmetica.java
    // Resultado esperado: 3
    // ==========================================================

    @GetMapping("/aritmetica")
    public ResponseEntity<?> consultarAritmetica()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.1",
                "OperacaoAritmetica.class"
        );

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(arquivo);

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.1-A",
                        "classe", "OperacaoAritmetica",
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas
                )
        );
    }

    // ==========================================================
    // EXPERIMENTO 3.17.1-B — MULTIPLICAÇÃO E SUBTRAÇÃO
    // Classe: OperacaoMatematica.java
    // Resultado esperado: 10
    // ==========================================================

    @GetMapping("/matematica")
    public ResponseEntity<?> consultarMatematica()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.1",
                "OperacaoMatematica.class"
        );

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(arquivo);

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.1-B",
                        "classe", "OperacaoMatematica",
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas
                )
        );
    }

    // ==========================================================
    // EXPERIMENTO 3.17.2 — ESTRUTURAS DE DECISÃO
    // Classe: EstruturaDecisao.java
    //
    // Código Java:
    // int a = 3;
    // int b = 5;
    //
    // if (a > b) {
    //     resultado = a;
    // } else {
    //     resultado = b;
    // }
    //
    // Resultado esperado: 5
    //
    // Instruções importantes:
    // if_icmple — desvio condicional
    // goto      — desvio incondicional
    // ==========================================================

    @GetMapping("/decisao")
    public ResponseEntity<?> consultarDecisao()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.2",
                "EstruturaDecisao.class"
        );

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(arquivo);

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.2",
                        "classe", "EstruturaDecisao",
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas
                )
        );
    }

    // ==========================================================
    // EXPERIMENTO 3.17.3-A — RACE CONDITION
    //
    // Classe: RaceCondition.java
    // Método analisado: lambda$main$0
    //
    // Quatro threads incrementam um contador compartilhado.
    // O incremento não possui sincronização.
    //
    // Instruções importantes:
    // getstatic — leitura do contador
    // iconst_1 — constante 1
    // iadd     — soma
    // putstatic — gravação do contador
    //
    // Resultado: sujeito a atualizações perdidas.
    // ==========================================================

    @GetMapping("/race-condition")
    public ResponseEntity<?> consultarRaceCondition()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.3",
                "RaceCondition.class"
        );

        String metodo = "lambda$main$0";

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        metodo
                );

        boolean sincronizado =
                bytecodeService.metodoSincronizado(
                        arquivo,
                        metodo
                );

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.3-A",
                        "classe", "RaceCondition",
                        "metodo", metodo,
                        "sincronizado", sincronizado,
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas,
                        "descricao", "Incremento de contador compartilhado sem sincronizacao"
                )
        );
    }

    // ==========================================================
    // EXPERIMENTO 3.17.3-B — RACE CONDITION CORRIGIDA
    //
    // Classe: RaceConditionSincronizada.java
    // Método analisado: incrementar
    //
    // A declaração synchronized garante exclusão mútua
    // durante a execução do método.
    //
    // Por ser um método static synchronized, o monitor
    // utilizado é o objeto Class correspondente à classe.
    //
    // O javap -v identifica a flag ACC_SYNCHRONIZED.
    // ==========================================================

    @GetMapping("/race-condition-sincronizada")
    public ResponseEntity<?> consultarRaceConditionSincronizada()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.3",
                "RaceConditionSincronizada.class"
        );

        String metodo = "incrementar";

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        metodo
                );

        boolean sincronizado =
                bytecodeService.metodoSincronizado(
                        arquivo,
                        metodo
                );

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.3-B",
                        "classe", "RaceConditionSincronizada",
                        "metodo", metodo,
                        "sincronizado", sincronizado,
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas,
                        "descricao", "Incremento de contador compartilhado com synchronized"
                )
        );
    }


    // ==========================================================
    // EXPERIMENTO 3.17.4 — BYTECODE DE OBJETOS
    //
    // Classe: Pessoa.java
    // Método analisado: main
    //
    // Objetivo:
    // Demonstrar a criação de objetos, a chamada do
    // construtor, o armazenamento de referências e
    // a execução de métodos de instância.
    //
    // Instruções importantes:
    // new           — criação do objeto
    // dup           — duplicação da referência
    // ldc           — carregamento da String
    // bipush        — carregamento do valor inteiro
    // invokespecial — chamada do construtor
    // astore_1      — armazenamento da referência
    // aload_1       — carregamento da referência
    // invokevirtual — chamada do método apresentar()
    // ==========================================================

    @GetMapping("/objetos")
    public ResponseEntity<?> consultarObjetos()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.4",
                "Pessoa.class"
        );

        String metodo = "main";

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        metodo
                );

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.4",
                        "classe", "Pessoa",
                        "metodo", metodo,
                        "descricao", "Criacao de objetos e referencias na JVM",
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas
                )
        );

    }

    // ==========================================================
    // EXPERIMENTO 3.17.5 — SINCRONIZAÇÃO COM MONITORES
    //
    // Classe: SincronizacaoMonitor.java
    // Método analisado: incrementar
    //
    // Objetivo:
    // Demonstrar a implementação de um bloco synchronized
    // por meio das instruções monitorenter e monitorexit.
    //
    // Instruções importantes:
    // monitorenter — aquisição do monitor
    // monitorexit  — liberação do monitor
    // athrow       — propagação de exceção
    //
    // O método utiliza um bloco synchronized(lock),
    // e não a declaração synchronized no método.
    // ==========================================================

    @GetMapping("/sincronizacao")
    public ResponseEntity<?> consultarSincronizacao()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.5",
                "SincronizacaoMonitor.class"
        );

        String metodo = "incrementar";

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesEstruturadas =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        metodo
                );

        boolean sincronizado =
                bytecodeService.metodoSincronizado(
                        arquivo,
                        metodo
                );

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.5",
                        "classe", "SincronizacaoMonitor",
                        "metodo", metodo,
                        "sincronizado", sincronizado,
                        "tipoSincronizacao", "BLOCO_SYNCHRONIZED",
                        "descricao",
                        "Sincronizacao por monitorenter e monitorexit",
                        "instrucoes", instrucoes,
                        "instrucoesEstruturadas", instrucoesEstruturadas
                )
        );
    }

    // ==========================================================
    // EXPERIMENTO 3.17.6 — DIAGNÓSTICO DE DEADLOCK
    //
    // Classe: DeadlockMonitor.java
    //
    // Métodos analisados:
    // executarThreadA() e executarThreadB()
    //
    // Objetivo:
    // Comparar a ordem de aquisição dos monitores
    // LOCK_1 e LOCK_2 no Bytecode real da JVM.
    //
    // Thread A: LOCK_1 -> LOCK_2
    // Thread B: LOCK_2 -> LOCK_1
    //
    // O deadlock foi diagnosticado separadamente
    // por jcmd e jstack.
    //
    // Este endpoint apenas consulta o arquivo .class.
    // Não inicia threads nem provoca deadlock.
    // ==========================================================

    @GetMapping("/deadlock")
    public ResponseEntity<?> consultarDeadlock()
            throws IOException, InterruptedException {

        Path arquivo = Path.of(
                "modulo-03-bytecode",
                "etapa-3.17.6",
                "DeadlockMonitor.class"
        );

        String instrucoes =
                bytecodeService.consultarInstrucoes(arquivo);

        var instrucoesThreadA =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        "executarThreadA"
                );

        var instrucoesThreadB =
                bytecodeService.extrairInstrucoes(
                        arquivo,
                        "executarThreadB"
                );

        return ResponseEntity.ok(
                Map.of(
                        "experimento", "3.17.6",
                        "classe", "DeadlockMonitor",
                        "descricao",
                        "Analise de deadlock por inversao da ordem de aquisicao dos monitores",
                        "instrucoes", instrucoes,
                        "metodos", java.util.List.of(
                                Map.of(
                                        "nome", "executarThreadA",
                                        "primeiroMonitor", "LOCK_1",
                                        "segundoMonitor", "LOCK_2",
                                        "instrucoesEstruturadas", instrucoesThreadA
                                ),
                                Map.of(
                                        "nome", "executarThreadB",
                                        "primeiroMonitor", "LOCK_2",
                                        "segundoMonitor", "LOCK_1",
                                        "instrucoesEstruturadas", instrucoesThreadB
                                )
                        )
                )
        );
    }


}
