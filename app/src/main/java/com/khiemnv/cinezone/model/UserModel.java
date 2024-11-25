package com.khiemnv.cinezone.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;
    private long createdAt;
    private List<String> favoriteMovieIds;

    // Constructor
    public UserModel() {
    }

    public UserModel(String firstName, String lastName, String email, String password, boolean isAdmin) {
        this.userId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdAt = System.currentTimeMillis();
        this.favoriteMovieIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getFavoriteMovieIds() {
        return favoriteMovieIds;
    }

    public void setFavoriteMovieIds(List<String> favoriteMovieIds) {
        this.favoriteMovieIds = favoriteMovieIds;
    }
}