package sample.models;

import javax.management.relation.Role;

public class RoleHolder extends Holder {
    public String role;
    private static RoleHolder INSTANCE = new RoleHolder();

    private RoleHolder() {}

    public static RoleHolder getInstance() {
        return INSTANCE;
    }

    public void set(String u) {
        this.role = u;
    }

    public String get() {
        return this.role;
    }
}
