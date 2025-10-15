package unitins.ecoleta.dto.Response;

import java.time.LocalDateTime;

import unitins.ecoleta.model.Mensagem;

public record MensagemResponseDTO(
    Long id,
    String conteudo,
    String remetente,
    LocalDateTime dataEnvio
) {
    public static MensagemResponseDTO fromEntity(Mensagem mensagem) {
        return new MensagemResponseDTO(
            mensagem.getId(),
            mensagem.getConteudo(),
            mensagem.getRemetente().name(),
            mensagem.getDataEnvio()
        );
    }
}
