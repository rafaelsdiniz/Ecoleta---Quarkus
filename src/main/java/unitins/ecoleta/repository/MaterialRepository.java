package unitins.ecoleta.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import unitins.ecoleta.model.Material;

@ApplicationScoped
public class MaterialRepository implements PanacheRepository<Material> {

    // Adiciona findAllById equivalente ao Spring
    public List<Material> findAllById(List<Long> ids) {
        return list("id in ?1", ids);
    }
}
