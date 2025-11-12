package unitins.ecoleta.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import unitins.ecoleta.dto.Request.PontoColetaRequestDTO;
import unitins.ecoleta.dto.Response.PontoColetaResponseDTO;
import unitins.ecoleta.service.PontoColetaService;

@Path("/pontos-coleta")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PontoColetaResource {

    @Inject
    PontoColetaService pontoColetaService;

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        PontoColetaResponseDTO ponto = PontoColetaResponseDTO.valueOf(pontoColetaService.findById(id));
        return Response.ok(ponto).build();
    }

    @GET
    public Response findAll(
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("100") int pageSize,
            @QueryParam("materialId") @DefaultValue("0") int materialId,
            @QueryParam("term") @DefaultValue("") String term,
            @QueryParam("latitude") @DefaultValue("0") double latitude,
            @QueryParam("longitude") @DefaultValue("0") double longitude) {

        if (latitude != 0 && longitude != 0) {
            return Response.ok(
                    pontoColetaService.findByDistance(latitude, longitude, materialId, term, pageNumber, pageSize)
            ).build();
        }

        if (!term.isBlank()) {
            List<PontoColetaResponseDTO> lista = pontoColetaService.findByNomeAndEndereco(term, materialId)
                    .stream()
                    .map(PontoColetaResponseDTO::valueOf)
                    .toList();
            return Response.ok(lista).build();
        }

        List<PontoColetaResponseDTO> all = pontoColetaService.findAll(pageNumber, pageSize, materialId)
                .stream()
                .map(PontoColetaResponseDTO::valueOf)
                .toList();
        return Response.ok(all).build();
    }

    @POST
    public Response create(PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO criado = PontoColetaResponseDTO.valueOf(pontoColetaService.create(dto));
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO atualizado = PontoColetaResponseDTO.valueOf(pontoColetaService.update(id, dto));
        return Response.ok(atualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        pontoColetaService.deleteById(id);
        return Response.ok(Map.of("message", "Ponto de coleta removido com sucesso.")).build();
    }

    @PATCH
    @Path("/{id}/image/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@PathParam("id") Long id,
                        @RestForm("nomeArquivo") String nomeArquivo,
                        @RestForm("arquivo") FileUpload arquivo) {

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Campo 'nomeArquivo' é obrigatório")).build();
        }

        if (arquivo == null || arquivo.uploadedFile() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Campo 'arquivo' é obrigatório")).build();
        }

        try {
            byte[] bytes = java.nio.file.Files.readAllBytes(arquivo.uploadedFile());
            pontoColetaService.upload(id, nomeArquivo, bytes);
            return Response.ok(Map.of("message", "Upload realizado com sucesso.")).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao processar o upload: " + e.getMessage()))
                    .build();
        }
    }
}
