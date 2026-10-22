
package br.com.jvmlabs.laboratorioapi.bytecode;

/**
 * Representa uma instrução real extraída do Bytecode JVM.
 *
 * @param offset posição da instrução dentro do método
 * @param opcode nome da instrução JVM
 * @param operandos operandos textuais da instrução
 * @param hexadecimal bytes reais da instrução em hexadecimal
 * @param binario bytes reais da instrução em binário
 */
public record InstrucaoBytecode(
        int offset,
        String opcode,
        String operandos,
        String hexadecimal,
        String binario
) {
}
