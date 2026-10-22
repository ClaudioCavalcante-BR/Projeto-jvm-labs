
public class RaceCondition {

    private static int contador = 0;

    private static final int NUMERO_THREADS = 4;
    private static final int INCREMENTOS_POR_THREAD = 100_000;

    public static void main(String[] args)
            throws InterruptedException {

        Thread[] threads = new Thread[NUMERO_THREADS];

        for (int i = 0; i < NUMERO_THREADS; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < INCREMENTOS_POR_THREAD; j++) {
                    contador++;
                }

            });

        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int resultadoEsperado =
                NUMERO_THREADS * INCREMENTOS_POR_THREAD;

        System.out.println("=== EXPERIMENTO 3.17.3 ===");
        System.out.println("Race Condition - Java 21");
        System.out.println();

        System.out.println("Numero de threads: " + NUMERO_THREADS);

        System.out.println(
                "Incrementos por thread: " + INCREMENTOS_POR_THREAD
        );

        System.out.println("Resultado esperado: " + resultadoEsperado);
        System.out.println("Resultado obtido: " + contador);

        System.out.println(
                "Incrementos perdidos: " + (resultadoEsperado - contador)
        );

    }
}
