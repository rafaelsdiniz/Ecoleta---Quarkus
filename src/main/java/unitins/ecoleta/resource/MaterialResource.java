package unitins.ecoleta.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
import unitins.ecoleta.dto.Request.MaterialRequestDTO;
import unitins.ecoleta.dto.Response.MaterialResponseDTO;
import unitins.ecoleta.service.MaterialService;

@Path("/materiais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource {

    @Inject
    MaterialService materialService;

    // ========== READ ==========
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        MaterialResponseDTO dto = MaterialResponseDTO.valueOf(materialService.findById(id));
        return Response.ok(dto).build();
    }

    @GET
    public Response findAll(
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize) {
        var lista = materialService.findAll(pageNumber, pageSize)
                .stream()
                .map(MaterialResponseDTO::valueOf)
                .toList();
        return Response.ok(lista).build();
    }

    // ========== CREATE ==========
    @POST
    public Response create(MaterialRequestDTO dto) {
        MaterialResponseDTO criado = MaterialResponseDTO.valueOf(materialService.create(dto));
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    // ========== UPDATE ==========
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, MaterialRequestDTO dto) {
        MaterialResponseDTO atualizado = MaterialResponseDTO.valueOf(materialService.update(id, dto));
        return Response.ok(atualizado).build();
    }

    // ========== DELETE ==========
    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        materialService.deleteById(id);
        return Response.ok(Map.of("message", "Material removido com sucesso.")).build();
    }

    // ========== UPLOAD ==========
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
            materialService.upload(id, nomeArquivo, bytes);
            return Response.ok(Map.of("message", "Upload realizado com sucesso.")).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao processar o upload: " + e.getMessage()))
                    .build();
        }
    }
}
