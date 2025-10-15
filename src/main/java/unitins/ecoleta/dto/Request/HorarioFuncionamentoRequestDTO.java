package unitins.ecoleta.dto.Request;

import unitins.ecoleta.model.HorarioFuncionamento;
import unitins.ecoleta.model.enums.Disponibilidade;

public record HorarioFuncionamentoRequestDTO(
    String horarioInicial,
    String horarioFinal,
    String disponibilidade 
) {
    public static HorarioFuncionamento toEntity(HorarioFuncionamentoRequestDTO dto){
        HorarioFuncionamento horario = new HorarioFuncionamento();
        horario.setHorarioInicial(dto.horarioInicial());
        horario.setHorarioFinal(dto.horarioFinal());
        horario.setDisponibilidade(Disponibilidade.valueOf(dto.disponibilidade().toUpperCase()));
        return horario;
    }
}
