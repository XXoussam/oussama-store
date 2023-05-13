package com.fsb.oussama_store.authentication.dao;

import com.fsb.oussama_store.authentication.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, String> {
}
