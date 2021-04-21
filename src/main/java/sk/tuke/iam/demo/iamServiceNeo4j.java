package sk.tuke.iam.demo;

import org.neo4j.driver.*;

import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class iamServiceNeo4j implements iamService, AutoCloseable{

    private final Driver driver;

    public iamServiceNeo4j( String uri, String user, String password ) {
        this.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void addUser(User user) {
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("CREATE (u:User {user_name:$user_name,age:$age})",
                        parameters("user_name",user.getUserName(),"age",user.getAge()));
                return null;
            });
        }
    }

    @Override
    public void addRole(Role role) {
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("CREATE (r:Role {role_name:$role_name,date_created:$date_created})",
                        parameters("role_name",role.getRoleName(),"date_created",role.getDateCreated().getTime()));
                return null;
            });
        }
    }

    @Override
    public void removeUser(User user) {

    }

    @Override
    public void denyRole(Role role) {

    }

    @Override
    public void assignRole(User user, Role role) {
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run( "MATCH (p:User {user_name: $user_name}) " +
                                "MATCH (r:Role {role_name: $role_name}) " +
                                "CREATE (p)-[:IS_MEMBER_OF]->(r)",
                        parameters( "user_name", user.getUserName(),
                                "role_name", role.getRoleName() ) );
                return null;
            });
        }
    }

    @Override
    public void denyRole(User user, Role role) {

    }

    @Override
    public User getUserByName(String userName) {
        return null;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    @Override
    public List<User> getUsersWithRole(Role role) {
        return null;
    }

    @Override
    public List<Role> getUserRoles(User user) {
        return null;
    }

    @Override
    public void close() throws Exception {
        this.driver.close();
    }


}
