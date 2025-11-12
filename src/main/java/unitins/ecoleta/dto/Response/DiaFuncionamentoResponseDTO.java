package unitins.ecoleta.dto.Response;

import java.util.List;

import unitins.ecoleta.model.DiaFuncionamento;
import unitins.ecoleta.model.enums.DiaSemana;

public record DiaFuncionamentoResponseDTO(
    DiaSemana diaSemana,
    List<HorarioFuncionamentoResponseDTO> listaHorarioFuncionamento
) {
    public static DiaFuncionamentoResponseDTO valueOf(DiaFuncionamento d){
        return new DiaFuncionamentoResponseDTO(
            d.getDiaSemana(),
            d.getListaHorarioFuncionamento().stream().map(HorarioFuncionamentoResponseDTO::valueOf).toList()
        );
    }
}
