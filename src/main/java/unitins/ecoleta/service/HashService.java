package unitins.ecoleta.service;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.elytron.security.common.BcryptUtil;

@ApplicationScoped
public class HashService {

    /**
     * Gera um hash BCrypt da senha fornecida
     */
    public String hashPassword(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash
     */
    public boolean verifyPassword(String password, String hashedPassword) {
        return BcryptUtil.matches(password, hashedPassword);
    }
}
