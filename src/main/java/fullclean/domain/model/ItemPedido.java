package fullclean.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private String produtoId; 

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private String nomeProdutoSnapshot; 

    @Column(nullable = true)
    private String urlImagemSnapshot;

    // Construtores
    public ItemPedido() {}

    public ItemPedido(String produtoId, Integer quantidade, BigDecimal precoUnitario) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
    public String getProdutoId() { return produtoId; }
    public Integer getQuantidade() { return quantidade; }
}