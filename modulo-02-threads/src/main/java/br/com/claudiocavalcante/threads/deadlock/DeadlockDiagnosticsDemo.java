package br.com.claudiocavalcante.threads.deadlock;

import java.util.concurrent.CountDownLatch;

public class DeadlockDiagnosticsDemo {

    private static final Object LOCK_A =
            new Object();

    private static final Object LOCK_B =
            new Object();

    /*
     * Garante que ambas as threads adquiram
     * seu primeiro monitor antes de tentarem
     * adquirir o segundo.
     */
    private static final CountDownLatch PRIMEIROS_LOCKS_ADQUIRIDOS =
            new CountDownLatch(2);

    public static void main(String[] args)
            throws InterruptedException {

        long pid =
                ProcessHandle.current()
                        .pid();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "MÓDULO 2 - DEADLOCK"
        );

        System.out.println(
                "CRIAÇÃO E DIAGNÓSTICO"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "\nPID da JVM: " + pid
        );

        Thread threadA =
                Thread.ofPlatform()
                        .name("deadlock-thread-A")
                        .unstarted(() -> {

                            synchronized (LOCK_A) {

                                System.out.println(
                                        "[deadlock-thread-A] LOCK_A adquirido."
                                );

                                PRIMEIROS_LOCKS_ADQUIRIDOS.countDown();

                                try {

                                    /*
                                     * Aguarda a outra thread
                                     * também adquirir LOCK_B.
                                     */
                                    PRIMEIROS_LOCKS_ADQUIRIDOS.await();

                                } catch (InterruptedException e) {

                                    Thread.currentThread()
                                            .interrupt();

                                    return;
                                }

                                System.out.println(
                                        "[deadlock-thread-A] tentando adquirir LOCK_B..."
                                );

                                /*
                                 * LOCK_B já pertence à Thread B.
                                 */
                                synchronized (LOCK_B) {

                                    System.out.println(
                                            "[deadlock-thread-A] LOCK_B adquirido."
                                    );
                                }
                            }
                        });

        Thread threadB =
                Thread.ofPlatform()
                        .name("deadlock-thread-B")
                        .unstarted(() -> {

                            synchronized (LOCK_B) {

                                System.out.println(
                                        "[deadlock-thread-B] LOCK_B adquirido."
                                );

                                PRIMEIROS_LOCKS_ADQUIRIDOS.countDown();

                                try {

                                    /*
                                     * Aguarda a outra thread
                                     * também adquirir LOCK_A.
                                     */
                                    PRIMEIROS_LOCKS_ADQUIRIDOS.await();

                                } catch (InterruptedException e) {

                                    Thread.currentThread()
                                            .interrupt();

                                    return;
                                }

                                System.out.println(
                                        "[deadlock-thread-B] tentando adquirir LOCK_A..."
                                );

                                /*
                                 * LOCK_A já pertence à Thread A.
                                 */
                                synchronized (LOCK_A) {

                                    System.out.println(
                                            "[deadlock-thread-B] LOCK_A adquirido."
                                    );
                                }
                            }
                        });

        threadA.start();
        threadB.start();

        /*
         * Dá tempo para o deadlock se estabelecer.
         */
        Thread.sleep(1000);

        System.out.println(
                "\n=== ESTADOS OBSERVADOS ==="
        );

        System.out.println(
                threadA.getName()
                        + " -> "
                        + threadA.getState()
        );

        System.out.println(
                threadB.getName()
                        + " -> "
                        + threadB.getState()
        );

        System.out.println(
                "\n========================================"
        );

        System.out.println(
                "DEADLOCK CRIADO INTENCIONALMENTE."
        );

        System.out.println(
                "Use jcmd e jstack em outro terminal."
        );

        System.out.println(
                "PID: " + pid
        );

        System.out.println(
                "========================================"
        );

        /*
         * A main também ficará aguardando.
         *
         * Como as duas workers estão em deadlock,
         * estes joins não retornarão.
         */
        threadA.join();
        threadB.join();
    }
}