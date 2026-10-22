package br.com.claudiocavalcante.jvm.memory;

public class MemoryDemo {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" JVM LAB - HEAP x JVM STACK");
        System.out.println("========================================");

        int numeroLocal = 100;

        Pessoa pessoa1 =
                new Pessoa("Pessoa A", 30);

        Pessoa pessoa2 =
                new Pessoa("Pessoa B", 40);

        System.out.println();
        System.out.println("Variavel local:");
        System.out.println("numeroLocal = " + numeroLocal);

        System.out.println();
        System.out.println("Objetos:");
        System.out.println("pessoa1 = " + pessoa1);
        System.out.println("pessoa2 = " + pessoa2);

        System.out.println();
        System.out.println("Identidade dos objetos:");

        System.out.println(
                "pessoa1 identityHashCode = "
                        + System.identityHashCode(pessoa1)
        );

        System.out.println(
                "pessoa2 identityHashCode = "
                        + System.identityHashCode(pessoa2)
        );

        System.out.println();

        executarMetodo(pessoa1);

        System.out.println();
        System.out.println("========================================");
    }

    private static void executarMetodo(Pessoa pessoa) {

        int valorMetodo = 500;

        System.out.println("Dentro de executarMetodo():");

        System.out.println(
                "Pessoa recebida = " + pessoa.getNome()
        );

        System.out.println(
                "valorMetodo = " + valorMetodo
        );
    }

}
