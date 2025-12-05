package fullclean.domain.repository;

import fullclean.domain.model.Estoque;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    // Busca simples (Leitura rápida)
    Optional<Estoque> findByProdutoId(String produtoId);

    // BUSCA SEGURA (ACID): Trava o registro para venda
    // O @Lock impede que dois pedidos baixem o estoque ao mesmo tempo
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Estoque> findByProdutoIdAndTenantId(String produtoId, String tenantId);
}