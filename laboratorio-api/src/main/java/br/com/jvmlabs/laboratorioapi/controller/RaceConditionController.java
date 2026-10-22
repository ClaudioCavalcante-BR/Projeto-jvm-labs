package br.com.jvmlabs.laboratorioapi.controller;

import br.com.jvmlabs.laboratorioapi.service.RaceConditionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/threads")
public class RaceConditionController {

    private final RaceConditionService service;

    public RaceConditionController(RaceConditionService service) {
        this.service = service;
    }

    @GetMapping("/race-condition")
    public Map<String, Object> executar()
            throws InterruptedException {

        return service.executarExperimento();
    }
}