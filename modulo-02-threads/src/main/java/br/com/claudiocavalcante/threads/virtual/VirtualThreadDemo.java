package br.com.claudiocavalcante.threads.virtual;

public class VirtualThreadDemo {

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "MÓDULO 2 - VIRTUAL THREADS"
        );

        System.out.println(
                "JAVA 21"
        );

        System.out.println(
                "========================================"
        );

        Thread mainThread =
                Thread.currentThread();

        System.out.println(
                "\n=== THREAD PRINCIPAL ==="
        );

        System.out.println(
                "Nome: "
                        + mainThread.getName()
        );

        System.out.println(
                "Virtual: "
                        + mainThread.isVirtual()
        );

        /*
         * ========================================
         * PLATFORM THREAD
         * ========================================
         */

        Thread platformThread =
                Thread.ofPlatform()
                        .name("platform-worker")
                        .unstarted(() -> {

                            Thread atual =
                                    Thread.currentThread();

                            System.out.println(
                                    "\n=== PLATFORM THREAD ==="
                            );

                            System.out.println(
                                    "Nome: "
                                            + atual.getName()
                            );

                            System.out.println(
                                    "Virtual: "
                                            + atual.isVirtual()
                            );

                            System.out.println(
                                    "Estado: "
                                            + atual.getState()
                            );
                        });

        /*
         * ========================================
         * VIRTUAL THREAD
         * ========================================
         */

        Thread virtualThread =
                Thread.ofVirtual()
                        .name("virtual-worker")
                        .unstarted(() -> {

                            Thread atual =
                                    Thread.currentThread();

                            System.out.println(
                                    "\n=== VIRTUAL THREAD ==="
                            );

                            System.out.println(
                                    "Nome: "
                                            + atual.getName()
                            );

                            System.out.println(
                                    "Virtual: "
                                            + atual.isVirtual()
                            );

                            System.out.println(
                                    "Estado: "
                                            + atual.getState()
                            );

                            try {

                                System.out.println(
                                        "Virtual Thread entrando em sleep..."
                                );

                                Thread.sleep(2000);

                                System.out.println(
                                        "Virtual Thread saiu do sleep."
                                );

                            } catch (InterruptedException e) {

                                Thread.currentThread()
                                        .interrupt();
                            }
                        });

        System.out.println(
                "\n=== ANTES DO start() ==="
        );

        System.out.println(
                "Platform: "
                        + platformThread.getState()
        );

        System.out.println(
                "Virtual : "
                        + virtualThread.getState()
        );

        platformThread.start();
        virtualThread.start();

        Thread.sleep(500);

        System.out.println(
                "\n=== DURANTE A EXECUÇÃO ==="
        );

        System.out.println(
                "Platform: "
                        + platformThread.getState()
        );

        System.out.println(
                "Virtual : "
                        + virtualThread.getState()
        );

        platformThread.join();
        virtualThread.join();

        System.out.println(
                "\n=== ESTADO FINAL ==="
        );

        System.out.println(
                "Platform: "
                        + platformThread.getState()
        );

        System.out.println(
                "Virtual : "
                        + virtualThread.getState()
        );

        System.out.println(
                "\nAplicação finalizada."
        );
    }
}