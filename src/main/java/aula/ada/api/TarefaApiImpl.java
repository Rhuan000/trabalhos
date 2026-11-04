package aula.ada.api;

import aula.ada.dto.TarefaRequest;
import aula.ada.dto.TarefaResponse;
import aula.ada.service.TarefaService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TarefaApiImpl implements TarefaApi {

    @Inject
    TarefaService tarefaService;

    @Override
    public List<TarefaResponse> listar() {
        return tarefaService.listar();
    }

    @Override
    public TarefaResponse buscar(long id) {
        return tarefaService.buscar(id);
    }

    @Override
    public TarefaResponse criar(TarefaRequest request) {
        return tarefaService.criar(request);
    }

    @Override
    public TarefaResponse atualizar(long id, TarefaRequest request) {
        return tarefaService.atualizar(id, request);
    }

    @Override
    public void excluir(long id) {
        tarefaService.excluir(id);
    }
}
