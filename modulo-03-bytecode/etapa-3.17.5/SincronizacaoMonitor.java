
public class SincronizacaoMonitor {

    private static int contador = 0;

    private static final Object lock = new Object();

    public static void incrementar() {

        synchronized (lock) {
            contador++;
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        int quantidadeThreads = 4;
        int incrementosPorThread = 100000;

        Thread[] threads = new Thread[quantidadeThreads];

        for (int i = 0; i < quantidadeThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0;
                     j < incrementosPorThread;
                     j++) {

                    incrementar();
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int esperado =
                quantidadeThreads * incrementosPorThread;

        System.out.println(
                "Resultado esperado: " + esperado
        );

        System.out.println(
                "Resultado obtido: " + contador
        );
    }
}
