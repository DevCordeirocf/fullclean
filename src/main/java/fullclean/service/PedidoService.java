package fullclean.service;

import fullclean.security.TenantContext;
import fullclean.domain.model.ItemPedido;
import fullclean.domain.model.Pedido;
import fullclean.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService; // Injeção do Serviço da Fase Anterior

    public PedidoService(PedidoRepository pedidoRepository, EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
    }

    @Transactional // CRÍTICO: Se algo falhar aqui, o estoque volta (Rollback)
    public Pedido criarPedido(List<DadosItemPedido> itensDTO) {
        Pedido pedido = new Pedido();
        pedido.setTenantId(TenantContext.getTenantId()); // Força o Tenant ID

        for (DadosItemPedido itemData : itensDTO) {
            // 1. CHAMA O ESTOQUE COM LOCK PESSIMISTA
            // Se não tiver estoque, isso lança uma Exception e cancela tudo.
            estoqueService.reservarEstoque(itemData.produtoId(), itemData.quantidade());

            // 2. Cria o item do pedido (Preço viria do Banco, aqui fixo para exemplo)
            ItemPedido item = new ItemPedido(
                itemData.produtoId(),
                itemData.quantidade(),
                new BigDecimal("100.00") 
            );
            
            pedido.adicionarItem(item);
        }

        return pedidoRepository.save(pedido);
    }

    // Record auxiliar (DTO) para receber dados simples
    public record DadosItemPedido(String produtoId, Integer quantidade) {}
}