package sk.tuke.iam.demo.service;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.entity.roleType;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class IAMServiceNeo4J implements IAMService, AutoCloseable{

    private final Driver driver;

    public IAMServiceNeo4J(String uri, String user, String password ) throws Exception{
        this.driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void addUser(User user) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("CREATE (u:User {user_name:$user_name})",
                        parameters("user_name",user.getUserName()));
                return null;
            });
        }
    }

    @Override
    public void addRole(Role role) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("CREATE (r:Role {role_name:$role_name})",
                        parameters("role_name", role.getRoleType().toString()));
                return null;
            });
        }
    }

    @Override
    public void removeUser(User user) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("MATCH (u:Person {user_name:$user_name})" +
                                   "DETACH DELETE u",
                        parameters("user_name",user.getUserName()));
                return null;
            });
        }
    }

    @Override
    public void removeRole(Role role) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("MATCH (r:Role {role_name:$role_name})" +
                                   "DETACH DELETE r",
                        parameters("role_name",role.getRoleType().toString()));
                return null;
            });
        }
    }

    @Override
    public void assignRole(User user, Role role) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run( "MATCH (p:User {user_name: $user_name}) " +
                                "MATCH (r:Role {role_name: $role_name}) " +
                                "CREATE (p)-[:IS_MEMBER_OF]->(r)",
                        parameters( "user_name", user.getUserName(),
                                "role_name", role.getRoleType().toString() ) );
                return null;
            });
        }
    }

    @Override
    public void denyRole(User user, Role role) throws Exception{
        try (Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) transaction -> {
                transaction.run("MATCH (u:User {user_name:$user_name})-[rel:IS_MEMBER_OF]->" +
                                "(r:Role {role_name:$role_name})" +
                                "DELETE rel",
                        parameters("user_name",user.getUserName(),"role_name",role.getRoleType().toString()));
                return null;
            });
        }
    }

    @Override
    public User getUserByName(String userName) throws Exception{
        Record record = null;
        try (Session session = driver.session()) {
            record = session.readTransaction(transaction -> {
                Result result =  transaction.run("MATCH (u:User {user_name:$user_name})" +
                                "RETURN u.user_name",
                        parameters("user_name", userName));
                return result.single();
            });

        }
        if (record != null){
            return new User(record.get("u.user_name").asString());
        }
        return null;
    }

    @Override
    public Role getRoleByName(String roleName) throws Exception{
        Record record = null;
        try (Session session = driver.session()) {
            record = session.readTransaction(transaction -> {
                Result result =  transaction.run("MATCH (r:Role {role_name:$role_name})" +
                                "RETURN r.role_name",
                        parameters("role_name", roleName));
                return result.single();
            });

        }
        if (record != null){
            return new Role(roleType.valueOf(record.get("r.role_name").asString()));
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws Exception{
        List<Record> record = null;
        List<User> usersList = new ArrayList<>();
        try (Session session = driver.session()) {
            record = session.readTransaction(transaction -> {
                Result result =  transaction.run("MATCH (u:User)" +
                                "RETURN u.user_name");
                return result.list();
            });

        }
        if (record != null){
            for (Record user : record){
                usersList.add(new User(user.get("u.user_name").asString()));
            }
        }
        return usersList;
    }

    @Override
    public List<Role> getAllRoles() throws Exception{
        List<Record> record = null;
        List<Role> rolesList = new ArrayList<>();
        try (Session session = driver.session()) {
            record = session.readTransaction(transaction -> {
                Result result =  transaction.run("MATCH (r:Role)" +
                        "RETURN r.role_name");
                return result.list();
            });

        }
        if (record != null){
            for (Record role : record){
                rolesList.add(new Role(roleType.valueOf(role.get("role_name").asString())));
            }
        }
        return rolesList;
    }

    @Override
    public List<User> getUsersWithRole(Role role) throws Exception{
        return null;
    }

    @Override
    public List<Role> getUserRoles(User user) throws Exception{
        return null;
    }

    @Override
    public void close() throws Exception {
        this.driver.close();
    }


}
