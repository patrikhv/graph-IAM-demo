package sk.tuke.iam.demo;

import java.util.Date;

public class Role {
    private String roleName;
    private Date dateCreated;


    public Role(String name) {
        this.roleName = name;
        this.dateCreated = new Date();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
