package unitins.ecoleta.dto.Response;

import unitins.ecoleta.model.Telefone;

public record TelefoneResponseDTO(
    String codigoArea,
    String numero
) 

{
    public static TelefoneResponseDTO valueOf(Telefone t){
        return new TelefoneResponseDTO(t.getCodigoArea(), t.getNumero());
    }
}
