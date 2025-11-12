package unitins.ecoleta.model;


public class PontoColetaDistanciaProjectionImpl implements PontoColetaDistanciaProjection {
    private final Long id;
    private final Double distancia;

    public PontoColetaDistanciaProjectionImpl(Long id, Double distancia) {
        this.id = id;
        this.distancia = distancia;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Double getDistancia() {
        return distancia;
    }
}
