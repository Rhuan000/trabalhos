package aula.ada.api;

import aula.ada.domain.PrioridadeTarefa;
import aula.ada.domain.StatusTarefa;
import aula.ada.dto.TarefaRequest;
import aula.ada.dto.TarefaResponse;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Task1TarefaApiMemoria implements TarefaApi {
    private final ConcurrentHashMap<Long, TarefaResponse> dados = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public List<TarefaResponse> listar() { return dados.values().stream().sorted(Comparator.comparing(TarefaResponse::getId)).toList(); }
    public TarefaResponse buscar(long id) {
        TarefaResponse tarefa = dados.get(id);
        if (tarefa == null) throw new TarefaNaoEncontradaException(id);
        return tarefa;
    }
    public TarefaResponse criar(TarefaRequest request) {
        long id = sequencia.incrementAndGet();
        TarefaResponse tarefa = converter(id, request);
        dados.put(id, tarefa);
        return tarefa;
    }
    public TarefaResponse atualizar(long id, TarefaRequest request) {
        buscar(id);
        TarefaResponse tarefa = converter(id, request);
        dados.put(id, tarefa);
        return tarefa;
    }
    public void excluir(long id) {
        if (dados.remove(id) == null) throw new TarefaNaoEncontradaException(id);
    }
    private TarefaResponse converter(long id, TarefaRequest request) {
        return TarefaResponse.builder().id(id).titulo(request.getTitulo()).descricao(request.getDescricao())
                .status(request.getStatus() == null ? StatusTarefa.ABERTA : request.getStatus())
                .prioridade(request.getPrioridade() == null ? PrioridadeTarefa.MEDIA : request.getPrioridade()).build();
    }
}
