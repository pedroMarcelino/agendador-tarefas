package com.marcelino.agendadortarefas.business;

import com.marcelino.agendadortarefas.business.dto.TarefasDTO;
import com.marcelino.agendadortarefas.business.mapper.TarefaConverter;
import com.marcelino.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.marcelino.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.marcelino.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.marcelino.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefasService {
    private final TarefasRepository tarefasRepository;
    private final TarefaConverter tarefaConverter;
    private final JwtUtil jwtUtil;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto){
        String email = jwtUtil.extractUsername(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefaConverter.paraTarefasEntity(dto);


        return tarefaConverter.paraTarefasDTO(
                tarefasRepository.save(entity));
    }
}
