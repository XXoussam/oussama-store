package com.fsb.oussama_store.authentication.Service;

import com.fsb.oussama_store.authentication.dao.RoleDao;
import com.fsb.oussama_store.authentication.dao.UserDao;
import com.fsb.oussama_store.authentication.entity.Role;
import com.fsb.oussama_store.authentication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerNewUser(User user) {
        Role role = roleDao.findById("Client").get();
        System.out.println(role);
        Set roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        return userDao.save(user);
    }

    public void deleteUser(String id) {
        userDao.deleteById(id);
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return username;
        } else {
            String username = principal.toString();
            return username;
        }
    }

    public void initRolesAndUser() {
        /*Role ownerRole=new Role();
        ownerRole.setRoleName("Owner");
        ownerRole.setRoleDescription("owner of the store");
        roleDao.save(ownerRole);

        Role clientRole=new Role();
        clientRole.setRoleName("Client");
        clientRole.setRoleDescription("simple client");
        roleDao.save(clientRole);

        User adminUser = new User();
        adminUser.setUserName("oussa13@gmail.com");
        adminUser.setName("oussama");
        adminUser.setUserPassword(getEncodedPassword("oussami2923"));
        Set<Role> ownerRoles = new HashSet<>();
        ownerRoles.add(ownerRole);
        adminUser.setRoles(ownerRoles);
        userDao.save(adminUser);

        User user = new User();
        user.setName("raj");
        user.setUserName("raj123@gmail.com");
        user.setUserPassword(getEncodedPassword("raj@pass"));
        Set<Role> clientRoles = new HashSet<>();
        clientRoles.add(clientRole);
        user.setRoles(clientRoles);
        userDao.save(user);

        User user1 = new User();
        user1.setName("amina");
        user1.setUserName("ami23@gmail.com");
        user1.setUserPassword(getEncodedPassword("ami24njrode44"));
        Set<Role> clientRoles1 = new HashSet<>();
        clientRoles1.add(clientRole);
        user1.setRoles(clientRoles1);
        userDao.save(user1);*/


    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
