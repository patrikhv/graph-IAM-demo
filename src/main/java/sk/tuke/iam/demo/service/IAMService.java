package sk.tuke.iam.demo.service;

import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;

import java.util.List;

public interface IAMService {
    void addUser(User user) throws Exception;
    void addRole(Role role) throws Exception;
    void removeUser(User user) throws Exception;
    void removeRole(Role role) throws Exception;

    void assignRole(User user, Role role) throws Exception;
    void denyRole(User user, Role role) throws Exception;

    User getUserByName(String userName) throws Exception;
    Role getRoleByName(String roleName) throws Exception;

    List<User> getAllUsers() throws Exception;
    List<Role> getAllRoles() throws Exception;

    List<User> getUsersWithRole(Role role) throws Exception;
    List<Role> getUserRoles(User user) throws Exception;
}
