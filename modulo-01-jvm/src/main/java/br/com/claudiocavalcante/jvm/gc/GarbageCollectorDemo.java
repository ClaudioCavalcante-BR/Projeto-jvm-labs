package br.com.claudiocavalcante.jvm.gc;

import java.util.ArrayList;
import java.util.List;

public class GarbageCollectorDemo {

    private static final int TAMANHO_BLOCO =
            512 * 1024; // 512 KB

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" JVM LAB - GARBAGE COLLECTOR");
        System.out.println("========================================");

        Runtime runtime = Runtime.getRuntime();

        exibirMemoria("Memoria inicial", runtime);

        List<byte[]> referencias = new ArrayList<>();

        System.out.println();
        System.out.println("Iniciando alocacoes...");

        for (int i = 1; i <= 200; i++) {

            byte[] bloco = new byte[TAMANHO_BLOCO];

            referencias.add(bloco);

            if (i % 8 == 0) {

                System.out.println();
                System.out.println(
                        "Lote " + i
                                + " - removendo referencias"
                );

                referencias.clear();
            }
        }

        System.out.println();
        exibirMemoria("Memoria apos alocacoes", runtime);

        System.out.println();
        System.out.println("Fim do experimento.");

        System.out.println("========================================");
    }

    private static void exibirMemoria(
            String titulo,
            Runtime runtime) {

        long memoriaTotal =
                runtime.totalMemory();

        long memoriaLivre =
                runtime.freeMemory();

        long memoriaUsada =
                memoriaTotal - memoriaLivre;

        System.out.println();
        System.out.println(titulo + ":");

        System.out.println(
                "Memoria usada  = "
                        + converterMB(memoriaUsada)
                        + " MB"
        );

        System.out.println(
                "Memoria livre  = "
                        + converterMB(memoriaLivre)
                        + " MB"
        );

        System.out.println(
                "Memoria total  = "
                        + converterMB(memoriaTotal)
                        + " MB"
        );
    }

    private static long converterMB(long bytes) {

        return bytes / (1024 * 1024);
    }

}
