package br.com.jvmlabs.laboratorioapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/threads")
public class VirtualThreadsController {

    private static final int TOTAL_TAREFAS = 100;
    private static final int ESPERA_MS = 100;

    @GetMapping("/virtual-threads")
    public Map<String, Object> executar() throws Exception {

        long tempoPlatform = executarPlatformThreads();
        long tempoVirtual = executarVirtualThreads();

        Map<String, Object> resultado = new LinkedHashMap<>();

        resultado.put("laboratorio", "Virtual Threads Java 21");
        resultado.put("tarefas", TOTAL_TAREFAS);
        resultado.put("esperaPorTarefaMs", ESPERA_MS);
        resultado.put("platformThreads", 10);
        resultado.put("virtualThreads", TOTAL_TAREFAS);
        resultado.put("tempoPlatformMs", tempoPlatform);
        resultado.put("tempoVirtualMs", tempoVirtual);
        resultado.put("execucao", "Concluída");

        return resultado;
    }

    private long executarPlatformThreads() throws Exception {

        try (ExecutorService executor =
                     Executors.newFixedThreadPool(10)) {

            return executarTarefas(executor, false);
        }
    }

    private long executarVirtualThreads() throws Exception {

        try (ExecutorService executor =
                     Executors.newVirtualThreadPerTaskExecutor()) {

            return executarTarefas(executor, true);
        }
    }

    private long executarTarefas(
            ExecutorService executor,
            boolean virtualEsperado
    ) throws Exception {

        long inicio = System.nanoTime();

        List<Future<Boolean>> resultados = new ArrayList<>();

        for (int i = 0; i < TOTAL_TAREFAS; i++) {

            resultados.add(executor.submit(() -> {

                boolean virtual = Thread.currentThread().isVirtual();

                if (virtual != virtualEsperado) {
                    throw new IllegalStateException(
                            "Tipo de thread inesperado"
                    );
                }

                TimeUnit.MILLISECONDS.sleep(ESPERA_MS);

                return virtual;
            }));
        }

        for (Future<Boolean> resultado : resultados) {
            resultado.get();
        }

        long fim = System.nanoTime();

        return TimeUnit.NANOSECONDS.toMillis(fim - inicio);
    }
}