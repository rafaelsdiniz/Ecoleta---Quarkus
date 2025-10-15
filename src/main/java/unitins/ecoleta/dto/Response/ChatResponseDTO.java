package unitins.ecoleta.dto.Response;

import java.time.LocalDateTime;

import unitins.ecoleta.model.Chat;

public record ChatResponseDTO(
    Long id,
    Long usuarioId,
    String nomeUsuario,
    Long pontoColetaId,
    String nomePontoColeta,
    LocalDateTime dataCriacao
) {
    public static ChatResponseDTO fromEntity(Chat chat) {
        return new ChatResponseDTO(
            chat.getId(),
            chat.getUsuario().getId(),
            chat.getUsuario().getNome(),
            chat.getPontoColeta().getId(),
            chat.getPontoColeta().getNome(),
            chat.getDataCriacao()
        );
    }
}
