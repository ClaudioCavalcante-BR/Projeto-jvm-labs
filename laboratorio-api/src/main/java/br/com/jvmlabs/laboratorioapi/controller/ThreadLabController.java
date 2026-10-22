package br.com.jvmlabs.laboratorioapi.controller;

import br.com.jvmlabs.laboratorioapi.service.ThreadLabService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/threads")
public class ThreadLabController {

    private final ThreadLabService service;

    public ThreadLabController(ThreadLabService service) {
        this.service = service;
    }

    @GetMapping("/executar")
    public Map<String, Object> executar() throws InterruptedException {
        return service.executarThread();
    }
}