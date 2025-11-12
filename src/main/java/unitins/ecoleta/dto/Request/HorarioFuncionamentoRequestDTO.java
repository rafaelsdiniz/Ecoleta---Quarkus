package unitins.ecoleta.dto.Request;

import unitins.ecoleta.model.HorarioFuncionamento;
import unitins.ecoleta.model.enums.Disponibilidade;

public record HorarioFuncionamentoRequestDTO(
    String horarioInicial,
    String horarioFinal,
    String disponibilidade
) {

    public static HorarioFuncionamento toEntity(HorarioFuncionamentoRequestDTO dto) {
        HorarioFuncionamento horario = new HorarioFuncionamento();
        horario.setHorarioInicial(dto.horarioInicial());
        horario.setHorarioFinal(dto.horarioFinal());

        // Converte a string de disponibilidade de forma segura
        if (dto.disponibilidade() != null && !dto.disponibilidade().isBlank()) {
            try {
                horario.setDisponibilidade(
                    Disponibilidade.valueOf(dto.disponibilidade().toUpperCase())
                );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Valor de disponibilidade inválido: '" + dto.disponibilidade()
                    + "'. Valores válidos: AUTO_ATENDIMENTO, APENAS_COM_PROFISSIONAL."
                );
            }
        } else {
            throw new IllegalArgumentException("O campo 'disponibilidade' é obrigatório.");
        }

        return horario;
    }
}
