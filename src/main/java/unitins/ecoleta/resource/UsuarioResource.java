package unitins.ecoleta.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import unitins.ecoleta.dto.Request.LoginRequestDTO;
import unitins.ecoleta.dto.Request.UsuarioRequestDTO;
import unitins.ecoleta.dto.Response.LoginResponseDTO;
import unitins.ecoleta.dto.Response.UsuarioResponseDTO;
import unitins.ecoleta.service.UsuarioService;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioResponseDTO> getAll() {
        return usuarioService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        UsuarioResponseDTO dto = usuarioService.findById(id);
        if (dto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(dto).build();
    }

    @POST
    public Response create(@Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO created = usuarioService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO updated = usuarioService.update(id, dto);
        if (updated == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        usuarioService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDTO dto) {
        LoginResponseDTO loginResponse = usuarioService.autenticar(dto.email(), dto.senha());
        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("E-mail ou senha inválidos.")
                           .build();
        }

        return Response.ok(loginResponse).build();
    }
}
