package br.com.claudiocavalcante.threads.execution;

public class StartRunSleepJoinDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("========================================");
        System.out.println("MÓDULO 2 - START, RUN, SLEEP E JOIN");
        System.out.println("========================================");

        System.out.println("\n=== THREAD PRINCIPAL ===");
        System.out.println(
                "Thread atual: "
                        + Thread.currentThread().getName()
        );

        /*
         * =====================================================
         * TESTE 1 - CHAMADA DIRETA AO run()
         * =====================================================
         */

        Thread threadRun = Thread.ofPlatform()
                .name("worker-run")
                .unstarted(() -> {

                    System.out.println("\n=== TESTE run() ===");

                    System.out.println(
                            "Thread executando a tarefa: "
                                    + Thread.currentThread().getName()
                    );

                    System.out.println(
                            "Virtual: "
                                    + Thread.currentThread().isVirtual()
                    );
                });

        System.out.println("\nEstado antes de run(): "
                + threadRun.getState());

        /*
         * Chamada normal de método.
         * NÃO inicia uma nova thread.
         */
        threadRun.run();

        System.out.println(
                "Estado após run(): "
                        + threadRun.getState()
        );

        /*
         * =====================================================
         * TESTE 2 - CHAMADA AO start()
         * =====================================================
         */

        Thread threadStart = Thread.ofPlatform()
                .name("worker-start")
                .unstarted(() -> {

                    System.out.println("\n=== TESTE start() ===");

                    System.out.println(
                            "Thread executando a tarefa: "
                                    + Thread.currentThread().getName()
                    );

                    System.out.println(
                            "Estado dentro da worker: "
                                    + Thread.currentThread().getState()
                    );

                    try {

                        System.out.println(
                                "\nworker-start entrando em sleep..."
                        );

                        Thread.sleep(2000);

                        System.out.println(
                                "worker-start saiu do sleep."
                        );

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();

                    }

                    System.out.println(
                            "worker-start encerrando."
                    );
                });

        System.out.println("\nEstado antes de start(): "
                + threadStart.getState());

        threadStart.start();

        /*
         * Dá tempo para a worker entrar no sleep().
         */
        Thread.sleep(500);

        System.out.println(
                "\nEstado observado pela main durante sleep(): "
                        + threadStart.getState()
        );

        /*
         * =====================================================
         * TESTE 3 - join()
         * =====================================================
         */

        System.out.println(
                "\nmain executará join() e aguardará a worker."
        );

        threadStart.join();

        System.out.println(
                "join() retornou."
        );

        System.out.println(
                "Estado final da worker: "
                        + threadStart.getState()
        );

        System.out.println(
                "\nAplicação finalizada."
        );
    }

}
