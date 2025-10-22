package unitins.ecoleta.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import unitins.ecoleta.model.enums.DiaSemana;

@Entity
@Table(name = "dia_funcionamento")
public class DiaFuncionamento extends DefaultEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    // ✅ Relacionamento com HorarioFuncionamento (mantido)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "id_dia_funcionamento", nullable = false)
    private List<HorarioFuncionamento> listaHorarioFuncionamento;

    // ✅ Relacionamento adicionado com PontoColeta (lado dono da relação)
    @ManyToOne
    @JoinColumn(name = "id_ponto_coleta", nullable = false)
    private PontoColeta pontoColeta;

    // Getters e Setters
    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public List<HorarioFuncionamento> getListaHorarioFuncionamento() {
        return listaHorarioFuncionamento;
    }

    public void setListaHorarioFuncionamento(List<HorarioFuncionamento> listaHorarioFuncionamento) {
        this.listaHorarioFuncionamento = listaHorarioFuncionamento;
    }

    public PontoColeta getPontoColeta() {
        return pontoColeta;
    }

    public void setPontoColeta(PontoColeta pontoColeta) {
        this.pontoColeta = pontoColeta;
    }
}
