package unitins.ecoleta.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unitins.ecoleta.model.Telefone;

@ApplicationScoped
public class TelefoneRepository implements PanacheRepository<Telefone> {}
