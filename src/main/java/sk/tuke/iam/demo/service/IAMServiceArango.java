package sk.tuke.iam.demo.service;

import com.arangodb.*;
import com.arangodb.entity.*;
import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.entity.roleType;

import java.util.*;

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
            System.err.println("Failed to execute query. " + e.getMessage());
        }
    }

    @Override
    public void denyRole(User user, Role role) throws Exception {
        String key = getUserRoleKey(user.getUserName(), role.getRoleType().toString());
        if (key.length() == 0){
            return;
        }
        try {
            String query = "REMOVE {_key: @key} IN IS_MEMBER_OF";
            Map<String, Object> bindVars = Collections.singletonMap("key",key);
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
        final User[] user = {null};
        try {
            String query = "FOR u IN user\n" +
                    "FILTER u.user_name == @user_name\n" +
                    "RETURN u";
            Map<String, Object> bindVars = Collections.singletonMap("user_name",userName);
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                //System.out.println("Key: " + aDocument.getProperties().get("user_name"));
                user[0] = new User(aDocument.getProperties().get("user_name").toString());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        return user[0];
    }

    @Override
    public Role getRoleByName(String roleName) throws Exception {
        final Role[] role = {null};
        try {
            String query = "FOR r IN role\n" +
                    "FILTER r.role_name == @role_name\n" +
                    "RETURN r";
            Map<String, Object> bindVars = Collections.singletonMap("role_name",roleName);
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                //System.out.println("Key: " + aDocument.getProperties().get("user_name"));
                role[0] = new Role(roleType.valueOf(aDocument.getProperties().get("role_name").toString()));
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        return role[0];
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            String query = "FOR u IN user\n" +
                            "RETURN u";
            //Map<String, Object> bindVars = Collections.singletonMap();
            ArangoCursor<BaseDocument> cursor = db.query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                //System.out.println("Key: " + aDocument.getProperties().get("user_name"));
                 users.add(new User(aDocument.getProperties().get("user_name").toString()));
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        return users;
    }

    @Override
    public List<Role> getAllRoles() throws Exception {
        final List<Role> roles = new ArrayList<>();
        try {
            String query = "FOR r IN role\n" +
                            "RETURN r";
            //Map<String, Object> bindVars = Collections.singletonMap("user_name",userName);
            ArangoCursor<BaseDocument> cursor = db.query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                //System.out.println("Key: " + aDocument.getProperties().get("user_name"));
                roles.add(new Role(roleType.valueOf(aDocument.getProperties().get("role_name").toString())));
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        return roles;
    }

    @Override
    public List<User> getUsersWithRole(Role role) throws Exception {
        //TODO
        return null;
    }

    @Override
    public List<Role> getUserRoles(User user) throws Exception {
        //TODO
        return null;
    }

    @Override
    public void close() throws Exception {
        arangoDB.shutdown();
    }

    private String getUserRoleKey(String userName, String roleName){
        List<String> relIds = new ArrayList<>();
        try {
            String query = "FOR rel IN IS_MEMBER_OF\n" +
                    "    FOR u IN user\n" +
                    "        FOR r IN role\n" +
                    "            FILTER u._key == @user_name AND r._key == @role_name\n" +
                    "            AND rel._from == u._id AND rel._to == r._id\n" +
                    "            RETURN rel";
            Map<String, Object> bindVars = new HashMap<>();
            bindVars.put("user_name",userName.toLowerCase());
            bindVars.put("role_name",roleName.toLowerCase());
            ArangoCursor<BaseDocument> cursor = db.query(query, bindVars, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                relIds.add(aDocument.getKey());
            });
        } catch (ArangoDBException e) {
            System.err.println("Failed to execute query. " + e.getMessage());
        }
        if (relIds.size() > 0){
            return relIds.get(0);
        }
        return "";
    }
}
