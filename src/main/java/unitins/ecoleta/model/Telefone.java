package unitins.ecoleta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Telefone extends DefaultEntity {

    @Column(name = "codigo_area", nullable = false)
    private String codigoArea;

    @Column(nullable = false)
    private String numero;

    // ✅ Relacionamento correto (lado dono da FK)
    @ManyToOne
    @JoinColumn(name = "id_ponto_coleta", nullable = true) // <-- agora pode ser nulo
    private PontoColeta pontoColeta;

    public String getCodigoArea() { return codigoArea; }
    public void setCodigoArea(String codigoArea) { this.codigoArea = codigoArea; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public PontoColeta getPontoColeta() { return pontoColeta; }
    public void setPontoColeta(PontoColeta pontoColeta) { this.pontoColeta = pontoColeta; }
}
