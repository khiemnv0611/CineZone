package com.khiemnv.cinezone.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MovieModel {
    private String movieId;
    private String title;
    private List<String> genre;
    private String type;
    private int ageRating;
    private String status;
    private String description;
    private String imageUrl;
    private String videoUrl;
    private String trailerUrl;
    private List<String> productionCompanies;
    private List<String> directors;
    private String country;
    private String season;
    private Date releaseDate;
    private int duration;
    private int viewCount;
    private double averageRating;
    private int totalRatings;
    private List<Actor> actors;
    private boolean isSeries;
    private int totalEpisodes;
    private List<String> episodeIds;

    // Constructor
    public MovieModel() {
    }

    public MovieModel(String title, List<String> genre, String type, int ageRating, String status, String description,
                      String imageUrl, String videoUrl, String trailerUrl, String season, String country, List<String> productionCompanies,
                      List<String> directors, Date releaseDate, int duration, int viewCount, double averageRating, int totalRatings, List<Actor> actors,
                      boolean isSeries, int totalEpisodes, List<String> episodeIds) {
        this.movieId = UUID.randomUUID().toString();
        this.title = title;
        this.genre = genre;
        this.type = type;
        this.ageRating = ageRating;
        this.status = status;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.trailerUrl = trailerUrl;
        this.season = season;
        this.directors = directors;
        this.productionCompanies = productionCompanies;
        this.country = country;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.viewCount = viewCount;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.actors = actors;
        this.isSeries = isSeries;
        this.totalEpisodes = totalEpisodes;
        this.episodeIds = episodeIds;
    }

    // Getters and Setters

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(int ageRating) {
        this.ageRating = ageRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<String> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public boolean getIsSeries() {
        return isSeries;
    }

    public void setIsSeries(boolean isSeries) {
        this.isSeries = isSeries;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public List<String> getEpisodeIds() {
        return episodeIds;
    }

    public void setEpisodeIds(List<String> episodeIds) {
        this.episodeIds = episodeIds;
    }
}