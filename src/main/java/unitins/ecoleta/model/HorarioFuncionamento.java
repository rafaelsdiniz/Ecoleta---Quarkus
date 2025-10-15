package unitins.ecoleta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import unitins.ecoleta.model.enums.Disponibilidade;

@Entity
@Table(name = "horario_funcionamento")
public class HorarioFuncionamento extends DefaultEntity {
    @Column(name = "horario_inicial", nullable = false)
    private String horarioInicial;

    @Column(name = "horario_final", nullable = false)
    private String horarioFinal;

    @Column(name = "id_disponibilidade", nullable = false)
    private Disponibilidade disponibilidade;

    public String getHorarioInicial() {
        return horarioInicial;
    }

    public void setHorarioInicial(String horarioInicial) {
        this.horarioInicial = horarioInicial;
    }

    public String getHorarioFinal() {
        return horarioFinal;
    }

    public void setHorarioFinal(String horarioFinal) {
        this.horarioFinal = horarioFinal;
    }

    public Disponibilidade getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
