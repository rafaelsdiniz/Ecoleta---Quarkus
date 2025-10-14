package unitins.ecoleta.service;

import java.io.File;
import java.util.List;

import unitins.ecoleta.dto.Request.PontoColetaRequestDTO;
import unitins.ecoleta.dto.Response.PontoColetaResponseDTO;
import unitins.ecoleta.model.PontoColeta;

public interface PontoColetaService {

    PontoColeta findById(Long id);

    /**
     * Busca todos os pontos de coleta com paginação.
     * @param page página (0-based)
     * @param size tamanho da página
     * @param materialId filtro opcional pelo material (0 = sem filtro)
     * @return lista de pontos de coleta
     */
    List<PontoColeta> findAll(int page, int size, int materialId);

    /**
     * Busca pontos de coleta por distância, material e termo, paginados.
     * @param latitude latitude do usuário
     * @param longitude longitude do usuário
     * @param materialId id do material (0 = sem filtro)
     * @param term termo de busca (nome ou endereço)
     * @param page página (0-based)
     * @param size tamanho da página
     * @return resultado paginado com DTOs
     */
    PaginatedResult<PontoColetaResponseDTO> findByDistance(double latitude,
                                                           double longitude,
                                                           int materialId,
                                                           String term,
                                                           int page,
                                                           int size);

    List<PontoColeta> findByNomeAndEndereco(String term, int materialId);

    PontoColeta create(PontoColetaRequestDTO dto);

    void update(Long id, PontoColetaRequestDTO dto);

    void deleteById(Long id);

    File download(String nomeArquivo);

    void upload(Long id, String nomeArquivo, byte[] arquivo);
}

