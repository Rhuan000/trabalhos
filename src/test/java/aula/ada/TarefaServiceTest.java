package aula.ada;

import aula.ada.api.Task1TarefaApiMemoria;
import aula.ada.dto.TarefaRequest;
import aula.ada.dto.TarefaResponse;
import aula.ada.domain.StatusTarefa;
import aula.ada.service.TarefaService;
import aula.ada.service.TransicaoTarefaInvalidaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TarefaServiceTest {

    @Test
    void naoPermiteConcluirTarefaCancelada() {
        TarefaService service = new TarefaService(new Task1TarefaApiMemoria());
        TarefaResponse criada = service.criar(request(StatusTarefa.CANCELADA));

        assertThrows(TransicaoTarefaInvalidaException.class,
                () -> service.atualizar(criada.getId(), request(StatusTarefa.CONCLUIDA)));
    }

    private TarefaRequest request(StatusTarefa status) {
        return TarefaRequest.builder()
                .titulo("Tarefa de teste")
                .status(status)
                .build();
    }
}
