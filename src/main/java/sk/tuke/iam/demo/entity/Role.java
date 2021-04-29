package sk.tuke.iam.demo.entity;

public class Role {
    private RoleType roleType;


    public Role(RoleType roleType) {
        this.roleType = roleType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleType=" + roleType +
                '}';
    }
}
