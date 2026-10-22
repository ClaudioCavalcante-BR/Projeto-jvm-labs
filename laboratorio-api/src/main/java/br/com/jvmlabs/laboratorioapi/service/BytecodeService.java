package br.com.jvmlabs.laboratorioapi.service;

import br.com.jvmlabs.laboratorioapi.bytecode.InstrucaoBytecode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BytecodeService {

    private static final Pattern PADRAO_INSTRUCAO =
            Pattern.compile(
                    "^\\s*(\\d+):\\s+([a-z][a-z0-9_]*)" +
                            "(?:\\s+(.*))?$"
            );

    private static final Pattern PADRAO_CONSTANT_POOL =
            Pattern.compile("#(\\d+)");

    /*
     * Executa javap e retorna a representação textual
     * do Bytecode presente no arquivo .class.
     */
    public String consultarInstrucoes(Path arquivoClass)
            throws IOException, InterruptedException {

        return executarJavap(arquivoClass, "-c", "-p");
    }

    /*
     * Executa javap -v para permitir a inspeção
     * das flags dos métodos, incluindo ACC_SYNCHRONIZED.
     */
    public String consultarDetalhes(Path arquivoClass)
            throws IOException, InterruptedException {

        return executarJavap(arquivoClass, "-v", "-p");
    }

    private String executarJavap(
            Path arquivoClass,
            String... argumentos
    ) throws IOException, InterruptedException {

        Path arquivo = arquivoClass
                .toAbsolutePath()
                .normalize();

        if (!Files.isRegularFile(arquivo)
                || !arquivo.toString().endsWith(".class")) {

            throw new IllegalArgumentException(
                    "Arquivo .class inválido ou inexistente"
            );
        }

        String javaHome = System.getProperty("java.home");

        Path javap = Path.of(
                javaHome,
                "bin",
                "javap"
        );

        if (!Files.isExecutable(javap)) {

            throw new IOException(
                    "Executável javap não encontrado no JDK"
            );
        }

        List<String> comando = new ArrayList<>();

        comando.add(javap.toString());

        comando.addAll(List.of(argumentos));

        comando.add(arquivo.toString());

        ProcessBuilder processo = new ProcessBuilder(comando);

        processo.redirectErrorStream(true);

        Process execucao = processo.start();

        boolean finalizou = execucao.waitFor(
                10,
                TimeUnit.SECONDS
        );

        if (!finalizou) {

            execucao.destroyForcibly();

            throw new IOException(
                    "Tempo limite excedido na execução do javap"
            );
        }

        String resultado = new String(
                execucao.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        if (execucao.exitValue() != 0) {

            throw new IOException(
                    "Falha ao interpretar o Bytecode: " + resultado
            );
        }

        return resultado;
    }

    /*
     * Mantém a compatibilidade com os laboratórios
     * anteriores, que analisam o método main().
     */
    public List<InstrucaoBytecode> extrairInstrucoes(
            Path arquivoClass
    ) throws IOException, InterruptedException {

        return extrairInstrucoes(
                arquivoClass,
                "main"
        );
    }

    /*
     * Novo recurso da Etapa 3.17.3:
     * permite selecionar o método que será analisado.
     *
     * Exemplos:
     * main
     * lambda$main$0
     * incrementar
     */
    public List<InstrucaoBytecode> extrairInstrucoes(
            Path arquivoClass,
            String nomeMetodo
    ) throws IOException, InterruptedException {

        if (nomeMetodo == null || nomeMetodo.isBlank()) {

            throw new IllegalArgumentException(
                    "Nome do método não informado"
            );
        }

        String saida = consultarInstrucoes(arquivoClass);

        List<InstrucaoBytecode> instrucoes = new ArrayList<>();

        boolean metodoEncontrado = false;
        boolean dentroDoCodigo = false;

        for (String linha : saida.split("\\R")) {

            String texto = linha.trim();

            if (!metodoEncontrado) {

                if (identificarMetodo(texto, nomeMetodo)) {

                    metodoEncontrado = true;
                }

                continue;
            }

            if (!dentroDoCodigo) {

                if (texto.equals("Code:")) {

                    dentroDoCodigo = true;
                }

                continue;
            }

            Matcher matcher =
                    PADRAO_INSTRUCAO.matcher(linha);

            if (!matcher.matches()) {

                if (texto.isEmpty()) {
                    continue;
                }

                break;
            }

            int offset =
                    Integer.parseInt(matcher.group(1));

            String opcode = matcher.group(2);

            String operandos = matcher.group(3) == null
                    ? ""
                    : matcher.group(3).trim();

            List<Integer> bytes = obterBytes(
                    offset,
                    opcode,
                    operandos
            );

            String hexadecimal =
                    converterHexadecimal(bytes);

            String binario =
                    converterBinario(bytes);

            instrucoes.add(
                    new InstrucaoBytecode(
                            offset,
                            opcode,
                            operandos,
                            hexadecimal,
                            binario
                    )
            );
        }

        if (!metodoEncontrado) {

            throw new IllegalArgumentException(
                    "Método não encontrado no Bytecode: "
                            + nomeMetodo
            );
        }

        if (instrucoes.isEmpty()) {

            throw new IllegalArgumentException(
                    "Nenhuma instrução encontrada para o método: "
                            + nomeMetodo
            );
        }

        return instrucoes;
    }

    /*
     * Identifica a assinatura do método no javap.
     * Evita confundir chamadas a métodos com
     * as respectivas declarações.
     */
    private boolean identificarMetodo(
            String texto,
            String nomeMetodo
    ) {

        if (!texto.endsWith(";")) {
            return false;
        }

        int abertura = texto.indexOf('(');

        if (abertura < 0) {
            return false;
        }

        String assinatura = texto.substring(
                0,
                abertura
        ).trim();

        return assinatura.endsWith(
                " " + nomeMetodo
        );
    }

    /*
     * Identifica se o método possui a flag
     * ACC_SYNCHRONIZED no arquivo .class.
     */
    public boolean metodoSincronizado(
            Path arquivoClass,
            String nomeMetodo
    ) throws IOException, InterruptedException {

        String saida = consultarDetalhes(arquivoClass);

        boolean metodoEncontrado = false;

        for (String linha : saida.split("\\R")) {

            String texto = linha.trim();

            if (!metodoEncontrado) {

                if (identificarMetodo(texto, nomeMetodo)) {

                    metodoEncontrado = true;
                }

                continue;
            }

            if (texto.startsWith("flags:")) {

                return texto.contains(
                        "ACC_SYNCHRONIZED"
                );
            }

            if (texto.endsWith(";")
                    && texto.contains("(")) {

                break;
            }
        }

        if (!metodoEncontrado) {

            throw new IllegalArgumentException(
                    "Método não encontrado: " + nomeMetodo
            );
        }

        throw new IllegalArgumentException(
                "Flags não encontradas para o método: "
                        + nomeMetodo
        );
    }

    /*
     * Converte as instruções reconhecidas nos bytes
     * correspondentes ao formato da JVM.
     */
    private List<Integer> obterBytes(
            int offset,
            String opcode,
            String operandos
    ) {

        int codigo = switch (opcode) {

            case "iconst_m1" -> 0x02;
            case "iconst_0" -> 0x03;
            case "iconst_1" -> 0x04;
            case "iconst_2" -> 0x05;
            case "iconst_3" -> 0x06;
            case "iconst_4" -> 0x07;
            case "iconst_5" -> 0x08;

            case "bipush" -> 0x10;
            case "sipush" -> 0x11;
            case "ldc" -> 0x12;
            case "ldc_w" -> 0x13;

            case "iload" -> 0x15;
            case "iload_0" -> 0x1A;
            case "iload_1" -> 0x1B;
            case "iload_2" -> 0x1C;
            case "iload_3" -> 0x1D;

            case "istore" -> 0x36;
            case "istore_0" -> 0x3B;
            case "istore_1" -> 0x3C;
            case "istore_2" -> 0x3D;
            case "istore_3" -> 0x3E;

            case "iadd" -> 0x60;
            case "isub" -> 0x64;
            case "imul" -> 0x68;

            case "iinc" -> 0x84;

            case "if_icmple" -> 0xA4;
            case "if_icmpge" -> 0xA2;
            case "goto" -> 0xA7;

            case "getstatic" -> 0xB2;
            case "putstatic" -> 0xB3;
            case "invokevirtual" -> 0xB6;
            case "invokestatic" -> 0xB8;

            case "return" -> 0xB1;

            case "aload" -> 0x19;
            case "aload_0" -> 0x2A;
            case "aload_1" -> 0x2B;
            case "aload_2" -> 0x2C;
            case "aload_3" -> 0x2D;

            case "astore" -> 0x3A;
            case "astore_0" -> 0x4B;
            case "astore_1" -> 0x4C;
            case "astore_2" -> 0x4D;
            case "astore_3" -> 0x4E;

            case "dup" -> 0x59;

            case "new" -> 0xBB;
            case "getfield" -> 0xB4;
            case "putfield" -> 0xB5;
            case "invokespecial" -> 0xB7;
            case "invokedynamic" -> 0xBA;

            case "athrow" -> 0xBF;
            case "monitorenter" -> 0xC2;
            case "monitorexit" -> 0xC3;

            default -> -1;
        };

        if (codigo < 0) {

            return List.of();
        }

        if (opcode.equals("iload")
                || opcode.equals("istore")
                || opcode.equals("aload")
                || opcode.equals("astore")) {

            int indice = extrairIndiceLocal(operandos);

            return List.of(codigo, indice);
        }

        if (opcode.equals("bipush")) {

            int valor = extrairInteiro(operandos);

            if (valor < -128 || valor > 127) {

                throw new IllegalArgumentException(
                        "Operando bipush fora do intervalo: "
                                + valor
                );
            }

            return List.of(
                    codigo,
                    valor & 0xFF
            );
        }

        if (opcode.equals("sipush")) {

            int valor = extrairInteiro(operandos);

            if (valor < Short.MIN_VALUE
                    || valor > Short.MAX_VALUE) {

                throw new IllegalArgumentException(
                        "Operando sipush inválido: " + valor
                );
            }

            return List.of(
                    codigo,
                    (valor >>> 8) & 0xFF,
                    valor & 0xFF
            );
        }

        if (opcode.equals("ldc")) {

            int indice = extrairIndiceConstantPool(operandos);

            if (indice > 255) {

                throw new IllegalArgumentException(
                        "Índice ldc fora do intervalo: "
                                + indice
                );
            }

            return List.of(codigo, indice);
        }

        if (opcode.equals("ldc_w")) {

            int indice = extrairIndiceConstantPool(operandos);

            return List.of(
                    codigo,
                    (indice >>> 8) & 0xFF,
                    indice & 0xFF
            );
        }

        if (opcode.equals("iinc")) {

            String parteNumerica =
                    operandos.split("//", 2)[0].trim();

            String[] partes =
                    parteNumerica.split("\\s*,\\s*");

            if (partes.length != 2) {

                throw new IllegalArgumentException(
                        "Operandos inválidos para iinc: "
                                + operandos
                );
            }

            int indice =
                    Integer.parseInt(partes[0]);

            int incremento =
                    Integer.parseInt(partes[1]);

            if (indice < 0 || indice > 255
                    || incremento < -128
                    || incremento > 127) {

                throw new IllegalArgumentException(
                        "Operandos iinc fora do intervalo"
                );
            }

            return List.of(
                    codigo,
                    indice,
                    incremento & 0xFF
            );
        }

        if (opcode.equals("if_icmple")
                || opcode.equals("if_icmpge")
                || opcode.equals("goto")) {

            int destino = extrairInteiro(operandos);

            int deslocamento = destino - offset;

            if (deslocamento < Short.MIN_VALUE
                    || deslocamento > Short.MAX_VALUE) {

                throw new IllegalArgumentException(
                        "Deslocamento de desvio inválido: "
                                + deslocamento
                );
            }

            return List.of(
                    codigo,
                    (deslocamento >>> 8) & 0xFF,
                    deslocamento & 0xFF
            );
        }

        if (opcode.equals("getstatic")
                || opcode.equals("putstatic")
                || opcode.equals("getfield")
                || opcode.equals("putfield")
                || opcode.equals("new")
                || opcode.equals("invokespecial")
                || opcode.equals("invokevirtual")
                || opcode.equals("invokestatic")) {

            int indice =
                    extrairIndiceConstantPool(operandos);

            return List.of(
                    codigo,
                    (indice >>> 8) & 0xFF,
                    indice & 0xFF
            );

        }

        if (opcode.equals("invokedynamic")) {

            int indice =
                    extrairIndiceConstantPool(operandos);

            return List.of(
                    codigo,
                    (indice >>> 8) & 0xFF,
                    indice & 0xFF,
                    0x00,
                    0x00
            );
        }

        return List.of(codigo);
    }

    private int extrairInteiro(String operandos) {

        String texto = operandos.trim();

        if (texto.isEmpty()) {

            throw new IllegalArgumentException(
                    "Operando numérico não informado"
            );
        }

        String primeiro =
                texto.split("\\s+")[0];

        return Integer.parseInt(primeiro);
    }

    private int extrairIndiceLocal(String operandos) {

        int indice = extrairInteiro(operandos);

        if (indice < 0 || indice > 255) {

            throw new IllegalArgumentException(
                    "Índice de variável local inválido: "
                            + indice
            );
        }

        return indice;
    }

    private int extrairIndiceConstantPool(
            String operandos
    ) {

        Matcher matcher =
                PADRAO_CONSTANT_POOL.matcher(operandos);

        if (!matcher.find()) {

            throw new IllegalArgumentException(
                    "Índice do constant pool não encontrado: "
                            + operandos
            );
        }

        int indice =
                Integer.parseInt(matcher.group(1));

        if (indice < 1 || indice > 65535) {

            throw new IllegalArgumentException(
                    "Índice do constant pool inválido: "
                            + indice
            );
        }

        return indice;
    }

    private String converterHexadecimal(
            List<Integer> bytes
    ) {

        if (bytes.isEmpty()) {
            return "";
        }

        List<String> valores = new ArrayList<>();

        for (int valor : bytes) {

            valores.add(
                    String.format("%02X", valor)
            );
        }

        return String.join(" ", valores);
    }

    private String converterBinario(
            List<Integer> bytes
    ) {

        if (bytes.isEmpty()) {
            return "";
        }

        List<String> valores = new ArrayList<>();

        for (int valor : bytes) {

            String binario = String.format(
                    "%8s",
                    Integer.toBinaryString(valor)
            ).replace(' ', '0');

            valores.add(binario);
        }

        return String.join(" ", valores);
    }
}
