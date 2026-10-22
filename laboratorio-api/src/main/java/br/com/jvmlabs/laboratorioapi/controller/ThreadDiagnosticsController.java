package br.com.jvmlabs.laboratorioapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/threads")
public class ThreadDiagnosticsController {

    @GetMapping("/diagnostico")
    public Map<String, Object> executar()
            throws InterruptedException {

        CountDownLatch iniciadas = new CountDownLatch(2);
        CountDownLatch liberar = new CountDownLatch(1);

        Runnable tarefa = () -> {
            iniciadas.countDown();

            try {
                liberar.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread threadA = new Thread(
                tarefa, "Diagnostico-Thread-A"
        );

        Thread threadB = new Thread(
                tarefa, "Diagnostico-Thread-B"
        );

        String estadoInicialA = threadA.getState().name();
        String estadoInicialB = threadB.getState().name();

        threadA.start();
        threadB.start();

        boolean iniciaram = iniciadas.await(
                5, TimeUnit.SECONDS
        );

        if (!iniciaram) {
            liberar.countDown();
            threadA.join();
            threadB.join();

            throw new IllegalStateException(
                    "As threads não iniciaram no prazo esperado"
            );
        }

        // Aguarda brevemente até ambas entrarem em WAITING.
        long limite = System.nanoTime()
                + TimeUnit.SECONDS.toNanos(5);

        while ((threadA.getState() != Thread.State.WAITING
                || threadB.getState() != Thread.State.WAITING)
                && System.nanoTime() < limite) {

            Thread.sleep(10);
        }

        String estadoDuranteA = threadA.getState().name();
        String estadoDuranteB = threadB.getState().name();

        StackTraceElement[] pilhaA = threadA.getStackTrace();
        StackTraceElement[] pilhaB = threadB.getStackTrace();

        liberar.countDown();

        threadA.join();
        threadB.join();

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put(
                "laboratorio",
                "Diagnóstico de Threads Java 21"
        );

        resultado.put("threadsCriadas", 2);

        resultado.put("threadA", threadA.getName());
        resultado.put("threadB", threadB.getName());

        resultado.put("estadoInicialA", estadoInicialA);
        resultado.put("estadoInicialB", estadoInicialB);

        resultado.put("estadoDuranteA", estadoDuranteA);
        resultado.put("estadoDuranteB", estadoDuranteB);

        resultado.put(
                "estadoFinalA",
                threadA.getState().name()
        );

        resultado.put(
                "estadoFinalB",
                threadB.getState().name()
        );

        resultado.put(
                "elementosPilhaA",
                pilhaA.length
        );

        resultado.put(
                "elementosPilhaB",
                pilhaB.length
        );

        resultado.put(
                "esperaConfirmada",
                "WAITING".equals(estadoDuranteA)
                        && "WAITING".equals(estadoDuranteB)
        );

        resultado.put("execucao", "Concluída");

        return resultado;
    }
}