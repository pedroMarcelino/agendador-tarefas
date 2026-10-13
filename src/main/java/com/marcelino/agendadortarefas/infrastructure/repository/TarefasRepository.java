package com.marcelino.agendadortarefas.infrastructure.repository;

import com.marcelino.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TarefasRepository extends MongoRepository<TarefasEntity, String> {
    List<TarefasEntity> findByDataEventoBetween(LocalDateTime dataIncial, LocalDateTime dataFinal);
    List<TarefasEntity> findByEmailUsuario(String email);
}

