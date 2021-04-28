package sk.tuke.iam.demo;

import sk.tuke.iam.demo.entity.User;
import sk.tuke.iam.demo.service.IAMServiceDgraph;

public class MainDgraph {
    public static void main(String[] args) throws Exception{
        try (IAMServiceDgraph iamService = new IAMServiceDgraph()){
            iamService.addUser(new User("Patrik"));
        }
    }
}
