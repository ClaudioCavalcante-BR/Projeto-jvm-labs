package br.com.claudiocavalcante.jvm.memory;

public class StackFrameDemo {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" JVM LAB - STACK FRAMES");
        System.out.println("========================================");

        System.out.println();
        System.out.println("Entrando em main()");

        metodoA();

        System.out.println();
        System.out.println("Retornando para main()");

        System.out.println();
        System.out.println("========================================");
    }

    private static void metodoA() {

        int valorA = 10;

        System.out.println();
        System.out.println("Entrando em metodoA()");
        System.out.println("valorA = " + valorA);

        metodoB();

        System.out.println("Saindo de metodoA()");
    }

    private static void metodoB() {

        int valorB = 20;

        System.out.println();
        System.out.println("Entrando em metodoB()");
        System.out.println("valorB = " + valorB);

        metodoC();

        System.out.println("Saindo de metodoB()");
    }

    private static void metodoC() {

        int valorC = 30;

        System.out.println();
        System.out.println("Entrando em metodoC()");
        System.out.println("valorC = " + valorC);

        System.out.println();
        System.out.println("Stack trace atual:");

        for (StackTraceElement elemento :
                Thread.currentThread().getStackTrace()) {

            System.out.println(" -> " + elemento);
        }

        System.out.println();
        System.out.println("Saindo de metodoC()");
    }
}
