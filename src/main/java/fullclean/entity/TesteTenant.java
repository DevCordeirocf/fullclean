package fullclean.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * TesteTenant - Entidade JPA para provar o Isolamento de Dados.
 * 
 * CRÍTICO: Esta entidade implementa o padrão Row Filtering (Filtro de Linha)
 * usando as anotações do Hibernate.
 */
@Entity
@Table(name = "teste_tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
// 1. Define o Filtro que será aplicado
@FilterDef(
    name = "tenantFilter",
    parameters = @ParamDef(name = "currentTenantId", type = String.class)
)
// 2. Aplica o Filtro na Entidade
@Filter(
    name = "tenantFilter",
    condition = "tenant_id = :currentTenantId" // A condição SQL que garante o isolamento
)
public class TesteTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String tenantId;
}
