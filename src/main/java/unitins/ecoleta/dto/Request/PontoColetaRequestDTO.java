package unitins.ecoleta.dto.Request;

import java.util.List;

public record PontoColetaRequestDTO(
    String nome,
    String descricao,
    String email, 
    String telefone,
    String endereco,
    Double latitude,
    Double longitude,
    List<TelefoneRequestDTO> listaTelefone,
    List<Long> listaIdMaterial,
    List<DiaFuncionamentoRequestDTO> listaDiaFuncionamento
)
{
    
}
