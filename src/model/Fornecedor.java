package model;

public class Fornecedor {
    private int id;
    private String cnpj;
    private String nome;
    private String endereco;
    private String contato;

    // --- Getters e Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // Outros getters e setters podem ser adicionados conforme a necessidade.
}
