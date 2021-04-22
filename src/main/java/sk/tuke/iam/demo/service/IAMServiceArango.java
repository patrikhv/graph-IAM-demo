package sk.tuke.iam.demo.service;

import com.arangodb.*;
import com.arangodb.entity.*;
import com.arangodb.model.EdgeCreateOptions;
import com.arangodb.model.VertexCreateOptions;
import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;

import java.util.List;

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
        graph.vertexCollection("user").insertVertex(user);
    }

    @Override
    public void addRole(Role role) throws Exception {
        graph.vertexCollection("role").insertVertex(role);
    }

    @Override
    public void removeUser(User user) throws Exception {
    }

    @Override
    public void removeRole(Role role) throws Exception {

    }

    @Override
    public void assignRole(User user, Role role) throws Exception {
        //graph.edgeCollection("IS_MEMBER_OF").insertEdge();
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
        arangoDB.shutdown();
    }
}
