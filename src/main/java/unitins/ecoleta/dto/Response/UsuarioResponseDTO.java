package unitins.ecoleta.dto.Response;

import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.model.enums.TipoUsuario;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    TipoUsuario tipoUsuario
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getTipoUsuario()
        );
    }
}
