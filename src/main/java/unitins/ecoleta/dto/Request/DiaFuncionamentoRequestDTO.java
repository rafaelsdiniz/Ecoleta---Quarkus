package unitins.ecoleta.dto.Request;

import java.util.List;

import unitins.ecoleta.model.DiaFuncionamento;
import unitins.ecoleta.model.enums.DiaSemana;

public record DiaFuncionamentoRequestDTO(
    String diaSemana, 
    List<HorarioFuncionamentoRequestDTO> listaHorarioFuncionamento
) {
    public static DiaFuncionamento toEntity(DiaFuncionamentoRequestDTO dto){
        DiaFuncionamento diaFuncionamento = new DiaFuncionamento();
        diaFuncionamento.setDiaSemana(DiaSemana.valueOf(dto.diaSemana().toUpperCase()));
        diaFuncionamento.setListaHorarioFuncionamento(
            dto.listaHorarioFuncionamento().stream().map(HorarioFuncionamentoRequestDTO::toEntity).toList()
        );
        return diaFuncionamento;
    }
}
