package unitins.ecoleta.service;

import java.io.File;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import unitins.ecoleta.dto.Request.MaterialRequestDTO;
import unitins.ecoleta.model.Material;
import unitins.ecoleta.repository.MaterialRepository;

@ApplicationScoped
public class MaterialServiceImpl implements MaterialService {

    @Inject
    MaterialRepository materialRepository;

    @Inject
    MaterialImageService materialImageService;

    @Override
    public List<Material> findAll(int pageNumber, int pageSize) {
        return materialRepository.findAll()
                                 .page(pageNumber, pageSize)
                                 .list();
    }

    @Override
    public Material findById(Long id) {
        Material material = materialRepository.findById(id);
        if (material == null)
            throw new NotFoundException("Material não encontrado");
        return material;
    }

    @Override
    @Transactional
    public Material create(MaterialRequestDTO dto) {
        Material material = new Material();
        material.setNome(dto.nome());
        materialRepository.persist(material);
        return material;
    }

    @Override
    @Transactional
    public Material update(Long id, MaterialRequestDTO dto) {
        Material entity = materialRepository.findById(id);
        if (entity == null)
            throw new NotFoundException("Material não encontrado");

        entity.setNome(dto.nome());
        materialRepository.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Material entity = materialRepository.findById(id);
        if (entity == null)
            throw new NotFoundException("Material não encontrado");

        // apaga imagem associada, se existir
        if (entity.getNomeImagem() != null)
            materialImageService.delete(entity.getNomeImagem());

        materialRepository.delete(entity);
    }

    @Override
    public File download(String nomeArquivo) {
        return materialImageService.download(nomeArquivo);
    }

    @Override
    @Transactional
    public void upload(Long id, String nomeArquivo, byte[] arquivo) {
        Material material = materialRepository.findById(id);
        if (material == null)
            throw new NotFoundException("Material não encontrado");

        String nomeImagem = materialImageService.upload(nomeArquivo, arquivo);

        if (material.getNomeImagem() != null)
            materialImageService.delete(material.getNomeImagem());

        material.setNomeImagem(nomeImagem);
        materialRepository.persist(material);
    }
}
