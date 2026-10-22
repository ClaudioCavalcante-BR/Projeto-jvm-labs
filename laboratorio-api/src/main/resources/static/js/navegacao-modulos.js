
/**
 * PROJETO JVM LABS
 * MÓDULO 4 — PORTAL DE NAVEGAÇÃO
 * ETAPA 4.4.3
 *
 * Autor: Claudio Cavalcante de Almeida
 * Ano: 2026
 *
 * Objetivo:
 * Selecionar automaticamente o Módulo 1 — JVM
 * ou o Módulo 2 — Threads por meio da URL.
 *
 * Não modifica app.js ou bytecode.js.
 */

"use strict";

document.addEventListener("DOMContentLoaded", () => {

    // 1. Identificar o módulo informado na URL.

    const parametros = new URLSearchParams(
        window.location.search
    );

    const modulo = parametros.get("modulo");

    // 2. Preservar comportamento original
    // quando nenhum módulo for informado.

    if (!modulo) {
        return;
    }

    // 3. Determinar o botão de navegação.

    const seletores = {
        jvm: 'button[data-view="jvm"]',
        threads: 'button[data-view="threads"]'
    };

    const seletor = seletores[modulo];

    if (!seletor) {
        console.warn(
            "[JVM Labs] Módulo desconhecido:",
            modulo
        );

        return;
    }

    // 4. Identificar o botão já existente
    // na interface original.

    const botaoModulo = document.querySelector(seletor);

    if (!botaoModulo) {
        console.warn(
            "[JVM Labs] Botão não encontrado:",
            seletor
        );

        return;
    }

    // 5. Utilizar a navegação original do app.js.
    //
    // O clique é enviado ao botão existente,
    // preservando seus eventos e suas regras.

    botaoModulo.click();

    console.info(
        "[JVM Labs] Módulo selecionado:",
        modulo
    );

});
