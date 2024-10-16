package com.midterm.appchatt;

public class User {
    private String username;
    private String email;
    private String profile_picture;

    // Constructor, getters and setters
    public User() {}  // Default constructor

    public User(String username, String email, String profile_picture) {
        this.username = username;
        this.email = email;
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }
}
