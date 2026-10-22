package br.com.jvmlabs.laboratorioapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/threads")
public class AtomicIntegerController {

    private static final int TOTAL_THREADS = 8;
    private static final int INCREMENTOS_POR_THREAD = 100_000;

    private static class ContadorVolatile {
        volatile int valor = 0;

        void incrementar() {
            valor++;
        }
    }

    @GetMapping("/atomic-integer")
    public Map<String, Object> executar()
            throws InterruptedException {

        ContadorVolatile contadorVolatile =
                new ContadorVolatile();

        AtomicInteger contadorAtomico =
                new AtomicInteger(0);

        CountDownLatch inicio =
                new CountDownLatch(1);

        CountDownLatch conclusao =
                new CountDownLatch(TOTAL_THREADS);

        for (int i = 0; i < TOTAL_THREADS; i++) {

            Thread thread = new Thread(() -> {

                try {
                    inicio.await();

                    for (int j = 0;
                         j < INCREMENTOS_POR_THREAD;
                         j++) {

                        contadorVolatile.incrementar();

                        contadorAtomico.incrementAndGet();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                } finally {
                    conclusao.countDown();
                }

            }, "Thread-Atomic-" + i);

            thread.start();
        }

        inicio.countDown();
        conclusao.await();

        int esperado =
                TOTAL_THREADS * INCREMENTOS_POR_THREAD;

        int resultadoVolatile =
                contadorVolatile.valor;

        int resultadoAtomico =
                contadorAtomico.get();

        Map<String, Object> resultado =
                new LinkedHashMap<>();

        resultado.put(
                "laboratorio",
                "volatile e AtomicInteger"
        );

        resultado.put("threads", TOTAL_THREADS);

        resultado.put(
                "incrementosPorThread",
                INCREMENTOS_POR_THREAD
        );

        resultado.put("resultadoEsperado", esperado);

        resultado.put(
                "contadorVolatile",
                resultadoVolatile
        );

        resultado.put(
                "contadorAtomicInteger",
                resultadoAtomico
        );

        resultado.put(
                "atualizacoesPerdidasVolatile",
                esperado - resultadoVolatile
        );

        resultado.put(
                "atomicIntegerCorreto",
                resultadoAtomico == esperado
        );

        resultado.put("execucao", "Concluída");

        return resultado;
    }
}