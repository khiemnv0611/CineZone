package com.khiemnv.cinezone.model;

public class HistoryModel {
    private String historyId;
    private String userId;
    private String movieId;
    private long watchedAt;

    // Constructor
    public HistoryModel() {}

    public HistoryModel(String historyId, String userId, String movieId, long watchedAt) {
        this.historyId = historyId;
        this.userId = userId;
        this.movieId = movieId;
        this.watchedAt = watchedAt;
    }

    // Getters and Setters
    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
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

    public long getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(long watchedAt) {
        this.watchedAt = watchedAt;
    }
}
