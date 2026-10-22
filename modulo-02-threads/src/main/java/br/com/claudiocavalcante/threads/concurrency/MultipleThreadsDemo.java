package br.com.claudiocavalcante.threads.concurrency;

public class MultipleThreadsDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("========================================");
        System.out.println("MÓDULO 2 - CONCORRÊNCIA");
        System.out.println("MÚLTIPLAS PLATFORM THREADS");
        System.out.println("========================================");

        System.out.println("\n=== THREAD PRINCIPAL ===");
        System.out.println(
                "Thread atual: "
                        + Thread.currentThread().getName()
        );

        Thread worker1 = criarWorker(
                "worker-01",
                500
        );

        Thread worker2 = criarWorker(
                "worker-02",
                700
        );

        Thread worker3 = criarWorker(
                "worker-03",
                900
        );

        System.out.println("\n=== ANTES DO start() ===");

        System.out.println(
                worker1.getName() + ": "
                        + worker1.getState()
        );

        System.out.println(
                worker2.getName() + ": "
                        + worker2.getState()
        );

        System.out.println(
                worker3.getName() + ": "
                        + worker3.getState()
        );

        System.out.println("\n=== INICIANDO AS THREADS ===");

        worker1.start();
        worker2.start();
        worker3.start();

        System.out.println(
                "\nmain iniciou todas as workers."
        );

        /*
         * A main aguarda todas terminarem.
         */
        worker1.join();
        worker2.join();
        worker3.join();

        System.out.println(
                "\n=== ESTADO FINAL ==="
        );

        System.out.println(
                worker1.getName() + ": "
                        + worker1.getState()
        );

        System.out.println(
                worker2.getName() + ": "
                        + worker2.getState()
        );

        System.out.println(
                worker3.getName() + ": "
                        + worker3.getState()
        );

        System.out.println(
                "\nTodas as workers finalizaram."
        );

        System.out.println(
                "Aplicação finalizada."
        );
    }

    private static Thread criarWorker(
            String nome,
            long tempo
    ) {

        return Thread.ofPlatform()
                .name(nome)
                .unstarted(() -> {

                    for (int etapa = 1; etapa <= 5; etapa++) {

                        System.out.println(
                                "[" + Thread.currentThread().getName()
                                        + "] executando etapa "
                                        + etapa
                        );

                        try {

                            Thread.sleep(tempo);

                        } catch (InterruptedException e) {

                            Thread.currentThread().interrupt();

                            System.out.println(
                                    "["
                                            + Thread.currentThread().getName()
                                            + "] interrompida."
                            );

                            return;
                        }
                    }

                    System.out.println(
                            "["
                                    + Thread.currentThread().getName()
                                    + "] terminou."
                    );
                });
    }

}
