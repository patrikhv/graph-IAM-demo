package sk.tuke.iam.demo.service;

import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;

import java.util.List;

public class IAMServiceDgraph implements IAMService, AutoCloseable{

    @Override
    public void addUser(User user) throws Exception {

    }

    @Override
    public void addRole(Role role) throws Exception {

    }

    @Override
    public void removeUser(User user) throws Exception {

    }

    @Override
    public void removeRole(Role role) throws Exception {

    }

    @Override
    public void assignRole(User user, Role role) throws Exception {

    }

    @Override
    public void denyRole(User user, Role role) throws Exception {

    }

    @Override
    public User getUserByName(String userName) throws Exception {
        return null;
    }

    @Override
    public Role getRoleByName(String roleName) throws Exception {
        return null;
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        return null;
    }

    @Override
    public List<Role> getAllRoles() throws Exception {
        return null;
    }

    @Override
    public List<User> getUsersWithRole(Role role) throws Exception {
        return null;
    }

    @Override
    public List<Role> getUserRoles(User user) throws Exception {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
