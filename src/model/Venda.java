package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa a entidade Venda.
 * <p>
 * Encapsula os dados de uma transação de venda, incluindo a data, o valor total,
 * a forma de pagamento e uma lista de todos os {@link ItemVenda} que a compõem.
 *
 * @author kenyamashida
 */
public class Venda {
    private int idVenda;
    private Date dataVenda;
    private double totalVenda;
    private String formaPagamento;
    private List<ItemVenda> itensVenda;

    /**
     * Construtor padrão.
     * Inicializa a lista de itens e define a data da venda para o momento atual.
     */
    public Venda() {
        this.itensVenda = new ArrayList<>();
        this.dataVenda = new Date();
    }

    // Getters e Setters
    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }
    public Date getDataVenda() { return dataVenda; }
    public void setDataVenda(Date dataVenda) { this.dataVenda = dataVenda; }
    public double getTotalVenda() { return totalVenda; }
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    public List<ItemVenda> getItensVenda() { return itensVenda; }

    /**
     * Adiciona um item à lista da venda e recalcula o valor total.
     * @param item O {@link ItemVenda} a ser adicionado.
     */
    public void adicionarItem(ItemVenda item) {
        this.itensVenda.add(item);
        calcularTotal();
    }

    /**
     * Calcula o valor total da venda somando os subtotais de todos os itens.
     */
    public void calcularTotal() {
        this.totalVenda = 0;
        for (ItemVenda item : itensVenda) {
            this.totalVenda += item.calcularSubtotal();
        }
    }
} // A CHAVE DE FECHAMENTO FINAL DA CLASSE DEVE ESTAR AQUI.
