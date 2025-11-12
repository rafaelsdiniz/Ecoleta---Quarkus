package unitins.ecoleta.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unitins.ecoleta.model.Mensagem;

@ApplicationScoped
public class MensagemRepository implements PanacheRepository<Mensagem> {

    public List<Mensagem> findByChat(Long chatId) {
        return find("chat.id = ?1 order by dataEnvio asc", chatId).list();
    }
}
