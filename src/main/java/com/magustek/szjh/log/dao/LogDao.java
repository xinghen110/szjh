package com.magustek.szjh.log.dao;

import com.magustek.szjh.log.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface LogDao extends CrudRepository<LogEntity, String> {//MongoRepository<LogEntity, String> {

    Page<LogEntity> findAllByTimeBetweenAndLevelContainingAndMessageContainingOrderByTimeDesc(String start, String end, String level, String message, Pageable pageable);
}
