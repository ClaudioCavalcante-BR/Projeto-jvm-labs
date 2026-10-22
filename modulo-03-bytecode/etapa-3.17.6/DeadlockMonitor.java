
import java.util.concurrent.CountDownLatch;

public class DeadlockMonitor {

    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    private static final CountDownLatch BARREIRA =
            new CountDownLatch(2);

    public static void executarThreadA() {

        synchronized (LOCK_1) {

            System.out.println(
                    "Thread A adquiriu LOCK_1"
            );

            BARREIRA.countDown();

            aguardarBarreira();

            System.out.println(
                    "Thread A aguardando LOCK_2"
            );

            synchronized (LOCK_2) {
                System.out.println(
                        "Thread A adquiriu LOCK_2"
                );
            }
        }
    }

    public static void executarThreadB() {

        synchronized (LOCK_2) {

            System.out.println(
                    "Thread B adquiriu LOCK_2"
            );

            BARREIRA.countDown();

            aguardarBarreira();

            System.out.println(
                    "Thread B aguardando LOCK_1"
            );

            synchronized (LOCK_1) {
                System.out.println(
                        "Thread B adquiriu LOCK_1"
                );
            }
        }
    }

    private static void aguardarBarreira() {

        try {
            BARREIRA.await();
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Interrupção durante a sincronização",
                    e
            );
        }
    }

    public static void main(String[] args) {

        Thread threadA = new Thread(
                DeadlockMonitor::executarThreadA,
                "Deadlock-Thread-A"
        );

        Thread threadB = new Thread(
                DeadlockMonitor::executarThreadB,
                "Deadlock-Thread-B"
        );

        threadA.start();
        threadB.start();

        System.out.println(
                "Experimento 3.17.6 iniciado."
        );
    }
}
