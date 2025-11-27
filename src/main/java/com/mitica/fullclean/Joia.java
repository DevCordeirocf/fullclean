package com.mitica.fullclean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(Joia.JoiaId.class)
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Joia {

    @Id
    private String tenantId;

    @Id
    private Long id;

    private String nome;
    private String sku;
    private Double preco;

    // Construtor padrão
    public Joia() {
    }

    // Construtor para facilitar a criação
    public Joia(String tenantId, Long id, String nome, String sku, Double preco) {
        this.tenantId = tenantId;
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.preco = preco;
    }

    // Getters e Setters
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    // Classe para a chave composta
    public static class JoiaId implements Serializable {
        private String tenantId;
        private Long id;

        public JoiaId() {
        }

        // Getters, Setters, equals e hashCode
        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JoiaId joiaId = (JoiaId) o;
            return Objects.equals(tenantId, joiaId.tenantId) && Objects.equals(id, joiaId.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tenantId, id);
        }
    }
}
