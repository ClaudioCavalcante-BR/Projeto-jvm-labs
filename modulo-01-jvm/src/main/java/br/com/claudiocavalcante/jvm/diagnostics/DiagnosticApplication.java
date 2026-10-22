package br.com.claudiocavalcante.jvm.diagnostics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DiagnosticApplication {

    private static final Object LOCK = new Object();

    private static final CountDownLatch KEEP_ALIVE =
            new CountDownLatch(1);

    public static void main(String[] args)
            throws InterruptedException {

        long pid = ProcessHandle.current().pid();

        System.out.println(
                "=============================================="
        );

        System.out.println(
                " JVM LAB - DIAGNOSTICO E OBSERVABILIDADE"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println();
        System.out.println("PID da JVM: " + pid);

        System.out.println();
        System.out.println("Threads de laboratorio:");

        System.out.println(" - diag-cpu-worker");
        System.out.println(" - diag-sleep-worker");
        System.out.println(" - diag-lock-owner");
        System.out.println(" - diag-lock-blocked");
        System.out.println(" - diag-allocation-worker");

        iniciarCpuWorker();
        iniciarSleepWorker();
        iniciarLockWorkers();
        iniciarAllocationWorker();

        System.out.println();
        System.out.println(
                "Aplicacao pronta para diagnostico."
        );

        System.out.println(
                "Use jcmd, jstack e JFR em outro Terminal."
        );

        System.out.println();
        System.out.println(
                "Interrompa pelo botao Stop do IntelliJ."
        );

        KEEP_ALIVE.await();
    }

    private static void iniciarCpuWorker() {

        Thread thread = new Thread(() -> {

            long resultado = 0;

            while (true) {

                for (int i = 1; i <= 2_000_000; i++) {

                    resultado +=
                            (i * 31L) % 97;
                }

                if (resultado == Long.MIN_VALUE) {

                    System.out.println(resultado);
                }

                dormir(50);
            }

        }, "diag-cpu-worker");

        thread.start();
    }

    private static void iniciarSleepWorker() {

        Thread thread = new Thread(() -> {

            while (true) {

                dormir(5_000);
            }

        }, "diag-sleep-worker");

        thread.start();
    }

    private static void iniciarLockWorkers()
            throws InterruptedException {

        Thread owner = new Thread(() -> {

            synchronized (LOCK) {

                while (true) {

                    dormir(1_000);
                }
            }

        }, "diag-lock-owner");

        owner.start();

        Thread.sleep(300);

        Thread blocked = new Thread(() -> {

            synchronized (LOCK) {

                System.out.println(
                        "Lock obtido por diag-lock-blocked."
                );
            }

        }, "diag-lock-blocked");

        blocked.start();
    }

    private static void iniciarAllocationWorker() {

        Thread thread = new Thread(() -> {

            List<byte[]> blocos =
                    new ArrayList<>();

            while (true) {

                blocos.add(
                        new byte[256 * 1024]
                );

                if (blocos.size() >= 16) {

                    blocos.clear();
                }

                dormir(100);
            }

        }, "diag-allocation-worker");

        thread.start();
    }

    private static void dormir(long millis) {

        try {

            Thread.sleep(millis);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

}
