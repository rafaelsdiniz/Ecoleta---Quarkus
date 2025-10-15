package unitins.ecoleta.model;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
    name = "PontoColetaDistanciaMapping",
    classes = @ConstructorResult(
        targetClass = PontoColetaDistanciaProjectionImpl.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "distancia", type = Double.class)
        }
    )
)
public class PontoColetaDistanciaMappingMarker {
    // Esta classe existe apenas para registrar o mapping
}
