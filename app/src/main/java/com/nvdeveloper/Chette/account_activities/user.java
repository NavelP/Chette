package com.nvdeveloper.Chette.account_activities;

public class user {
    String email;
    String username;

    public user(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public user() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
