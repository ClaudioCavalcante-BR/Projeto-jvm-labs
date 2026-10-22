
package br.com.jvmlabs.laboratorioapi;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.net.URI;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * PROJETO JVM LABS
 * ETAPA 4.8.1 — ABERTURA AUTOMATICA DO PORTAL
 *
 * Autor: Claudio Cavalcante de Almeida
 * Ano: 2026
 */
@Component
public class PortalBrowserLauncher {

    private static final Logger logger =
            LoggerFactory.getLogger(PortalBrowserLauncher.class);

    @Value("${laboratorio.portal.auto-open:false}")
    private boolean autoOpen;

    @Value("${laboratorio.portal.path:/portal.html}")
    private String portalPath;

    @EventListener(ApplicationReadyEvent.class)
    public void abrirPortal(ApplicationReadyEvent event) {

        if (!autoOpen) {
            logger.info(
                    "Portal JVM Labs: abertura manual configurada."
            );
            return;
        }

        String porta = event.getApplicationContext()
                .getEnvironment()
                .getProperty("local.server.port", "8080");

        String caminho = portalPath.startsWith("/")
                ? portalPath
                : "/" + portalPath;

        URI endereco = URI.create(
                "http://localhost:" + porta + caminho
        );

        String sistema = System.getProperty(
                "os.name", ""
        ).toLowerCase(Locale.ROOT);

        try {

            // macOS: utiliza o comando nativo open.
            if (sistema.contains("mac")) {

                new ProcessBuilder(
                        "open",
                        endereco.toString()
                ).start();

                logger.info(
                        "Portal JVM Labs: abertura solicitada no macOS: {}",
                        endereco
                );

                return;
            }

            // Outros sistemas: utiliza java.awt.Desktop.
            if (!GraphicsEnvironment.isHeadless()
                    && Desktop.isDesktopSupported()
                    && Desktop.getDesktop().isSupported(
                    Desktop.Action.BROWSE
            )) {

                Desktop.getDesktop().browse(endereco);

                logger.info(
                        "Portal JVM Labs: abertura solicitada: {}",
                        endereco
                );

                return;
            }

            logger.warn(
                    "Abertura automatica indisponivel. Acesse: {}",
                    endereco
            );

        } catch (Exception e) {

            logger.warn(
                    "Falha ao abrir o Portal automaticamente: {}",
                    e.getMessage()
            );
        }
    }
}
