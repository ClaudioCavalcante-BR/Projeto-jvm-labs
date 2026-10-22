package br.com.jvmlabs.laboratorioapi.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service
public class RaceConditionService {

    private static final int THREADS = 8;
    private static final int INCREMENTOS = 100_000;

    private int contadorSemSincronizacao;
    private int contadorComSincronizacao;

    public Map<String, Object> executarExperimento()
            throws InterruptedException {

        int esperado = THREADS * INCREMENTOS;

        int semSincronizacao = executarTeste(false);
        int comSincronizacao = executarTeste(true);

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put("threads", THREADS);
        resultado.put("incrementosPorThread", INCREMENTOS);
        resultado.put("resultadoEsperado", esperado);
        resultado.put("semSincronizacao", semSincronizacao);
        resultado.put("comSincronizacao", comSincronizacao);
        resultado.put("atualizacoesPerdidas",
                esperado - semSincronizacao);

        return resultado;
    }

    private int executarTeste(boolean sincronizado)
            throws InterruptedException {

        // Cada requisição usa um contador próprio.
        Contador contador = new Contador();

        CountDownLatch inicio = new CountDownLatch(1);
        CountDownLatch fim = new CountDownLatch(THREADS);

        for (int i = 0; i < THREADS; i++) {

            Thread thread = new Thread(() -> {
                try {
                    inicio.await();

                    for (int j = 0; j < INCREMENTOS; j++) {
                        if (sincronizado) {
                            contador.incrementarSincronizado();
                        } else {
                            contador.incrementarSemSincronizacao();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    fim.countDown();
                }
            });

            thread.start();
        }

        inicio.countDown();
        fim.await();

        return contador.valor;
    }

    private static class Contador {

        private int valor = 0;

        void incrementarSemSincronizacao() {
            valor++;
        }

        synchronized void incrementarSincronizado() {
            valor++;
        }
    }
}