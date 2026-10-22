package br.com.claudiocavalcante.threads.lifecycle;

public class CicloVidaThread {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("=================================");
        System.out.println("MÓDULO 2 - CICLO DE VIDA THREAD");
        System.out.println("=================================");

        Thread worker = Thread.ofPlatform()
                .name("worker-lifecycle")
                .unstarted(() -> {

                    System.out.println("\n=== THREAD INICIOU ===");

                    System.out.println(
                            "Nome: "
                                    + Thread.currentThread().getName()
                    );

                    System.out.println(
                            "Estado dentro da execução: "
                                    + Thread.currentThread().getState()
                    );

                    try {

                        System.out.println(
                                "\nThread entrando em sleep..."
                        );

                        Thread.sleep(3000);

                        System.out.println(
                                "Thread saiu do sleep."
                        );

                    } catch (InterruptedException e) {

                        Thread.currentThread().interrupt();

                    }

                    System.out.println(
                            "\nThread encerrando execução..."
                    );
                });

        /*
         * ESTADO 1
         * A Thread foi criada, mas ainda não iniciada.
         */
        System.out.println("\n1 - APÓS CRIAÇÃO");
        System.out.println(
                "Estado: " + worker.getState()
        );

        /*
         * Inicia efetivamente a Thread.
         */
        worker.start();

        /*
         * ESTADO 2
         */
        System.out.println("\n2 - APÓS start()");
        System.out.println(
                "Estado: " + worker.getState()
        );

        /*
         * Dá tempo para a worker entrar no sleep().
         */
        Thread.sleep(500);

        /*
         * ESTADO 3
         */
        System.out.println("\n3 - DURANTE sleep()");
        System.out.println(
                "Estado: " + worker.getState()
        );

        /*
         * A main aguarda a worker terminar.
         */
        worker.join();

        /*
         * ESTADO 4
         */
        System.out.println("\n4 - APÓS TÉRMINO");
        System.out.println(
                "Estado: " + worker.getState()
        );

        System.out.println("\nAplicação finalizada.");
    }
}
