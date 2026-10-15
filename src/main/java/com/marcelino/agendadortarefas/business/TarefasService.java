package com.marcelino.agendadortarefas.business;

import com.marcelino.agendadortarefas.business.dto.TarefasDTO;
import com.marcelino.agendadortarefas.business.mapper.TarefaConverter;
import com.marcelino.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.marcelino.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.marcelino.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.marcelino.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.marcelino.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.marcelino.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {
    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefasEntity(dto);


        return tarefaConverter.paraTarefasDTO(
                tarefasRepository.save(entity)
        );
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal){
        return tarefaConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal)
        );
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token){
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefasEntity> listaTarefas = tarefasRepository.findByEmailUsuario(email);
        return tarefaConverter.paraListaTarefasDTO((listaTarefas));
    }

    public void deletarTarefaPorId (String id){
        try{
            tarefasRepository.deleteById(id);
        }catch(ResourceNotFoundException e ){
            throw new ResourceNotFoundException("Erro ao deletar Tarefa por id ou id inexistente" + id, e.getCause());
        }

    }

    public TarefasDTO alterarStatus (StatusNotificacaoEnum status, String id){
        try{
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada" + id));
            entity.setStatusNotificacaoEnum(status);
            return tarefaConverter.paraTarefasDTO(tarefasRepository.save(entity));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Erro ai alterar status da tarefa" + e.getCause());
        }
    }

    public  TarefasDTO updateTarefas(TarefasDTO dto, String id){
        try {
            TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa nao encontrada" + id));
            tarefaUpdateConverter.updateTarefas(dto, entity);
            return  tarefaConverter.paraTarefasDTO(tarefasRepository.save(entity));
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ai alterar status da tarefa" + e.getCause());
        }
    }
}
