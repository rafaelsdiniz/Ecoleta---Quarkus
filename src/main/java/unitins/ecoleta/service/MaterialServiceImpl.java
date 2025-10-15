package unitins.ecoleta.service;

import java.io.File;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    public List<Material> findAll(int pageNumber, int pageSize){
        // Usando PanacheRepository do Quarkus para paginação
        return materialRepository.findAll()
                                 .page(pageNumber, pageSize)
                                 .list();
    }

    @Override
    public Material findById(Long id){
        return materialRepository.findById(id); // Panache retorna null se não existir
    }

    @Override
    @Transactional
    public Material create(MaterialRequestDTO dto){
        Material material = new Material();
        material.setNome(dto.nome());

        materialRepository.persist(material); // persist() ao invés de save()
        return material;
    }

    @Override
    @Transactional
    public void update(Long id, MaterialRequestDTO dto){
        Material material = materialRepository.findById(id);
        if(material != null){
            material.setNome(dto.nome());
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        materialRepository.deleteById(id);
    }

    @Override
    public File download(String nomeArquivo) {
        return materialImageService.download(nomeArquivo);
    }

    @Override
    @Transactional
    public void upload(Long id, String nomeArquivo, byte[] arquivo) {
        Material material = materialRepository.findById(id);
        if(material != null){
            String nomeImagem = materialImageService.upload(nomeArquivo, arquivo);

            // Remove imagem antiga, se existir
            if(material.getNomeImagem() != null)
                materialImageService.delete(material.getNomeImagem());

            material.setNomeImagem(nomeImagem);
        }
    }
}
