package br.com.claudiocavalcante.threads.diagnostics;

import java.util.concurrent.CountDownLatch;

public class ThreadDiagnosticsDemo {

    private static final Object WAIT_MONITOR =
            new Object();

    private static final Object BLOCK_MONITOR =
            new Object();

    private static final CountDownLatch LOCK_ADQUIRIDO =
            new CountDownLatch(1);

    private static volatile boolean executando = true;

    public static void main(String[] args)
            throws InterruptedException {

        long pid =
                ProcessHandle.current()
                        .pid();

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - THREAD DIAGNOSTICS"
        );
        System.out.println(
                "JCMD | JSTACK | JFR"
        );
        System.out.println(
                "========================================"
        );

        System.out.println(
                "\nPID da JVM: " + pid
        );

        /*
         * ========================================
         * 1 - THREAD RUNNABLE
         * ========================================
         */
        Thread cpuWorker =
                Thread.ofPlatform()
                        .name("diag-cpu-worker")
                        .unstarted(() -> {

                            long contador = 0;

                            while (executando) {

                                contador++;

                                /*
                                 * Mantém a thread ativa,
                                 * mas permite ao scheduler
                                 * alternar a execução.
                                 */
                                if ((contador & 0xFFFFF) == 0) {

                                    Thread.yield();
                                }
                            }
                        });

        /*
         * ========================================
         * 2 - THREAD TIMED_WAITING
         * ========================================
         */
        Thread sleepWorker =
                Thread.ofPlatform()
                        .name("diag-sleep-worker")
                        .unstarted(() -> {

                            while (executando) {

                                try {

                                    Thread.sleep(10_000);

                                } catch (InterruptedException e) {

                                    if (!executando) {
                                        return;
                                    }

                                    Thread.currentThread()
                                            .interrupt();

                                    return;
                                }
                            }
                        });

        /*
         * ========================================
         * 3 - THREAD WAITING
         * ========================================
         */
        Thread waitWorker =
                Thread.ofPlatform()
                        .name("diag-wait-worker")
                        .unstarted(() -> {

                            synchronized (WAIT_MONITOR) {

                                while (executando) {

                                    try {

                                        WAIT_MONITOR.wait();

                                    } catch (InterruptedException e) {

                                        Thread.currentThread()
                                                .interrupt();

                                        return;
                                    }
                                }
                            }
                        });

        /*
         * ========================================
         * 4 - THREAD QUE POSSUI O MONITOR
         * ========================================
         */
        Thread lockHolder =
                Thread.ofPlatform()
                        .name("diag-lock-holder")
                        .unstarted(() -> {

                            synchronized (BLOCK_MONITOR) {

                                System.out.println(
                                        "[diag-lock-holder] "
                                                + "monitor adquirido."
                                );

                                /*
                                 * Sinaliza que já possui
                                 * BLOCK_MONITOR.
                                 */
                                LOCK_ADQUIRIDO.countDown();

                                try {

                                    Thread.sleep(180_000);

                                } catch (InterruptedException e) {

                                    /*
                                     * Permite finalizar
                                     * o laboratório.
                                     */
                                }
                            }
                        });

        /*
         * ========================================
         * 5 - THREAD BLOCKED
         * ========================================
         */
        Thread blockedWorker =
                Thread.ofPlatform()
                        .name("diag-blocked-worker")
                        .unstarted(() -> {

                            try {

                                /*
                                 * Só tenta entrar depois
                                 * que lockHolder já possui
                                 * o monitor.
                                 */
                                LOCK_ADQUIRIDO.await();

                            } catch (InterruptedException e) {

                                Thread.currentThread()
                                        .interrupt();

                                return;
                            }

                            System.out.println(
                                    "[diag-blocked-worker] "
                                            + "tentando adquirir monitor..."
                            );

                            /*
                             * Enquanto lockHolder possuir
                             * BLOCK_MONITOR, esta thread
                             * ficará BLOCKED.
                             */
                            synchronized (BLOCK_MONITOR) {

                                System.out.println(
                                        "[diag-blocked-worker] "
                                                + "monitor adquirido."
                                );
                            }
                        });

        /*
         * ========================================
         * INICIALIZAÇÃO
         * ========================================
         */

        cpuWorker.start();
        sleepWorker.start();
        waitWorker.start();
        lockHolder.start();
        blockedWorker.start();

        /*
         * Dá tempo para as threads
         * atingirem os estados planejados.
         */
        Thread.sleep(1000);

        System.out.println(
                "\n=== ESTADOS OBSERVADOS PELA APLICAÇÃO ==="
        );

        imprimirEstado(cpuWorker);
        imprimirEstado(sleepWorker);
        imprimirEstado(waitWorker);
        imprimirEstado(lockHolder);
        imprimirEstado(blockedWorker);

        System.out.println(
                "\n========================================"
        );

        System.out.println(
                "Aplicação permanecerá ativa por 180 segundos."
        );

        System.out.println(
                "Use agora jcmd, jstack e JFR em outro terminal."
        );

        System.out.println(
                "PID: " + pid
        );

        System.out.println(
                "========================================"
        );

        /*
         * 3 minutos para coleta das evidências.
         */
        Thread.sleep(180_000);

        /*
         * ========================================
         * FINALIZAÇÃO CONTROLADA
         * ========================================
         */

        executando = false;

        sleepWorker.interrupt();

        synchronized (WAIT_MONITOR) {

            WAIT_MONITOR.notifyAll();
        }

        /*
         * Interrompe o sleep do holder,
         * permitindo liberar BLOCK_MONITOR.
         */
        lockHolder.interrupt();

        cpuWorker.join();
        sleepWorker.join();
        waitWorker.join();
        lockHolder.join();
        blockedWorker.join();

        System.out.println(
                "\nAplicação finalizada."
        );
    }

    private static void imprimirEstado(
            Thread thread
    ) {

        System.out.println(
                thread.getName()
                        + " -> "
                        + thread.getState()
        );
    }
}