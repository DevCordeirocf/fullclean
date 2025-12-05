package fullclean.service;

import fullclean.security.TenantContext;
import fullclean.domain.model.Estoque;
import fullclean.domain.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fullclean.exception.ResourceNotFoundException;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;

    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    // Chamado pelo Admin para cadastrar estoque inicial
    @Transactional
    public Estoque inicializarEstoque(String produtoId, Integer quantidade) {
        Estoque estoque = new Estoque(produtoId, quantidade);
        estoque.setTenantId(TenantContext.getTenantId()); // Força o Tenant Context
        return repository.save(estoque);
    }

    // O MÉTODO CRÍTICO: Chamado pelo PedidoService
    @Transactional
    public void reservarEstoque(String produtoId, Integer quantidadeSolicitada) {
        String tenantId = TenantContext.getTenantId();

        // 1. Busca e TRAVA a linha no banco (Lock Pessimista)
        Estoque estoque = repository.findByProdutoIdAndTenantId(produtoId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado para produto: " + produtoId));

        // 2. Verifica se tem saldo real (Disponivel - Já Reservado)
        int saldoReal = estoque.getQuantidadeDisponivel() - estoque.getQuantidadeReservada();

        if (saldoReal < quantidadeSolicitada) {
            throw new RuntimeException("Estoque insuficiente. Saldo atual: " + saldoReal);
        }

        // 3. Incrementa a reserva
        estoque.setQuantidadeReservada(estoque.getQuantidadeReservada() + quantidadeSolicitada);
        
        // 4. Salva (A transação libera o Lock automaticamente aqui)
        repository.save(estoque);
    }

    public Estoque consultarEstoque(String produtoId) {
        return repository.findByProdutoId(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque não encontrado"));
    }
}