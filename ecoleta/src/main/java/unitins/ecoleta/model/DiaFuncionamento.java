package unitins.ecoleta.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import unitins.ecoleta.model.enums.DiaSemana;

@Entity
@Table(name = "dia_funcionamento")
public class DiaFuncionamento extends DefaultEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_funcionamento", nullable = false)
    private List<HorarioFuncionamento> listaHorarioFuncionamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    public List<HorarioFuncionamento> getListaHorarioFuncionamento() {
        return listaHorarioFuncionamento;
    }

    public void setListaHorarioFuncionamento(List<HorarioFuncionamento> listaHorarioFuncionamento) {
        this.listaHorarioFuncionamento = listaHorarioFuncionamento;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }
}
