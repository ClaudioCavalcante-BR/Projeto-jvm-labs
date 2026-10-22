
/**
 * PROJETO JVM LABS
 * MÓDULO 4 — PORTAL DE NAVEGAÇÃO
 * ETAPA 4.3
 *
 * Autor: Claudio Cavalcante de Almeida
 * Ano: 2026
 *
 * Arquivo: portal.js
 */

"use strict";

document.addEventListener("DOMContentLoaded", () => {

    const moduloJVM = document.querySelector(
        ".module-card:nth-child(1) .module-link"
    );

    const moduloThreads = document.querySelector(
        ".module-card:nth-child(2) .module-link"
    );

    const moduloBytecode = document.querySelector(
        ".module-card:nth-child(3) .module-link"
    );

    // Módulo 1 — JVM
    if (moduloJVM) {
        moduloJVM.href = "/?modulo=jvm";
    }

    // Módulo 2 — Threads
    if (moduloThreads) {
        moduloThreads.href = "/?modulo=threads";
    }

    // Módulo 3 — Bytecode Explorer
    if (moduloBytecode) {
        moduloBytecode.href = "/bytecode.html";
    }

    console.info(
        "[JVM Labs] Portal de navegação inicializado."
    );

});
