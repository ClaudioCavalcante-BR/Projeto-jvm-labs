package br.com.claudiocavalcante.threads.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorThreadPoolDemo {

    private static final int NUMERO_THREADS_POOL = 3;
    private static final int NUMERO_TAREFAS = 9;

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - EXECUTORS"
        );
        System.out.println(
                "FIXED THREAD POOL"
        );
        System.out.println(
                "========================================"
        );

        System.out.println(
                "\nThread principal: "
                        + Thread.currentThread().getName()
        );

        System.out.println(
                "Threads no pool: "
                        + NUMERO_THREADS_POOL
        );

        System.out.println(
                "Tarefas enviadas: "
                        + NUMERO_TAREFAS
        );

        /*
         * Cria um pool fixo com três Platform Threads.
         */
        ExecutorService executor =
                Executors.newFixedThreadPool(
                        NUMERO_THREADS_POOL
                );

        System.out.println(
                "\n=== ENVIANDO TAREFAS ==="
        );

        for (int tarefa = 1;
             tarefa <= NUMERO_TAREFAS;
             tarefa++) {

            int numeroTarefa = tarefa;

            executor.submit(() -> {

                String thread =
                        Thread.currentThread().getName();

                System.out.println(
                        "["
                                + thread
                                + "] iniciou tarefa "
                                + numeroTarefa
                );

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    Thread.currentThread()
                            .interrupt();

                    System.out.println(
                            "["
                                    + thread
                                    + "] tarefa "
                                    + numeroTarefa
                                    + " interrompida."
                    );

                    return;
                }

                System.out.println(
                        "["
                                + thread
                                + "] terminou tarefa "
                                + numeroTarefa
                );
            });
        }

        /*
         * Não aceita novas tarefas.
         *
         * As tarefas já submetidas continuam.
         */
        executor.shutdown();

        System.out.println(
                "\n[main] shutdown() solicitado."
        );

        System.out.println(
                "[main] aguardando conclusão das tarefas..."
        );

        boolean terminou =
                executor.awaitTermination(
                        30,
                        TimeUnit.SECONDS
                );

        System.out.println(
                "\n=== RESULTADO FINAL ==="
        );

        System.out.println(
                "Todas as tarefas terminaram? "
                        + terminou
        );

        System.out.println(
                "Executor encerrado? "
                        + executor.isTerminated()
        );

        System.out.println(
                "\nAplicação finalizada."
        );
    }
}