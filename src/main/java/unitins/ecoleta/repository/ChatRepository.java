package unitins.ecoleta.repository;

import java.util.Optional;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unitins.ecoleta.model.Chat;

@ApplicationScoped
public class ChatRepository implements PanacheRepository<Chat> {
    
    /**
     * Busca um chat existente entre um usuário e um ponto de coleta.
     */
    public Optional<Chat> findByUsuarioIdAndPontoColetaId(Long usuarioId, Long pontoColetaId) {
        return find("usuario.id = ?1 and pontoColeta.id = ?2", usuarioId, pontoColetaId)
                .firstResultOptional();
    }
}
