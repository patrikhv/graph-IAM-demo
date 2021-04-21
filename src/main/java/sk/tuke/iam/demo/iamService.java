package sk.tuke.iam.demo;

import java.util.List;

public interface iamService {
    void addUser(User user);
    void addRole(Role role);
    void removeUser(User user);
    void denyRole(Role role);

    void assignRole(User user, Role role);
    void denyRole(User user, Role role);

    User getUserByName(String userName);
    Role getRoleByName(String roleName);

    List<User> getAllUsers();
    List<Role> getAllRoles();

    List<User> getUsersWithRole(Role role);
    List<Role> getUserRoles(User user);
}
