package sample.models;

import javafx.collections.ObservableList;

public final class UserHolder extends Holder {
    public User us;
    private  static UserHolder INSTANCE = new UserHolder();

    private UserHolder() {}

    public static UserHolder getInstance() {
        return INSTANCE;
    }

    public void set(User u) {
        this.us = u;
    }

    public User get() {
        return this.us;
    }
}
