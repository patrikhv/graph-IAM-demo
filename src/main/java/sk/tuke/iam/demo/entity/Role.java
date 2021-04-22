package sk.tuke.iam.demo.entity;

import java.util.Date;

public class Role {
    private String roleName;


    public Role(String name) {
        this.roleName = name;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
