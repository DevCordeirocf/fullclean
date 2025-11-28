package fullcleanpackage;

import jakarta.persistence.Entity;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

// 4.2. Usando anotações de chave composta para garantir o isolamento
@Entity
@IdClass(TesteTenant.TesteTenantId.class)
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = String.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TesteTenant {

    @Id
    private String tenantId;

    @Id
    private Long id;

    private String dado;

    // Construtor padrão
    public TesteTenant() {
    }

    // Construtor para facilitar a criação
    public TesteTenant(String tenantId, Long id, String dado) {
        this.tenantId = tenantId;
        this.id = id;
        this.dado = dado;
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

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    // Classe para a chave composta
    public static class TesteTenantId implements Serializable {
        private String tenantId;
        private Long id;

        public TesteTenantId() {
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
            TesteTenantId that = (TesteTenantId) o;
            return Objects.equals(tenantId, that.tenantId) && Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tenantId, id);
        }
    }
}
