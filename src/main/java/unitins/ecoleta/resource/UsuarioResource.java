package unitins.ecoleta.resource;

import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
import unitins.ecoleta.dto.Response.AuthResponseDTO;
import unitins.ecoleta.dto.Response.UsuarioResponseDTO;
import unitins.ecoleta.service.JwtService;
import unitins.ecoleta.service.UsuarioService;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JwtService jwtService;

    @GET
    @RolesAllowed({"ADMIN", "USUARIO"})
    public List<UsuarioResponseDTO> getAll() {
        return usuarioService.findAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USUARIO"})
    public Response getById(@PathParam("id") Long id) {
        UsuarioResponseDTO dto = usuarioService.findById(id);
        if (dto == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(dto).build();
    }

    @POST
    @PermitAll
    public Response create(@Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO created = usuarioService.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "USUARIO"})
    public Response update(@PathParam("id") Long id, @Valid UsuarioRequestDTO dto) {
        UsuarioResponseDTO updated = usuarioService.update(id, dto);
        if (updated == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        usuarioService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDTO dto) {
        var usuario = usuarioService.autenticar(dto.email(), dto.senha());
        if (usuario == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("E-mail ou senha inválidos.")
                           .build();
        }

        // Generate JWT token
        String token = jwtService.generateJwt(usuario);
        
        // Return token and user data
        AuthResponseDTO authResponse = new AuthResponseDTO(
            token,
            new UsuarioResponseDTO(usuario)
        );
        
        return Response.ok(authResponse).build();
    }
}
