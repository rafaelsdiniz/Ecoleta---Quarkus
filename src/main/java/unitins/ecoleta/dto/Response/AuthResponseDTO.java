package unitins.ecoleta.dto.Response;

public record AuthResponseDTO(
    String token,
    UsuarioResponseDTO usuario
) {}
