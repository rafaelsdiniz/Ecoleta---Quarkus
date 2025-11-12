package unitins.ecoleta.dto.Response;

import unitins.ecoleta.model.Material;

public record MaterialResponseDTO(
    Long id,
    String nome,
    String imagemBase64
) {
    public static MaterialResponseDTO valueOf(Material m){
        return new MaterialResponseDTO(m.getId(), m.getNome(), m.getImagemBase64());
    }
}
