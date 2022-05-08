package com.houdaoul.teletractest.repository;

import com.houdaoul.teletractest.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
