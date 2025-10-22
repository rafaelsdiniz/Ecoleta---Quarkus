package unitins.ecoleta.service;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import unitins.ecoleta.model.Chat;
import unitins.ecoleta.model.PontoColeta;
import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.repository.ChatRepository;

@ApplicationScoped
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    public Chat criarChat(Usuario usuario, PontoColeta pontoColeta) {
        // Evita duplicação: se já existir, retorna o existente
        Optional<Chat> existente = chatRepository.findByUsuarioIdAndPontoColetaId(usuario.getId(), pontoColeta.getId());
        if (existente.isPresent()) {
            return existente.get();
        }

        Chat chat = new Chat();
        chat.setUsuario(usuario);
        chat.setPontoColeta(pontoColeta);
        chatRepository.persist(chat);
        return chat;
    }

    public List<Chat> listarChatsPorUsuario(Usuario usuario) {
        return chatRepository.list("usuario", usuario);
    }

    public List<Chat> listarChatsPorPonto(PontoColeta ponto) {
        return chatRepository.list("pontoColeta", ponto);
    }

    public List<Chat> listarTodosChats() {
        return chatRepository.listAll();
    }

    /**
     * Busca um chat entre usuário e ponto de coleta.
     */
    public Optional<Chat> buscarChatPorUsuarios(Long usuarioId, Long pontoColetaId) {
        return chatRepository.findByUsuarioIdAndPontoColetaId(usuarioId, pontoColetaId);
    }
}
