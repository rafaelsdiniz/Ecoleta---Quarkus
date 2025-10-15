package unitins.ecoleta.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import unitins.ecoleta.model.Chat;
import unitins.ecoleta.model.Mensagem;
import unitins.ecoleta.model.enums.Remetente;
import unitins.ecoleta.repository.MensagemRepository;

@ApplicationScoped
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    public MensagemService(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    @Transactional
    public Mensagem enviarMensagem(Chat chat, Remetente remetente, String conteudo) {
        Mensagem mensagem = new Mensagem();
        mensagem.setChat(chat);
        mensagem.setConteudo(conteudo);
        mensagem.setRemetente(remetente);
        mensagemRepository.persistAndFlush(mensagem);
        // também opcional: chat.addMensagem(mensagem); e persist chat se necessário
        return mensagem;
    }

    @Transactional
    public List<Mensagem> listarMensagensPorChat(Long chatId) {
        return mensagemRepository.list("chat.id", chatId);
    }

    public List<Mensagem> listarTodasMensagens() {
        return mensagemRepository.listAll();
    }
}
