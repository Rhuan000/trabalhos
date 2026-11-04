package aula.ada.service;

import aula.ada.api.TarefaApi;
import aula.ada.api.Task1TarefaApiMemoria;
import aula.ada.dto.TarefaRequest;
import aula.ada.dto.TarefaResponse;
import aula.ada.domain.StatusTarefa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TarefaService implements TarefaApi {
    private final Task1TarefaApiMemoria armazenamento;

    @Inject
    public TarefaService(Task1TarefaApiMemoria armazenamento) {
        this.armazenamento = armazenamento;
    }

    @Override
    public List<TarefaResponse> listar() {
        return armazenamento.listar();
    }

    @Override
    public TarefaResponse buscar(long id) {
        return armazenamento.buscar(id);
    }

    @Override
    public TarefaResponse criar(TarefaRequest request) {
        return armazenamento.criar(request);
    }

    @Override
    public TarefaResponse atualizar(long id, TarefaRequest request) {
        TarefaResponse atual = buscar(id);
        StatusTarefa novoStatus = request.getStatus();
        if (atual.getStatus() == StatusTarefa.CANCELADA && novoStatus == StatusTarefa.CONCLUIDA) {
            throw new TransicaoTarefaInvalidaException(id, atual.getStatus(), novoStatus);
        }
        return armazenamento.atualizar(id, request);
    }

    @Override
    public void excluir(long id) {
        buscar(id);
        armazenamento.excluir(id);
    }
}
