package unitins.ecoleta.dto.Response;

import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.model.enums.TipoUsuario;

public record LoginResponseDTO(
    String token,
    Long id,
    String nome,
    String email,
    String telefone,
    TipoUsuario tipoUsuario
) {
    public static LoginResponseDTO valueOf(Usuario usuario, String token) {
        return new LoginResponseDTO(
            token,
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getTipoUsuario()
        );
    }
}
