package sk.tuke.iam.demo.entity;

import java.util.Date;

public class Role {
    private roleType roleType;


    public Role(roleType roleType) {
        this.roleType = roleType;
    }

    public sk.tuke.iam.demo.entity.roleType getRoleType() {
        return roleType;
    }

    public void setRoleType(sk.tuke.iam.demo.entity.roleType roleType) {
        this.roleType = roleType;
    }
}
