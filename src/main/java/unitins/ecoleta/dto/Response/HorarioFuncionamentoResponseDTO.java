package unitins.ecoleta.dto.Response;

import unitins.ecoleta.model.HorarioFuncionamento;
import unitins.ecoleta.model.enums.Disponibilidade;

public record HorarioFuncionamentoResponseDTO(
    String horarioInicial,
    String horarioFinal,
    Disponibilidade disponibilidade
) {
    public static HorarioFuncionamentoResponseDTO valueOf(HorarioFuncionamento h){
        return new HorarioFuncionamentoResponseDTO(
            h.getHorarioInicial(),
            h.getHorarioFinal(),
            h.getDisponibilidade()
        );
    }
}
