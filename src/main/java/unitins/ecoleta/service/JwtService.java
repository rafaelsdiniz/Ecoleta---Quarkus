package unitins.ecoleta.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import io.smallrye.jwt.build.Jwt;
import unitins.ecoleta.model.Usuario;

@ApplicationScoped
public class JwtService {

    private static final Duration JWT_DURATION = Duration.ofHours(24);

    /**
     * Gera um token JWT para o usuário autenticado
     */
    public String generateJwt(Usuario usuario) {
        Set<String> roles = new HashSet<>();
        roles.add(usuario.getTipoUsuario().name());

        return Jwt.issuer("ecoleta-api")
                .upn(usuario.getEmail())
                .subject(usuario.getId().toString())
                .groups(roles)
                .expiresAt(Instant.now().plus(JWT_DURATION))
                .sign();
    }
}
