
public class RaceConditionSincronizada {

    private static int contador = 0;

    private static final int NUMERO_THREADS = 4;
    private static final int INCREMENTOS_POR_THREAD = 100_000;

    // Apenas uma thread por vez pode executar
    // este método usando o monitor da classe.
    private static synchronized void incrementar() {
        contador++;
    }

    public static void main(String[] args)
            throws InterruptedException {

        Thread[] threads = new Thread[NUMERO_THREADS];

        // Criação das threads
        for (int i = 0; i < NUMERO_THREADS; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0;
                     j < INCREMENTOS_POR_THREAD;
                     j++) {

                    incrementar();
                }
            });
        }

        // Inicialização das threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Aguarda a conclusão de todas as threads
        for (Thread thread : threads) {
            thread.join();
        }

        int resultadoEsperado =
                NUMERO_THREADS * INCREMENTOS_POR_THREAD;

        int incrementosPerdidos =
                resultadoEsperado - contador;

        System.out.println("=== EXPERIMENTO 3.17.3 ===");
        System.out.println("Race Condition com synchronized - Java 21");
        System.out.println();

        System.out.println("Numero de threads: " + NUMERO_THREADS);

        System.out.println(
                "Incrementos por thread: " + INCREMENTOS_POR_THREAD
        );

        System.out.println(
                "Resultado esperado: " + resultadoEsperado
        );

        System.out.println(
                "Resultado obtido: " + contador
        );

        System.out.println(
                "Incrementos perdidos: " + incrementosPerdidos
        );
    }
}
