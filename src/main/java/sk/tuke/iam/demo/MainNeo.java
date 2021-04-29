package sk.tuke.iam.demo;

import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.entity.RoleType;
import sk.tuke.iam.demo.service.IAMServiceNeo4J;

public class MainNeo {
    public static void main( String... args ) throws Exception
    {
        try ( IAMServiceNeo4J db = new IAMServiceNeo4J( "bolt://localhost:7687", "neo4j", "qetuop" ) )
        {
            db.addUser(new User("Patrik"));
            db.addRole(new Role(RoleType.STUDENT));
            db.assignRole(db.getUserByName("Patrik"), db.getRoleByName(RoleType.STUDENT.toString()));

            db.addUser(new User("Igor"));
            db.addRole(new Role(RoleType.SUPERVISOR));
            db.assignRole(db.getUserByName("Igor"), db.getRoleByName(RoleType.SUPERVISOR.toString()));

            db.addUser(new User("Tomas"));
            db.assignRole(db.getUserByName("Tomas"), db.getRoleByName(RoleType.SUPERVISOR.toString()));
            db.assignRole(db.getUserByName("Tomas"), db.getRoleByName(RoleType.STUDENT.toString()));

            db.denyRole(db.getUserByName("Tomas"),db.getRoleByName(RoleType.STUDENT.toString()));

            db.removeRole(db.getRoleByName(RoleType.STUDENT.toString()));

            db.getAllUsers().forEach(user -> System.out.println(user.toString()));
            db.getAllRoles().forEach(role -> System.out.println(role.toString()));


        }
    }
}
