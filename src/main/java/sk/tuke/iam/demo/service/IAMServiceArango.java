package sk.tuke.iam.demo.service;

import com.arangodb.*;
import com.arangodb.entity.*;
import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IAMServiceArango implements IAMService, AutoCloseable{

    public final ArangoDB arangoDB;
    public final ArangoDatabase db;
    public ArangoGraph graph;
    public GraphEntity graphEntity;

    public IAMServiceArango(){
        arangoDB = new ArangoDB.Builder()
                .user("root")
                .password("qetuop")
                .build();
        if (arangoDB.db("iam").exists())
            arangoDB.db("iam").drop();
        arangoDB.createDatabase("iam");
        db = arangoDB.db("iam");
        setupGraph();

    }

    public void setupGraph(){
        graphEntity = db.createGraph("IdentityManagementDemo1",null);
        graph = db.graph("IdentityManagementDemo1");
        graph.addVertexCollection("user");
        graph.addVertexCollection("role");
        EdgeDefinition edgeDefinition = new EdgeDefinition();
        edgeDefinition.collection("IS_MEMBER_OF").from("user").to("role");
        graph.addEdgeDefinition(edgeDefinition);
    }

    @Override
    public void addUser(User user) throws Exception {
        try {
            String query = "INSERT {_key: @key, user_name: @user_name} INTO user";
            Map<String, Object> bindVars = new HashMap<>();
            bindVars.put("key", user.getUserName().toLowerCase());
            bindVars.put("user_name", user.getUserName());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    @Override
    public void addRole(Role role) throws Exception {
        try {
            String query = "INSERT {_key: @key ,role_name: @role_name} INTO role";
            Map<String, Object> bindVars = new HashMap<>();
            bindVars.put("key", role.getRoleType().toString().toLowerCase());
            bindVars.put("role_name", role.getRoleType().toString());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    @Override
    public void removeUser(User user) throws Exception {
        // TODO delete all edges from user
        try {
            String query = "REMOVE {_key: @key} IN user";
            Map<String, Object> bindVars = Collections.singletonMap("key", user.getUserName().toLowerCase());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }

    }

    @Override
    public void removeRole(Role role) throws Exception {
        // TODO delete all edges to role
        // druhy sposob - podla role_name
        try {
            String query = "REMOVE {_key: @key} IN role";
            Map<String, Object> bindVars = Collections.singletonMap("key", role.getRoleType().toString().toLowerCase());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    @Override
    public void assignRole(User user, Role role) throws Exception {
        try {
            String query = "INSERT {_from: @from ,_to: @to} INTO IS_MEMBER_OF";
            Map<String, Object> bindVars = new HashMap<>();
            bindVars.put("from", "user/" + user.getUserName().toLowerCase());
            bindVars.put("to", "role/" + role.getRoleType().toString().toLowerCase());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("4Failed to execute query. " + e.getMessage());
        }
    }

    @Override
    public void denyRole(User user, Role role) throws Exception {
        try {
            String query = "REMOVE {_from: @from ,_to: @to} IN IS_MEMBER_OF";
            Map<String, Object> bindVars = new HashMap<>();
            bindVars.put("from", "user/" + user.getUserName().toLowerCase());
            bindVars.put("to", "role/" + role.getRoleType().toString().toLowerCase());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                System.out.println("Key: " + aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("5Failed to execute query. " + e.getMessage());
        }
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
        arangoDB.shutdown();
    }

    private String getUserRoleId(){
        return "";
    }
}
