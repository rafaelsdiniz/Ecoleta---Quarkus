package unitins.ecoleta.service;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import unitins.ecoleta.dto.Request.DiaFuncionamentoRequestDTO;
import unitins.ecoleta.dto.Request.PontoColetaRequestDTO;
import unitins.ecoleta.dto.Request.TelefoneRequestDTO;
import unitins.ecoleta.dto.Response.PontoColetaResponseDTO;
import unitins.ecoleta.model.DiaFuncionamento;
import unitins.ecoleta.model.Material;
import unitins.ecoleta.model.PontoColeta;
import unitins.ecoleta.model.PontoColetaDistanciaProjection;
import unitins.ecoleta.model.Telefone;
import unitins.ecoleta.repository.MaterialRepository;
import unitins.ecoleta.repository.PontoColetaRepository;

@ApplicationScoped
public class PontoColetaServiceImpl implements PontoColetaService {

    @Inject
    PontoColetaRepository pontoColetaRepository;

    @Inject
    MaterialRepository materialRepository;

    @Inject
    PontoColetaImageService pontoColetaImageService;

    @Override
    public List<PontoColeta> findAll(int page, int size, int materialId) {
        return pontoColetaRepository.findByCategory(page, size, materialId == 0 ? null : materialId);
    }

    @Override
    public PaginatedResult<PontoColetaResponseDTO> findByDistance(double latitude,
                                                                  double longitude,
                                                                  int materialId,
                                                                  String term,
                                                                  int page,
                                                                  int size) {

        List<PontoColetaDistanciaProjection> projPage =
                pontoColetaRepository.findByDistanceAndOptionalMaterialAndTerm(
                        latitude,
                        longitude,
                        materialId == 0 ? null : materialId,
                        term,
                        page,
                        size
                );

        long totalElements = pontoColetaRepository.countByDistanceAndOptionalMaterialAndTerm(
                latitude, longitude, materialId == 0 ? null : materialId, term);

        List<Long> ids = projPage.stream().map(PontoColetaDistanciaProjection::getId).toList();
        Map<Long, Double> mapDist = projPage.stream()
                .collect(Collectors.toMap(PontoColetaDistanciaProjection::getId,
                        PontoColetaDistanciaProjection::getDistancia));

        List<PontoColetaResponseDTO> dtos =
                pontoColetaRepository.findAllById(ids).stream()
                        .map(p -> PontoColetaResponseDTO.valueOf(p, mapDist.get(p.getId())))
                        .sorted(Comparator.comparingDouble(PontoColetaResponseDTO::distancia))
                        .toList();

        return new PaginatedResult<>(dtos, page, size, totalElements);
    }

    @Override
    public List<PontoColeta> findByNomeAndEndereco(String term, int materialId) {
        return pontoColetaRepository.findByTermAndOptionalMaterial(term, materialId == 0 ? null : materialId);
    }

    @Override
    public PontoColeta findById(Long id) {
        return pontoColetaRepository.findById(id);
    }

    @Override
    @Transactional
    public PontoColeta create(PontoColetaRequestDTO dto) {
        List<Material> listaMaterial = materialRepository.findAllById(dto.listaIdMaterial());
        List<DiaFuncionamento> listaDiaFuncionamento = dto.listaDiaFuncionamento().stream()
                .map(DiaFuncionamentoRequestDTO::toEntity)
                .toList();
        List<Telefone> listaTelefone = dto.listaTelefone().stream()
                .map(TelefoneRequestDTO::toEntity)
                .toList();

        PontoColeta pontoColeta = new PontoColeta();
        pontoColeta.setNome(dto.nome());
        pontoColeta.setDescricao(dto.descricao());
        pontoColeta.setEmail(dto.email());
        pontoColeta.setEndereco(dto.endereco());
        pontoColeta.setLatitude(dto.latitude());
        pontoColeta.setLongitude(dto.longitude());
        pontoColeta.setListaTelefone(listaTelefone);
        pontoColeta.setListaMaterial(listaMaterial);
        pontoColeta.setListaDiaFuncionamento(listaDiaFuncionamento);

        pontoColetaRepository.persist(pontoColeta);
        return pontoColeta;
    }

    @Override
    @Transactional
    public void update(Long id, PontoColetaRequestDTO dto) {
        List<Material> listaMaterial = materialRepository.findAllById(dto.listaIdMaterial());
        List<DiaFuncionamento> listaDiaFuncionamento = dto.listaDiaFuncionamento().stream()
                .map(DiaFuncionamentoRequestDTO::toEntity)
                .toList();
        List<Telefone> listaTelefone = dto.listaTelefone().stream()
                .map(TelefoneRequestDTO::toEntity)
                .toList();

        PontoColeta pontoColeta = pontoColetaRepository.findById(id);
        pontoColeta.setNome(dto.nome());
        pontoColeta.setDescricao(dto.descricao());
        pontoColeta.setEmail(dto.email());
        pontoColeta.setEndereco(dto.endereco());
        pontoColeta.setLatitude(dto.latitude());
        pontoColeta.setLongitude(dto.longitude());
        pontoColeta.setListaMaterial(listaMaterial);

        pontoColeta.getListaTelefone().clear();
        pontoColeta.getListaTelefone().addAll(listaTelefone);

        pontoColeta.getListaDiaFuncionamento().clear();
        pontoColeta.getListaDiaFuncionamento().addAll(listaDiaFuncionamento);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        pontoColetaRepository.deleteById(id);
    }

    @Override
    public File download(String nomeArquivo) {
        return pontoColetaImageService.download(nomeArquivo);
    }

    @Override
    @Transactional
    public void upload(Long id, String nomeArquivo, byte[] arquivo) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id);
        String nomeImagem = pontoColetaImageService.upload(nomeArquivo, arquivo);

        if (pontoColeta.getNomeImagem() != null)
            pontoColetaImageService.delete(pontoColeta.getNomeImagem());

        pontoColeta.setNomeImagem(nomeImagem);
    }
}
