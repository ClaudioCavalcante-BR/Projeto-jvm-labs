package br.com.claudiocavalcante.threads.communication;

public class WaitNotifyDemo {

    /*
     * Objeto utilizado como monitor.
     */
    private static final Object MONITOR = new Object();

    /*
     * Quantidade de workers autorizadas
     * a continuar a execução.
     */
    private static int permissoes = 0;

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - WAIT, NOTIFY E NOTIFYALL"
        );
        System.out.println(
                "COMUNICAÇÃO ENTRE THREADS"
        );
        System.out.println(
                "========================================"
        );

        Thread worker1 = criarWorker("worker-wait-01");
        Thread worker2 = criarWorker("worker-wait-02");
        Thread worker3 = criarWorker("worker-wait-03");

        Thread[] workers = {
                worker1,
                worker2,
                worker3
        };

        System.out.println(
                "\n=== INICIANDO WORKERS ==="
        );

        for (Thread worker : workers) {
            worker.start();
        }

        /*
         * Aguarda as três entrarem efetivamente
         * no estado WAITING.
         */
        aguardarQuantidadeEmEstado(
                workers,
                Thread.State.WAITING,
                3
        );

        System.out.println(
                "\n=== TODAS EM WAITING ==="
        );

        imprimirEstados(workers);

        /*
         * ========================================
         * TESTE notify()
         * ========================================
         */

        synchronized (MONITOR) {

            System.out.println(
                    "\n[main] concedendo 1 permissão."
            );

            permissoes = 1;

            System.out.println(
                    "[main] executando notify()."
            );

            MONITOR.notify();
        }

        /*
         * Esperamos uma worker terminar.
         */
        aguardarQuantidadeEmEstado(
                workers,
                Thread.State.TERMINATED,
                1
        );

        System.out.println(
                "\n=== APÓS notify() ==="
        );

        imprimirEstados(workers);

        /*
         * ========================================
         * TESTE notifyAll()
         * ========================================
         */

        synchronized (MONITOR) {

            System.out.println(
                    "\n[main] concedendo 2 permissões."
            );

            permissoes = 2;

            System.out.println(
                    "[main] executando notifyAll()."
            );

            MONITOR.notifyAll();
        }

        /*
         * Aguarda todas terminarem.
         */
        for (Thread worker : workers) {
            worker.join();
        }

        System.out.println(
                "\n=== ESTADO FINAL ==="
        );

        imprimirEstados(workers);

        System.out.println(
                "\nAplicação finalizada."
        );
    }

    private static Thread criarWorker(
            String nome
    ) {

        return Thread.ofPlatform()
                .name(nome)
                .unstarted(() -> {

                    synchronized (MONITOR) {

                        System.out.println(
                                "["
                                        + Thread.currentThread().getName()
                                        + "] entrou no monitor."
                        );

                        /*
                         * Sempre verificamos a condição
                         * utilizando while.
                         */
                        while (permissoes == 0) {

                            try {

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] sem permissão. Executando wait()..."
                                );

                                MONITOR.wait();

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] acordou e recuperou o monitor."
                                );

                            } catch (InterruptedException e) {

                                Thread.currentThread()
                                        .interrupt();

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] interrompida."
                                );

                                return;
                            }
                        }

                        /*
                         * Consome uma autorização.
                         */
                        permissoes--;

                        System.out.println(
                                "["
                                        + Thread.currentThread().getName()
                                        + "] recebeu permissão."
                        );
                    }

                    System.out.println(
                            "["
                                    + Thread.currentThread().getName()
                                    + "] terminou."
                    );
                });
    }

    private static void imprimirEstados(
            Thread[] workers
    ) {

        for (Thread worker : workers) {

            System.out.println(
                    worker.getName()
                            + ": "
                            + worker.getState()
            );
        }
    }

    private static void aguardarQuantidadeEmEstado(
            Thread[] workers,
            Thread.State estado,
            int quantidade
    ) throws InterruptedException {

        long limite =
                System.currentTimeMillis()
                        + 5000;

        while (true) {

            int encontrados = 0;

            for (Thread worker : workers) {

                if (worker.getState() == estado) {

                    encontrados++;
                }
            }

            if (encontrados >= quantidade) {

                return;
            }

            if (System.currentTimeMillis()
                    >= limite) {

                throw new IllegalStateException(
                        "Timeout aguardando "
                                + quantidade
                                + " thread(s) no estado "
                                + estado
                );
            }

            Thread.sleep(10);
        }
    }
}
