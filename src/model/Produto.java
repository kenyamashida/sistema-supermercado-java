package model;

/**
 * Representa a entidade Produto.
 * <p>
 * Esta classe é um "Plain Old Java Object" (POJO) que encapsula todos os
 * atributos de um produto, como código de barras, nome, preços e estoque.
 * Ela também contém regras de negócio simples relacionadas ao produto.
 *
 * @author kenyamashida
 */
public class Produto {
    private String codigoBarras;
    private String nome;
    private double precoVenda;
    private double precoCompra;
    private String categoria;
    private int quantidadeEstoque;
    private int estoqueMinimo;
    private Fornecedor fornecedor;

    // Getters e Setters (métodos de acesso para os atributos privados)

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
    public double getPrecoCompra() { return precoCompra; }
    public void setPrecoCompra(double precoCompra) { this.precoCompra = precoCompra; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public int getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    /**
     * Atualiza a quantidade em estoque, adicionando ou subtraindo um valor.
     * @param quantidade A quantidade a ser adicionada (positiva) ou removida (negativa).
     */
    public void atualizarEstoque(int quantidade) {
        this.quantidadeEstoque += quantidade;
    }

    /**
     * Verifica se o estoque do produto atingiu ou está abaixo do nível mínimo.
     * @return {@code true} se o estoque estiver baixo, {@code false} caso contrário.
     */
    public boolean verificarEstoqueBaixo() {
        return this.quantidadeEstoque <= this.estoqueMinimo;
    }
}