package unitins.ecoleta.dto.Request;

import unitins.ecoleta.model.enums.Remetente;

public record MensagemRequestDTO(
    Remetente remetente,
    String conteudo
) {}
