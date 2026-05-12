package com.marcelino.agendadortarefas.business.mapper;

import com.marcelino.agendadortarefas.business.dto.TarefasDTO;
import com.marcelino.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefaConverter {

    TarefasEntity paraTarefasEntity(TarefasDTO dto);

    TarefasDTO paraTarefasDTO(TarefasEntity entity);


}
