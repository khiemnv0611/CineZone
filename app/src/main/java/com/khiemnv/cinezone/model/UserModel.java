package com.khiemnv.cinezone.model;

import java.util.List;

public class UserModel {
    private String userId;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private long createdAt;
    private long updatedAt;
    private List<String> favoriteMovieIds;

    // Constructor
    public UserModel() {}

    public UserModel(String userId, String username, String email, String password, boolean isAdmin, long createdAt, long updatedAt, List<String> favoriteMovieIds) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.favoriteMovieIds = favoriteMovieIds;
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getFavoriteMovieIds() {
        return favoriteMovieIds;
    }

    public void setFavoriteMovieIds(List<String> favoriteMovieIds) {
        this.favoriteMovieIds = favoriteMovieIds;
    }
}