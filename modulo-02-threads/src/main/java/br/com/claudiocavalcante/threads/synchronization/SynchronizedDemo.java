package br.com.claudiocavalcante.threads.synchronization;

public class SynchronizedDemo {

    private static int contador = 0;

    private static final int NUMERO_THREADS = 12;
    private static final int INCREMENTOS_POR_THREAD = 1_000_000;
    private static final int RODADAS = 5;

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - SYNCHRONIZED"
        );
        System.out.println(
                "ESTADO COMPARTILHADO COM SINCRONIZAÇÃO"
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
     * Região crítica protegida por synchronized.
     *
     * Como o método é static synchronized,
     * o monitor utilizado pertence à classe:
     *
     * SynchronizedDemo.class
     */
    private static synchronized void incrementar() {

        contador++;
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
                            "worker-sync-"
                                    + numeroWorker
                    )
                    .unstarted(() -> {

                        for (int incremento = 0;
                             incremento
                                     < INCREMENTOS_POR_THREAD;
                             incremento++) {

                            /*
                             * Agora o incremento
                             * está protegido.
                             */
                            incrementar();

                            /*
                             * Mantemos o yield para
                             * aumentar a alternância
                             * entre as threads.
                             */
                            if ((incremento & 1023) == 0) {
                                Thread.yield();
                            }
                        }
                    });
        }

        /*
         * Inicia todas as workers.
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
