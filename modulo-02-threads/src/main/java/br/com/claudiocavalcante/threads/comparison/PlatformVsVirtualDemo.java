package br.com.claudiocavalcante.threads.comparison;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PlatformVsVirtualDemo {

    private static final int NUMERO_TAREFAS = 1_000;

    private static final int TEMPO_ESPERA_MS = 200;

    private static final int PLATFORM_POOL_SIZE = 50;

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "MÓDULO 2 - PLATFORM vs VIRTUAL THREADS"
        );

        System.out.println(
                "COMPARAÇÃO DE CONCORRÊNCIA"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "\nTarefas: "
                        + NUMERO_TAREFAS
        );

        System.out.println(
                "Espera simulada por tarefa: "
                        + TEMPO_ESPERA_MS
                        + " ms"
        );

        System.out.println(
                "Platform Threads no pool: "
                        + PLATFORM_POOL_SIZE
        );

        /*
         * ========================================
         * TESTE 1 - PLATFORM THREADS
         * ========================================
         */

        ExecutorService platformExecutor =
                Executors.newFixedThreadPool(
                        PLATFORM_POOL_SIZE
                );

        long tempoPlatform =
                executarTeste(
                        "PLATFORM THREADS",
                        platformExecutor
                );

        /*
         * Pequena pausa apenas para separar
         * visualmente os dois testes.
         */
        Thread.sleep(1000);

        /*
         * ========================================
         * TESTE 2 - VIRTUAL THREADS
         * ========================================
         */

        ExecutorService virtualExecutor =
                Executors.newVirtualThreadPerTaskExecutor();

        long tempoVirtual =
                executarTeste(
                        "VIRTUAL THREADS",
                        virtualExecutor
                );

        /*
         * ========================================
         * COMPARAÇÃO FINAL
         * ========================================
         */

        double segundosPlatform =
                tempoPlatform / 1_000_000_000.0;

        double segundosVirtual =
                tempoVirtual / 1_000_000_000.0;

        double razao =
                segundosPlatform
                        / segundosVirtual;

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "COMPARAÇÃO FINAL"
        );

        System.out.println(
                "=========================================="
        );

        System.out.printf(
                "Platform Threads: %.3f s%n",
                segundosPlatform
        );

        System.out.printf(
                "Virtual Threads : %.3f s%n",
                segundosVirtual
        );

        System.out.printf(
                "Razão de tempo observada: %.2fx%n",
                razao
        );

        System.out.println(
                "\nIMPORTANTE:"
        );

        System.out.println(
                "A razão acima NÃO significa que uma"
        );

        System.out.println(
                "Virtual Thread individual seja mais rápida."
        );

        System.out.println(
                "O ganho vem da maior concorrência durante"
        );

        System.out.println(
                "as esperas simuladas de I/O."
        );

        System.out.println(
                "\nAplicação finalizada."
        );
    }

    private static long executarTeste(
            String titulo,
            ExecutorService executor
    ) throws InterruptedException {

        CountDownLatch tarefasFinalizadas =
                new CountDownLatch(
                        NUMERO_TAREFAS
                );

        /*
         * Registra quantas threads distintas
         * efetivamente executaram as tarefas.
         */
        Set<Long> threadsUtilizadas =
                ConcurrentHashMap.newKeySet();

        /*
         * Conta quantas tarefas executaram
         * dentro de Virtual Threads.
         */
        AtomicInteger tarefasVirtuais =
                new AtomicInteger();

        System.out.println(
                "\n=== "
                        + titulo
                        + " ==="
        );

        long inicio =
                System.nanoTime();

        for (int tarefa = 1;
             tarefa <= NUMERO_TAREFAS;
             tarefa++) {

            executor.submit(() -> {

                Thread atual =
                        Thread.currentThread();

                threadsUtilizadas.add(
                        atual.threadId()
                );

                if (atual.isVirtual()) {

                    tarefasVirtuais.incrementAndGet();
                }

                try {

                    /*
                     * Simula uma operação I/O-bound.
                     *
                     * Não existe trabalho pesado de CPU.
                     */
                    Thread.sleep(
                            TEMPO_ESPERA_MS
                    );

                } catch (InterruptedException e) {

                    Thread.currentThread()
                            .interrupt();

                } finally {

                    tarefasFinalizadas.countDown();
                }
            });
        }

        tarefasFinalizadas.await();

        long fim =
                System.nanoTime();

        executor.shutdown();

        if (!executor.awaitTermination(
                10,
                TimeUnit.SECONDS
        )) {

            executor.shutdownNow();
        }

        long duracao =
                fim - inicio;

        double segundos =
                duracao
                        / 1_000_000_000.0;

        double throughput =
                NUMERO_TAREFAS
                        / segundos;

        System.out.println(
                "Tarefas concluídas: "
                        + NUMERO_TAREFAS
        );

        System.out.println(
                "Threads distintas utilizadas: "
                        + threadsUtilizadas.size()
        );

        System.out.println(
                "Tarefas executadas em Virtual Threads: "
                        + tarefasVirtuais.get()
        );

        System.out.printf(
                "Tempo total: %.3f s%n",
                segundos
        );

        System.out.printf(
                "Throughput aproximado: %.2f tarefas/s%n",
                throughput
        );

        return duracao;
    }
}