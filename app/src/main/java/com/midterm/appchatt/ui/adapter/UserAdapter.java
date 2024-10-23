package com.midterm.appchatt.ui.adapter;

import com.midterm.appchatt.model.User;

public class UserAdapter {
    private User user;

    public UserAdapter(User user) {
        this.user = user;
    }

    // Return the user who logged in.
    public static UserAdapter getCurrentUser() {
        return new UserAdapter(new User("", "antialberteinstein@gmail.com", "Nguyen"));
    }

    public static UserAdapter getUser(String id) {
        return new UserAdapter(new User(id, "antialberteinstein@gmail.com", "Nguyen"));
    }

    public User getUser() {
        return user;
    }
}
