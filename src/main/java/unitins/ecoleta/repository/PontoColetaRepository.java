package unitins.ecoleta.repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import unitins.ecoleta.model.PontoColeta;
import unitins.ecoleta.model.PontoColetaDistanciaProjection;

@ApplicationScoped
public class PontoColetaRepository implements PanacheRepository<PontoColeta> {

    @PersistenceContext
    EntityManager em;

    /**
     * Busca todos os pontos de coleta, opcionalmente filtrando pelo materialId, com paginação.
     * Se page ou size forem nulos ou size <= 0, retorna todos os registros.
     */
    public List<PontoColeta> findByCategory(Integer page, Integer size, Integer materialId) {
        String jpql = "SELECT DISTINCT p FROM PontoColeta p LEFT JOIN p.listaMaterial m " +
                      "WHERE (:materialId IS NULL OR m.id = :materialId)";
        Query query = em.createQuery(jpql, PontoColeta.class)
                        .setParameter("materialId", materialId);

        if (page != null && size != null && size > 0) {
            query.setFirstResult(page * size)
                 .setMaxResults(size);
        }

        @SuppressWarnings("unchecked")
        List<PontoColeta> result = query.getResultList();
        return result;
    }

    /**
     * Busca por nome ou endereço, opcionalmente filtrando pelo materialId.
     */
    public List<PontoColeta> findByTermAndOptionalMaterial(String term, Integer materialId) {
        String jpql = "SELECT DISTINCT p FROM PontoColeta p LEFT JOIN p.listaMaterial m " +
                      "WHERE (UPPER(p.nome) LIKE CONCAT('%', UPPER(:term), '%') " +
                      "OR UPPER(p.endereco) LIKE CONCAT('%', UPPER(:term), '%')) " +
                      "AND (:materialId IS NULL OR m.id = :materialId)";
        return em.createQuery(jpql, PontoColeta.class)
                 .setParameter("term", term)
                 .setParameter("materialId", materialId)
                 .getResultList();
    }

    /**
     * Busca pontos próximos do usuário com cálculo de distância (projeção),
     * aplicando paginação.
     */
    public List<PontoColetaDistanciaProjection> findByDistanceAndOptionalMaterialAndTerm(
            double lat, double lng, Integer materialId, String term, int page, int size) {

        String sql = """
            SELECT p.id AS id,
                   (6371 * acos(
                       cos(radians(:lat)) * cos(radians(p.latitude)) *
                       cos(radians(p.longitude) - radians(:lng)) +
                       sin(radians(:lat)) * sin(radians(p.latitude))
                   )) AS distancia
            FROM ponto_coleta p
            LEFT JOIN material_ponto_coleta mpc ON p.id = mpc.id_ponto_coleta
            WHERE (:materialId IS NULL OR mpc.id_material = :materialId)
              AND (:term = '' OR
                   UPPER(p.nome) LIKE CONCAT('%', UPPER(:term), '%') OR
                   UPPER(p.endereco) LIKE CONCAT('%', UPPER(:term), '%'))
            GROUP BY p.id
            ORDER BY distancia
        """;

        Query query = em.createNativeQuery(sql, "PontoColetaDistanciaMapping")
                        .setParameter("lat", lat)
                        .setParameter("lng", lng)
                        .setParameter("materialId", materialId)
                        .setParameter("term", term);

        // Paginação só se size > 0
        if (size > 0) {
            query.setFirstResult(page * size)
                 .setMaxResults(size);
        }

        @SuppressWarnings("unchecked")
        List<PontoColetaDistanciaProjection> result = query.getResultList();
        return result;
    }

    /**
     * Conta total de resultados para a pesquisa de distância (para paginação).
     */
    public long countByDistanceAndOptionalMaterialAndTerm(double lat, double lng, Integer materialId, String term) {
        String sql = """
            SELECT COUNT(DISTINCT p.id)
            FROM ponto_coleta p
            LEFT JOIN material_ponto_coleta mpc ON p.id = mpc.id_ponto_coleta
            WHERE (:materialId IS NULL OR mpc.id_material = :materialId)
              AND (:term = '' OR
                   UPPER(p.nome) LIKE CONCAT('%', UPPER(:term), '%') OR
                   UPPER(p.endereco) LIKE CONCAT('%', UPPER(:term), '%'))
        """;

        Query query = em.createNativeQuery(sql)
                        .setParameter("materialId", materialId)
                        .setParameter("term", term);

        return ((Number) query.getSingleResult()).longValue();
    }

    /**
     * Busca múltiplos registros por ID.
     */
    public List<PontoColeta> findAllById(List<Long> ids) {
        return list("id in ?1", ids);
    }
}
