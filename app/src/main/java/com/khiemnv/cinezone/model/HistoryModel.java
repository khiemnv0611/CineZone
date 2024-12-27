package com.khiemnv.cinezone.model;

public class HistoryModel {
    private String historyId;
    private String email;
    private String movieId;
    private long watchedAt;

    // Constructor
    public HistoryModel() {
    }

    public HistoryModel(String historyId, String email, String movieId, long watchedAt) {
        this.historyId = historyId;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
