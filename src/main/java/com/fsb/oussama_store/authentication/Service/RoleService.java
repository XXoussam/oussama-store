package com.fsb.oussama_store.authentication.Service;

import com.fsb.oussama_store.authentication.dao.RoleDao;
import com.fsb.oussama_store.authentication.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
}
