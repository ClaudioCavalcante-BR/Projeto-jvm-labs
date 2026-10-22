package br.com.claudiocavalcante.jvm.jit;

public class JitCompilerDemo {

    private static final int ITERACOES = 20_000_000;

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" JVM LAB - INTERPRETER x JIT");
        System.out.println("========================================");

        long inicio = System.nanoTime();

        long resultado = 0;

        for (int i = 0; i < ITERACOES; i++) {
            resultado += calcular(i);
        }

        long fim = System.nanoTime();

        double tempoMs =
                (fim - inicio) / 1_000_000.0;

        System.out.println();
        System.out.println("Iteracoes = " + ITERACOES);
        System.out.println("Resultado = " + resultado);
        System.out.println("Tempo = " + tempoMs + " ms");

        System.out.println();
        System.out.println("========================================");
    }

    private static long calcular(long valor) {

        return (valor * 31) + 17;
    }
}
