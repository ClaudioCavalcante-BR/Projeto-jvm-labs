
public class Pessoa {

    private String nome;
    private int idade;

    // Construtor -- O construtor utiliza putfield para inicializar os atributos nome e idade.
    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    // Método de instância
    public void apresentar() {
        System.out.println(
                "Nome: " + nome + " | Idade: " + idade
        );
    }
    // O método main() cria uma instância de Pessoa, armazena sua referência em uma variável local e chama apresentar().
    public static void main(String[] args) {

        // Criação do objeto
        Pessoa pessoa = new Pessoa("Claudio", 53);

        // Chamada de método de instância
        pessoa.apresentar();
    }
}
