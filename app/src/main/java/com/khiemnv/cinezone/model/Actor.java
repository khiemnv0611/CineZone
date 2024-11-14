package com.khiemnv.cinezone.model;

import java.util.UUID;

public class Actor {
    private String id;
    private String name;
    private String avatarUrl;

    // Constructor
    public Actor() {
    }
    
    public Actor(String name, String avatarUrl) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Các getter và setter khác
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
