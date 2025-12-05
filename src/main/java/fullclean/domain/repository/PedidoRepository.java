package fullclean.domain.repository;

import fullclean.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Graças ao @Filter, o findAll() já trará apenas os pedidos da loja certa.
}