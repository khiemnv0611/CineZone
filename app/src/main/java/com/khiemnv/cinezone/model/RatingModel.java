package com.khiemnv.cinezone.model;

public class RatingModel {
    private String ratingId;
    private String userId;
    private String movieId;
    private int ratingValue;  // Giá trị đánh giá (thường từ 1 đến 5 sao)
    private long createdAt;

    // Constructor
    public RatingModel() {}

    public RatingModel(String ratingId, String userId, String movieId, int ratingValue, long createdAt) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.movieId = movieId;
        this.ratingValue = ratingValue;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
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

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

