package br.com.claudiocavalcante.threads;

public class Main {

    public static void main(String[] args) {

        Thread atual = Thread.currentThread();

        System.out.println("==================================");
        System.out.println("MÓDULO 2 - THREADS E CONCORRÊNCIA");
        System.out.println("==================================");

        System.out.println("\nThread atual:");
        System.out.println("Nome: " + atual.getName());
        System.out.println("Estado: " + atual.getState());
        System.out.println("Virtual: " + atual.isVirtual());

        System.out.println("\nJava: "
                + System.getProperty("java.version"));

        System.out.println("\nMódulo 2 iniciado com sucesso.");
    }

}
