package com.fsb.oussama_store.authentication.dao;

import com.fsb.oussama_store.authentication.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {
}
