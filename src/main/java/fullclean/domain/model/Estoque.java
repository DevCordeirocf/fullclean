package fullclean.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;

@Entity
@Table(name = "estoque")
// Filtro de Segurança: Garante que Loja A não mexe no estoque da Loja B
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "produto_id", nullable = false)
    private String produtoId; // ID que virá do MongoDB

    @Column(nullable = false)
    private Integer quantidadeDisponivel;

    @Column(nullable = false)
    private Integer quantidadeReservada;

    private LocalDateTime ultimaAtualizacao;

    public Estoque() {}

    public Estoque(String produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidadeDisponivel = quantidade;
        this.quantidadeReservada = 0;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters básicos
    public Long getId() { return id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getTenantId() { return tenantId; }
    public String getProdutoId() { return produtoId; }
    public Integer getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public Integer getQuantidadeReservada() { return quantidadeReservada; }
    public void setQuantidadeReservada(Integer quantidadeReservada) { this.quantidadeReservada = quantidadeReservada; }
    
    @PrePersist @PreUpdate
    public void prePersist() { this.ultimaAtualizacao = LocalDateTime.now(); }
}