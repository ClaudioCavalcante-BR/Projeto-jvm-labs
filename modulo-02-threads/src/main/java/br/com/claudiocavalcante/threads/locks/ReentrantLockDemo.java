package br.com.claudiocavalcante.threads.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private static int contador = 0;

    private static final int NUMERO_THREADS = 12;
    private static final int INCREMENTOS_POR_THREAD = 1_000_000;
    private static final int RODADAS = 5;

    /*
     * Lock compartilhado entre todas as threads.
     */
    private static final Lock lock =
            new ReentrantLock();

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - REENTRANTLOCK"
        );
        System.out.println(
                "ESTADO COMPARTILHADO COM LOCK EXPLÍCITO"
        );
        System.out.println(
                "========================================"
        );

        int esperado =
                NUMERO_THREADS
                        * INCREMENTOS_POR_THREAD;

        System.out.println(
                "\nThreads: "
                        + NUMERO_THREADS
        );

        System.out.println(
                "Incrementos por thread: "
                        + INCREMENTOS_POR_THREAD
        );

        System.out.println(
                "Resultado esperado por rodada: "
                        + esperado
        );

        for (int rodada = 1;
             rodada <= RODADAS;
             rodada++) {

            executarRodada(
                    rodada,
                    esperado
            );
        }

        System.out.println(
                "\nAplicação finalizada."
        );
    }

    /*
     * Região crítica protegida
     * explicitamente pelo ReentrantLock.
     */
    private static void incrementar() {

        lock.lock();

        try {

            contador++;

        } finally {

            /*
             * O unlock deve ficar no finally
             * para garantir a liberação do lock.
             */
            lock.unlock();
        }
    }

    private static void executarRodada(
            int rodada,
            int esperado
    ) throws InterruptedException {

        contador = 0;

        Thread[] workers =
                new Thread[NUMERO_THREADS];

        for (int i = 0;
             i < NUMERO_THREADS;
             i++) {

            int numeroWorker = i + 1;

            workers[i] = Thread.ofPlatform()
                    .name(
                            "worker-lock-"
                                    + numeroWorker
                    )
                    .unstarted(() -> {

                        for (int incremento = 0;
                             incremento
                                     < INCREMENTOS_POR_THREAD;
                             incremento++) {

                            incrementar();

                            if ((incremento & 1023) == 0) {

                                Thread.yield();
                            }
                        }
                    });
        }

        /*
         * Inicia todas as threads.
         */
        for (Thread worker : workers) {

            worker.start();
        }

        /*
         * Aguarda todas terminarem.
         */
        for (Thread worker : workers) {

            worker.join();
        }

        int perdido =
                esperado - contador;

        System.out.println(
                "\n=== RODADA "
                        + rodada
                        + " ==="
        );

        System.out.println(
                "Esperado : "
                        + esperado
        );

        System.out.println(
                "Obtido   : "
                        + contador
        );

        System.out.println(
                "Perdidos : "
                        + perdido
        );

        System.out.println(
                "Resultado correto? "
                        + (contador == esperado)
        );
    }

}
