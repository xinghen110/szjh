package com.magustek.szjh.log.dao;

import com.magustek.szjh.log.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogDao extends MongoRepository<LogEntity, String> {

    Page<LogEntity> findAllByTimeBetweenAndLevelContainingAndMessageContainingOrderByTimeDesc(String start, String end, String level, String message, Pageable pageable);
}
