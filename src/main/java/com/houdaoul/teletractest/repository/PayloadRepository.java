package com.houdaoul.teletractest.repository;

import com.houdaoul.teletractest.domain.Payload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayloadRepository extends CrudRepository<Payload, Long> {
}
