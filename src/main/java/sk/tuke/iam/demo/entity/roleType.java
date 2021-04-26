package sk.tuke.iam.demo.entity;

public enum roleType {
    STUDENT,
    SUPERVISOR,
    TEACHER,
    OPPONENT,
    VISITOR;

    @Override
    public String toString() {
        return this.name();
    }
}
