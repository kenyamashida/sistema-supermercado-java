package model;

public class ItemVenda {
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPrecoVenda();
    }

    // Getters
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public double getPrecoUnitario() { return precoUnitario; }

    /**
     * Calcula o subtotal para este item.
     * @return O valor do subtotal (quantidade * preço).
     */
    public double calcularSubtotal() {
        return this.quantidade * this.precoUnitario;
    }
}