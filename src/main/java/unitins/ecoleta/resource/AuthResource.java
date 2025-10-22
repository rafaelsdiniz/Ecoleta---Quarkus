package unitins.ecoleta.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import unitins.ecoleta.dto.Request.LoginRequestDTO;
import unitins.ecoleta.dto.Response.AuthResponseDTO;
import unitins.ecoleta.dto.Response.UsuarioResponseDTO;
import unitins.ecoleta.service.JwtService;
import unitins.ecoleta.service.UsuarioService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService usuarioService;

    @Inject
    JwtService jwtService;

    @POST
    @Path("/login")
    @PermitAll
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
