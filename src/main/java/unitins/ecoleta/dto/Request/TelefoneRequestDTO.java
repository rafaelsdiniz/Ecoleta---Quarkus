package unitins.ecoleta.dto.Request;

import unitins.ecoleta.model.Telefone;

public record TelefoneRequestDTO(
    String numero,
    String codigoArea  
) 

{
    public static Telefone toEntity(TelefoneRequestDTO dto){
        Telefone t = new Telefone();
        t.setNumero(dto.numero());
        t.setCodigoArea(dto.codigoArea());

        return t;
    }
}
