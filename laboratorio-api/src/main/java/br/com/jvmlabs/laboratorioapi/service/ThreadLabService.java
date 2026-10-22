package br.com.jvmlabs.laboratorioapi.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ThreadLabService {

    public Map<String, Object> executarThread() throws InterruptedException {

        Map<String, Object> resultado = new LinkedHashMap<>();

        Thread thread = new Thread(() -> {
            System.out.println(
                    "Executando thread: " + Thread.currentThread().getName()
            );
        }, "thread-laboratorio-01");

        Thread.State estadoAntes = thread.getState();

        thread.start();
        thread.join();

        resultado.put("nome", thread.getName());
        resultado.put("id", thread.threadId());
        resultado.put("estadoAntes", estadoAntes.name());
        resultado.put("estadoDepois", thread.getState().name());
        resultado.put("virtual", thread.isVirtual());
        resultado.put("execucao", "Concluída");

        return resultado;
    }
}