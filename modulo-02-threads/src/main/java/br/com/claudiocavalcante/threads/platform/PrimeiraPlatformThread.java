package br.com.claudiocavalcante.threads.platform;

public class PrimeiraPlatformThread {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=================================");
        System.out.println("MÓDULO 2 - PLATFORM THREAD");
        System.out.println("=================================");

        // Thread que está executando o método main
        Thread mainThread = Thread.currentThread();

        System.out.println("\n=== THREAD PRINCIPAL ===");
        System.out.println("Nome: " + mainThread.getName());
        System.out.println("Estado: " + mainThread.getState());
        System.out.println("Virtual: " + mainThread.isVirtual());

        // Criação de uma Platform Thread
        Thread worker = Thread.ofPlatform()
                .name("worker-platform-01")
                .start(() -> {

                    Thread atual = Thread.currentThread();

                    System.out.println("\n=== PLATFORM THREAD ===");
                    System.out.println("Nome: " + atual.getName());
                    System.out.println("Estado: " + atual.getState());
                    System.out.println("Virtual: " + atual.isVirtual());

                    System.out.println(
                            "Executando tarefa na thread: "
                                    + atual.getName()
                    );
                });

        // Aguarda a Platform Thread terminar
        worker.join();

        System.out.println("\n=== FINALIZAÇÃO ===");
        System.out.println("Estado final da worker: "
                + worker.getState());

        System.out.println("Aplicação finalizada.");
    }

}
