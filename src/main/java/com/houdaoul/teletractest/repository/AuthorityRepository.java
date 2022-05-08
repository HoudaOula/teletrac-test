package com.houdaoul.teletractest.repository;

import com.houdaoul.teletractest.domain.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, String> {
}
