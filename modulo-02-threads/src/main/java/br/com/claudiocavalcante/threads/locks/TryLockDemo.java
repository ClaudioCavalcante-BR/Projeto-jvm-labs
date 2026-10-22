package br.com.claudiocavalcante.threads.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockDemo {

    private static final Lock lock =
            new ReentrantLock();

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "MÓDULO 2 - TRYLOCK"
        );
        System.out.println(
                "TENTATIVA DE AQUISIÇÃO DE LOCK"
        );
        System.out.println(
                "========================================"
        );

        /*
         * A própria main adquire o lock primeiro.
         * Isso torna o experimento determinístico:
         * as outras threads encontrarão o lock ocupado.
         */
        lock.lock();

        System.out.println(
                "\n[main] lock adquirido."
        );

        Thread tentativaImediata =
                Thread.ofPlatform()
                        .name("worker-trylock-imediato")
                        .unstarted(() -> {

                            long inicio =
                                    System.nanoTime();

                            System.out.println(
                                    "\n["
                                            + Thread.currentThread().getName()
                                            + "] tentando tryLock()..."
                            );

                            boolean conseguiu =
                                    lock.tryLock();

                            long tempoMs =
                                    TimeUnit.NANOSECONDS
                                            .toMillis(
                                                    System.nanoTime()
                                                            - inicio
                                            );

                            System.out.println(
                                    "["
                                            + Thread.currentThread().getName()
                                            + "] conseguiu lock? "
                                            + conseguiu
                            );

                            System.out.println(
                                    "["
                                            + Thread.currentThread().getName()
                                            + "] tempo da tentativa: "
                                            + tempoMs
                                            + " ms"
                            );

                            if (conseguiu) {

                                try {

                                    System.out.println(
                                            "["
                                                    + Thread.currentThread()
                                                    .getName()
                                                    + "] região crítica."
                                    );

                                } finally {

                                    lock.unlock();
                                }

                            } else {

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] lock ocupado."
                                );

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] continuará sem bloquear."
                                );
                            }
                        });

        Thread tentativaComTimeout =
                Thread.ofPlatform()
                        .name("worker-trylock-timeout")
                        .unstarted(() -> {

                            long inicio =
                                    System.nanoTime();

                            System.out.println(
                                    "\n["
                                            + Thread.currentThread().getName()
                                            + "] tentando tryLock(5 segundos)..."
                            );

                            boolean conseguiu = false;

                            try {

                                conseguiu =
                                        lock.tryLock(
                                                5,
                                                TimeUnit.SECONDS
                                        );

                                long tempoMs =
                                        TimeUnit.NANOSECONDS
                                                .toMillis(
                                                        System.nanoTime()
                                                                - inicio
                                                );

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] conseguiu lock? "
                                                + conseguiu
                                );

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] tempo de espera: "
                                                + tempoMs
                                                + " ms"
                                );

                                if (conseguiu) {

                                    System.out.println(
                                            "["
                                                    + Thread.currentThread()
                                                    .getName()
                                                    + "] entrou na região crítica."
                                    );
                                }

                            } catch (InterruptedException e) {

                                Thread.currentThread()
                                        .interrupt();

                                System.out.println(
                                        "["
                                                + Thread.currentThread()
                                                .getName()
                                                + "] foi interrompida."
                                );

                            } finally {

                                if (conseguiu) {

                                    lock.unlock();

                                    System.out.println(
                                            "["
                                                    + Thread.currentThread()
                                                    .getName()
                                                    + "] lock liberado."
                                    );
                                }
                            }
                        });

        tentativaImediata.start();
        tentativaComTimeout.start();

        /*
         * A main mantém o lock por 3 segundos.
         */
        System.out.println(
                "\n[main] manterá o lock por 3 segundos..."
        );

        Thread.sleep(3000);

        lock.unlock();

        System.out.println(
                "\n[main] lock liberado."
        );

        tentativaImediata.join();
        tentativaComTimeout.join();

        System.out.println(
                "\n=== ESTADO FINAL ==="
        );

        System.out.println(
                tentativaImediata.getName()
                        + ": "
                        + tentativaImediata.getState()
        );

        System.out.println(
                tentativaComTimeout.getName()
                        + ": "
                        + tentativaComTimeout.getState()
        );

        System.out.println(
                "\nAplicação finalizada."
        );
    }

}
