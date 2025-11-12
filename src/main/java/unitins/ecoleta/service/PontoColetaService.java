package unitins.ecoleta.service;

import java.io.File;
import java.util.List;

import unitins.ecoleta.dto.Request.PontoColetaRequestDTO;
import unitins.ecoleta.dto.Response.PontoColetaResponseDTO;
import unitins.ecoleta.model.PontoColeta;

public interface PontoColetaService {

    PontoColeta findById(Long id);

    List<PontoColeta> findAll(int page, int size, int materialId);

    PaginatedResult<PontoColetaResponseDTO> findByDistance(double latitude,
                                                           double longitude,
                                                           int materialId,
                                                           String term,
                                                           int page,
                                                           int size);

    List<PontoColeta> findByNomeAndEndereco(String term, int materialId);

    PontoColeta create(PontoColetaRequestDTO dto);

    PontoColeta update(Long id, PontoColetaRequestDTO dto);

    void deleteById(Long id);

    File download(String nomeArquivo);

    void upload(Long id, String nomeArquivo, byte[] arquivo);
}
