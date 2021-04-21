package sk.tuke.iam.demo;

public class Main {
    public static void main( String... args ) throws Exception
    {
        try ( iamServiceNeo4j db = new iamServiceNeo4j( "bolt://localhost:7687", "neo4j", "qetuop" ) )
        {
            User user = new User("Patrik",23);
            Role role = new Role("student");
            db.addUser(user);
            db.addRole(role);
            db.assignRole(user,role);
        }
    }
}
