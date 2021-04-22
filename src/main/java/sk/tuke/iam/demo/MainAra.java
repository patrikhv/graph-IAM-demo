package sk.tuke.iam.demo;

import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.service.IAMService;
import sk.tuke.iam.demo.service.IAMServiceArango;

public class MainAra {
    public static void main(String[] args) throws Exception {
        try(IAMServiceArango iamService = new IAMServiceArango()){
            iamService.addUser(new User("Patrik"));
            System.out.println(iamService.getUserByName("322930"));
        }
    }
}
