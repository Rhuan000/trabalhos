package aula.ada.service;

import aula.ada.api.TarefaNaoEncontradaException;
import aula.ada.dto.TarefaRequest;
import aula.ada.dto.TarefaResponse;
import aula.ada.entity.TarefaEntity;
import aula.ada.repository.TarefaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TarefaService {

    @Inject
    TarefaRepository tarefaRepository;

    public List<TarefaResponse> listar() {
        return tarefaRepository.listAll().stream()
                .map(TarefaEntity::toResponse)
                .toList();
    }

    public TarefaResponse buscar(long id) {
        return tarefaRepository.findByIdOptional(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id))
                .toResponse();
    }

    @Transactional
    public TarefaResponse criar(TarefaRequest request) {
        TarefaEntity entity = TarefaEntity.from(request);
        tarefaRepository.persist(entity);
        return entity.toResponse();
    }

    @Transactional
    public TarefaResponse atualizar(long id, TarefaRequest request) {
        TarefaEntity entity = tarefaRepository.findByIdOptional(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));

        entity.setTitulo(request.getTitulo());
        entity.setDescricao(request.getDescricao());
        entity.setStatus(request.getStatus() == null ? entity.getStatus() : request.getStatus());
        entity.setPrioridade(request.getPrioridade() == null ? entity.getPrioridade() : request.getPrioridade());
        tarefaRepository.getEntityManager().flush();
        return entity.toResponse();
    }

    @Transactional
    public void excluir(long id) {
        boolean removido = tarefaRepository.deleteById(id);
        if (!removido) {
            throw new TarefaNaoEncontradaException(id);
        }
    }
}
