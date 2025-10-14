package unitins.ecoleta.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    // ===================== CRUD =====================
    @GET
    @Path("/{id}")
    public MaterialResponseDTO findById(@PathParam("id") Long id){
        return MaterialResponseDTO.valueOf(materialService.findById(id));
    }

    @GET
    public Response findAll(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                            @QueryParam("pageSize") @DefaultValue("20") int pageSize){
        var lista = materialService.findAll(pageNumber, pageSize)
                                   .stream()
                                   .map(MaterialResponseDTO::valueOf)
                                   .toList();
        return Response.ok(lista).build();
    }

    @POST
    public Response create(MaterialRequestDTO dto){
        MaterialResponseDTO criado = MaterialResponseDTO.valueOf(materialService.create(dto));
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, MaterialRequestDTO dto){
        materialService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id){
        materialService.deleteById(id);
        return Response.noContent().build();
    }

    // ===================== DOWNLOAD =====================
    @GET
    @Path("/image/download/{nomeImagem}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) throws IOException {
        File file = materialService.download(nomeImagem);
        if (file == null || !file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new FileInputStream(file))
                       .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                       .build();
    }

    // ===================== UPLOAD =====================
    @PATCH
    @Path("/{id}/image/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@PathParam("id") Long id,
                        @RestForm("nomeArquivo") String nomeArquivo,
                        @RestForm("arquivo") FileUpload arquivo) {

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Campo 'nomeArquivo' é obrigatório").build();
        }
        if (arquivo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Campo 'arquivo' é obrigatório").build();
        }

        try {
            // Converte o arquivo para bytes
            byte[] bytes = arquivo.uploadedFile().toFile().toPath().toFile().exists() ?
                        java.nio.file.Files.readAllBytes(arquivo.uploadedFile().toFile().toPath()) :
                        new byte[0];
            
            materialService.upload(id, nomeArquivo, bytes);
            return Response.noContent().build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(e.getMessage()).build();
        }
    }
}
