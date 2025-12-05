package fullclean.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
// Segurança: O Filtro garante que a Loja A não vê pedidos da Loja B
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private LocalDateTime dataPedido;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private BigDecimal valorTotal;

    // Cascade: Ao salvar o Pedido, salva os Itens automaticamente
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.valorTotal = BigDecimal.ZERO;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        // Recalcula total simples
        this.valorTotal = this.valorTotal.add(item.getSubtotal());
    }

    // Getters e Setters
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getId() { return id; }
    public List<ItemPedido> getItens() { return itens; }
    public void setStatus(StatusPedido status) { this.status = status; }
}