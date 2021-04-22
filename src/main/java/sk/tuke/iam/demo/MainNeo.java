package sk.tuke.iam.demo;

import sk.tuke.iam.demo.service.IAMServiceNeo4J;

public class MainNeo {
    public static void main( String... args ) throws Exception
    {
        try ( IAMServiceNeo4J db = new IAMServiceNeo4J( "bolt://localhost:7687", "neo4j", "qetuop" ) )
        {
            /*db.addUser(new User("Patrik"));
            db.addRole(new Role("student"));
            db.assignRole(db.getUserByName("Patrik"), db.getRoleByName("student"));

            db.addUser(new User("Igor"));
            db.addRole(new Role("mentor"));
            db.assignRole(db.getUserByName("Igor"), db.getRoleByName("mentor"));*/

            /*db.addUser(new User("Tomas"));
            db.assignRole(db.getUserByName("Tomas"), db.getRoleByName("mentor"));
            db.assignRole(db.getUserByName("Tomas"), db.getRoleByName("student"));*/

            //db.denyRole(db.getUserByName("Tomas"),db.getRoleByName("student"));

            //db.removeRole(db.getRoleByName("student"));
            System.out.println(db.getAllUsers());
            System.out.println(db.getAllRoles());


        }
    }
}
