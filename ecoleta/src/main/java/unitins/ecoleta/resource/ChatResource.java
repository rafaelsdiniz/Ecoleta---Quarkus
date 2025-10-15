package unitins.ecoleta.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import unitins.ecoleta.dto.Request.ChatRequestDTO;
import unitins.ecoleta.dto.Request.MensagemRequestDTO;
import unitins.ecoleta.dto.Response.ChatResponseDTO;
import unitins.ecoleta.dto.Response.MensagemResponseDTO;
import unitins.ecoleta.model.Chat;
import unitins.ecoleta.model.Mensagem;
import unitins.ecoleta.model.PontoColeta;
import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.model.enums.Remetente;
import unitins.ecoleta.repository.ChatRepository;
import unitins.ecoleta.repository.PontoColetaRepository;
import unitins.ecoleta.repository.UsuarioRepository;
import unitins.ecoleta.service.ChatService;
import unitins.ecoleta.service.MensagemService;

@Path("/chats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    ChatService chatService;

    @Inject
    MensagemService mensagemService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PontoColetaRepository pontoRepository;

    @Inject
    ChatRepository chatRepository;

    // Criar chat entre usuário e ponto de coleta (recebe DTO no body)
    @POST
    @Transactional
    public Response criarChat(ChatRequestDTO request) {
        if (request == null || request.usuarioId() == null || request.pontoColetaId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("usuarioId e pontoColetaId são obrigatórios").build();
        }

        Usuario usuario = usuarioRepository.findById(request.usuarioId());
        if (usuario == null)
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Usuário não encontrado").build();

        PontoColeta ponto = pontoRepository.findById(request.pontoColetaId());
        if (ponto == null)
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Ponto de coleta não encontrado").build();

        // verifica chat existente (por causa do unique constraint)
        Chat existente = chatRepository.find("usuario = ?1 and pontoColeta = ?2", usuario, ponto)
                                      .firstResult();
        if (existente != null) {
            return Response.status(Response.Status.CONFLICT)
                           .entity(ChatResponseDTO.fromEntity(existente))
                           .build();
        }

        Chat chat = chatService.criarChat(usuario, ponto);
        return Response.status(Response.Status.CREATED)
                       .entity(ChatResponseDTO.fromEntity(chat))
                       .build();
    }

    // Listar todos os chats de um usuário
    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarChatsUsuario(@PathParam("usuarioId") Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId);
        if (usuario == null)
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Usuário não encontrado").build();

        List<Chat> chats = chatService.listarChatsPorUsuario(usuario);
        List<ChatResponseDTO> dtos = chats.stream()
                                          .map(ChatResponseDTO::fromEntity)
                                          .toList();
        return Response.ok(dtos).build();
    }

    // Endpoint admin: listar todos os chats
    @GET
    @Path("/todos")
    public Response listarTodosChats() {
        List<Chat> chats = chatService.listarTodosChats();
        List<ChatResponseDTO> dtos = chats.stream()
                                          .map(ChatResponseDTO::fromEntity)
                                          .toList();
        return Response.ok(dtos).build();
    }

    // Enviar mensagem em um chat
    @POST
    @Path("/{chatId}/mensagens")
    @Transactional
    public Response enviarMensagem(@PathParam("chatId") Long chatId,
                                   MensagemRequestDTO request) {
        if (request == null || request.conteudo() == null || request.conteudo().isBlank() || request.remetente() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("remetente e conteudo são obrigatórios").build();
        }

        Chat chat = chatRepository.findById(chatId);
        if (chat == null)
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Chat não encontrado").build();

        Remetente remetente = request.remetente();
        Mensagem mensagem = mensagemService.enviarMensagem(chat, remetente, request.conteudo());

        return Response.status(Response.Status.CREATED)
                       .entity(MensagemResponseDTO.fromEntity(mensagem))
                       .build();
    }

    // Listar mensagens de um chat (corrigido: passa o chatId, não o objeto Chat)
    @GET
    @Path("/{chatId}/mensagens")
    @Transactional
    public Response listarMensagens(@PathParam("chatId") Long chatId) {
        Chat chat = chatRepository.findById(chatId);
        if (chat == null)
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Chat não encontrado").build();

        // chama o service com o id
        List<Mensagem> mensagens = mensagemService.listarMensagensPorChat(chatId);
        List<MensagemResponseDTO> dtos = mensagens.stream()
                                                  .map(MensagemResponseDTO::fromEntity)
                                                  .toList();

        return Response.ok(dtos).build();
    }
}
