package com.khiemnv.cinezone.model;

public class CommentModel {
    private String commentId;
    private String userId;
    private String movieId;
    private String username;
    private String comment;
    private String imageUrl;
    private long createdAt;

    // Constructor
    public CommentModel() {}

    public CommentModel(String commentId, String userId, String movieId, String username, String comment, String imageUrl, long createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.movieId = movieId;
        this.username = username;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

