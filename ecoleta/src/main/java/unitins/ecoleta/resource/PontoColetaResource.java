package unitins.ecoleta.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
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
        PontoColetaResponseDTO pontoColeta = PontoColetaResponseDTO.valueOf(pontoColetaService.findById(id));
        return Response.ok(pontoColeta).build();
    }

    @GET
    public Response findAll(
            @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
            @QueryParam("pageSize") @DefaultValue("20") int pageSize,
            @QueryParam("materialId") @DefaultValue("0") int materialId,
            @QueryParam("term") @DefaultValue("") String term,
            @QueryParam("latitude") @DefaultValue("0") double latitude,
            @QueryParam("longitude") @DefaultValue("0") double longitude
    ) {
        if (latitude != 0 && longitude != 0) {
            return Response.ok(
                    pontoColetaService.findByDistance(latitude, longitude, materialId, term, pageNumber, pageSize)
            ).build();
        }

        if (!term.isBlank()) {
            List<PontoColetaResponseDTO> listaResponse = pontoColetaService.findByNomeAndEndereco(term, materialId)
                    .stream()
                    .map(PontoColetaResponseDTO::valueOf)
                    .toList();
            return Response.ok(listaResponse).build();
        }

        List<PontoColetaResponseDTO> all = pontoColetaService.findAll(pageNumber, pageSize, materialId)
                .stream()
                .map(PontoColetaResponseDTO::valueOf)
                .toList();
        return Response.ok(all).build();
    }

    @POST
    public Response create(PontoColetaRequestDTO dto) {
        PontoColetaResponseDTO pontoCriado = PontoColetaResponseDTO.valueOf(pontoColetaService.create(dto));
        return Response.status(Response.Status.CREATED).entity(pontoCriado).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, PontoColetaRequestDTO dto) {
        pontoColetaService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        pontoColetaService.deleteById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/image/download/{nomeImagem}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("nomeImagem") String nomeImagem) {
        try {
            File file = pontoColetaService.download(nomeImagem);
            return Response.ok(new FileInputStream(file))
                    .header("Content-Disposition", "attachment; filename=\"" + nomeImagem + "\"")
                    .build();
        } catch (FileNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
