package com.khiemnv.cinezone.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;
    private String avatarUrl;
    private long createdAt;
    private List<String> favoriteMovieIds;

    // Constructor
    public UserModel() {
    }

    public UserModel(String firstName, String lastName, String email, String password, boolean isAdmin, String avatarUrl) {
        this.userId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.avatarUrl = (avatarUrl == null || avatarUrl.isEmpty()) ? getRandomAvatar() : avatarUrl;
        this.createdAt = System.currentTimeMillis();
        this.favoriteMovieIds = new ArrayList<>();
    }

    private String getRandomAvatar() {
        List<String> avatarUrls = Arrays.asList(
                "https://mir-s3-cdn-cf.behance.net/project_modules/disp/84c20033850498.56ba69ac290ea.png",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvW15m_SY-oEMjBgNHZ5LnTbXtRnffH09u2w&s",
                "https://wallpapers.com/images/featured/netflix-profile-pictures-w3lqr61qe57e9yt8.jpg",
                "https://mir-s3-cdn-cf.behance.net/project_modules/disp/64623a33850498.56ba69ac2a6f7.png",
                "https://i.pinimg.com/736x/92/b4/e7/92b4e7c57de1b5e1e8c5e883fd915450.jpg",
                "https://i.pinimg.com/564x/a4/c6/5f/a4c65f709d4c0cb1b4329c12beb9cd78.jpg"
        );
        return avatarUrls.get(new Random().nextInt(avatarUrls.size()));
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatartUrl) {
        this.avatarUrl = avatartUrl;
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