package unitins.ecoleta.dto.Response;

import java.util.List;

import unitins.ecoleta.model.PontoColeta;

public record PontoColetaResponseDTO(
    Long id,
    String nome,
    String descricao,
    String email, 
    String endereco,
    String imagemBase64,
    Double latitude,
    Double longitude,
    Double distancia,
    String latLng,
    List<TelefoneResponseDTO> listaTelefone,
    List<MaterialResponseDTO> listaMaterial,
    List<DiaFuncionamentoResponseDTO> listaDiaFuncionamento
) {
    public static PontoColetaResponseDTO valueOf(PontoColeta p){
        return new PontoColetaResponseDTO(
            p.getId(),
            p.getNome(),
            p.getDescricao(),
            p.getEmail(),
            p.getEndereco(),
            p.getImagemBase64(),
            p.getLatitude(),
            p.getLongitude(),
            null,
            String.format("LatLng(lat: %s, lng: %s)", p.getLatitude().toString(), p.getLongitude().toString()),
            p.getListaTelefone().stream().map(TelefoneResponseDTO::valueOf).toList(),
            p.getListaMaterial() != null ? p.getListaMaterial().stream().map(MaterialResponseDTO::valueOf).toList() : null,
            p.getListaDiaFuncionamento() != null ? p.getListaDiaFuncionamento().stream().map(DiaFuncionamentoResponseDTO::valueOf).toList() : null
        );
    }

    public static PontoColetaResponseDTO valueOf(PontoColeta p, Double distancia){
        return new PontoColetaResponseDTO(
            p.getId(),
            p.getNome(),
            p.getDescricao(),
            p.getEmail(),
            p.getEndereco(),
            p.getImagemBase64(),
            p.getLatitude(),
            p.getLongitude(),
            distancia,
            String.format("LatLng(lat: %s, lng: %s)", p.getLatitude().toString(), p.getLongitude().toString()),
            p.getListaTelefone() != null ? p.getListaTelefone().stream().map(TelefoneResponseDTO::valueOf).toList() : null,
            p.getListaMaterial() != null ? p.getListaMaterial().stream().map(MaterialResponseDTO::valueOf).toList() : null,
            p.getListaDiaFuncionamento() != null ? p.getListaDiaFuncionamento().stream().map(DiaFuncionamentoResponseDTO::valueOf).toList() : null
        );
    }
}
