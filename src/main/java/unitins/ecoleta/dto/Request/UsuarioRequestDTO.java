package unitins.ecoleta.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import unitins.ecoleta.model.enums.TipoUsuario;

public record UsuarioRequestDTO(

    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 5, message = "A senha deve ter pelo menos 5 caracteres.")
    String senha,

    @NotBlank(message = "O telefone é obrigatório.")
    String telefone,

    TipoUsuario tipoUsuario
) {}
