package unitins.ecoleta.service;

import java.io.File;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import unitins.ecoleta.dto.Request.MaterialRequestDTO;
import unitins.ecoleta.model.Material;

@ApplicationScoped
public interface MaterialService {

    Material findById(Long id);

    List<Material> findAll(int pageNumber, int pageSize);

    Material create(MaterialRequestDTO dto);

    Material update(Long id, MaterialRequestDTO dto);

    void deleteById(Long id);

    File download(String nomeArquivo);

    void upload(Long id, String nomeArquivo, byte[] arquivo);
}
