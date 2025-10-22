package unitins.ecoleta.security;

import jakarta.annotation.Priority;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.inject.Inject;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Check if the endpoint has @PermitAll annotation
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class) ||
            resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            return; // Allow access without authentication
        }

        // Check if Authorization header is present
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token JWT não fornecido ou inválido")
                    .build()
            );
            return;
        }

        // JWT validation is handled automatically by Quarkus SmallRye JWT
        // If we reach here and jwt is valid, the request proceeds
    }
}
