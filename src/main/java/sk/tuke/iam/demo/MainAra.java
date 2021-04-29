package sk.tuke.iam.demo;

import sk.tuke.iam.demo.entity.Role;
import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.entity.RoleType;
import sk.tuke.iam.demo.service.IAMServiceArango;

public class MainAra {
    public static void main(String[] args) throws Exception {
        try(IAMServiceArango iamService = new IAMServiceArango()){
            iamService.addUser(new User("Patrik"));
            iamService.addRole(new Role(RoleType.STUDENT));
            iamService.addUser(new User("Igor"));
            iamService.addRole(new Role(RoleType.SUPERVISOR));
            iamService.assignRole(new User("Patrik"),new Role(RoleType.STUDENT));
            iamService.assignRole(new User("Igor"), new Role(RoleType.SUPERVISOR));
            iamService.removeUser(new User("Igor"));
            iamService.removeRole(new Role(RoleType.SUPERVISOR));
            iamService.denyRole(new User("Patrik"), new Role(RoleType.STUDENT));
            User test = iamService.getUserByName("Patrik");

            iamService.getAllUsers().forEach(user -> System.out.println(user.toString()));
            iamService.getAllRoles().forEach(role -> System.out.println(role.toString()));
        }
    }
}
