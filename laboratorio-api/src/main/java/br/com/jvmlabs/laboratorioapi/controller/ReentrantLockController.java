package br.com.jvmlabs.laboratorioapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/threads")
public class ReentrantLockController {

    @GetMapping("/reentrant-lock")
    public Map<String, Object> executar() throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();

        CountDownLatch lockAdquirido = new CountDownLatch(1);
        CountDownLatch tentativaConcluida = new CountDownLatch(1);

        AtomicBoolean primeiraTentativa = new AtomicBoolean(false);
        AtomicBoolean segundaTentativa = new AtomicBoolean(false);

        Thread threadA = new Thread(() -> {

            lock.lock();

            try {
                lockAdquirido.countDown();

                // Mantém o recurso ocupado durante a
                // primeira tentativa da Thread B.
                tentativaConcluida.await();

                TimeUnit.SECONDS.sleep(2);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

        }, "thread-A");

        Thread threadB = new Thread(() -> {

            try {
                // Garante que A adquiriu o lock.
                lockAdquirido.await();

                boolean conseguiu = lock.tryLock();

                primeiraTentativa.set(conseguiu);

                if (conseguiu) {
                    try {
                        System.out.println(
                                "Thread B adquiriu o lock na primeira tentativa."
                        );
                    } finally {
                        lock.unlock();
                    }
                }

                tentativaConcluida.countDown();

                // Aguarda a conclusão da Thread A.
                threadA.join();

                boolean conseguiuDepois = lock.tryLock();

                segundaTentativa.set(conseguiuDepois);

                if (conseguiuDepois) {
                    try {
                        System.out.println(
                                "Thread B adquiriu o lock após a liberação."
                        );
                    } finally {
                        lock.unlock();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                tentativaConcluida.countDown();
            }

        }, "thread-B");

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put("laboratorio", "ReentrantLock e tryLock");
        resultado.put("threadA", "Adquiriu e liberou o lock");
        resultado.put("primeiraTentativa", primeiraTentativa.get());
        resultado.put("segundaTentativa", segundaTentativa.get());
        resultado.put("lockLiberado", !lock.isLocked());
        resultado.put("execucao", "Concluída");

        return resultado;
    }
}