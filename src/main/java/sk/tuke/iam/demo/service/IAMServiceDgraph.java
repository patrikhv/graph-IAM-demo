package sk.tuke.iam.demo.service;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import io.dgraph.*;
import io.dgraph.DgraphProto.Operation;
import io.dgraph.DgraphProto.Mutation;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;

import java.util.List;

public class IAMServiceDgraph implements IAMService, AutoCloseable{

    final DgraphClient dgraphClient;

    public IAMServiceDgraph() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9080)
                .usePlaintext().build();
        DgraphGrpc.DgraphStub stub1 = DgraphGrpc.newStub(channel);

        this.dgraphClient = new DgraphClient(stub1);
        System.out.println(dgraphClient.checkVersion());
        //dgraphClient.alter(Operation.newBuilder().setDropAll(true).build());

        //setSchema();
    }

    private void setSchema(){
        String schema = "userName: string @index(term) .\n" +
                        "roleName: string @index(term) .\n" +
                        "\ntype User {\nuserName\n}\n" +
                        "\ntype Role {\nroleName\n}\n";
        Operation operation = Operation.newBuilder().setSchema(schema).build();
        dgraphClient.alter(operation);

    }

    @Override
    public void addUser(User user) throws Exception {
        Transaction txn = dgraphClient.newTransaction();

        try {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            Mutation mutation =
                    Mutation.newBuilder().setSetJson(ByteString.copyFromUtf8(json.toString())).build();
            txn.mutate(mutation);
            txn.commit();
        } catch (TxnConflictException ex) {
            // Retry or handle exception.
        } finally {
            txn.discard();
        }
    }

    @Override
    public void addRole(Role role) throws Exception {
        Transaction txn = dgraphClient.newTransaction();

        try {
            Gson gson = new Gson();
            String json = gson.toJson(role);
            Mutation mutation =
                    Mutation.newBuilder().setSetJson(ByteString.copyFromUtf8(json.toString())).build();
            txn.mutate(mutation);
            txn.commit();
        } catch (TxnConflictException ex) {
            // Retry or handle exception.
        } finally {
            txn.discard();
        }
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
