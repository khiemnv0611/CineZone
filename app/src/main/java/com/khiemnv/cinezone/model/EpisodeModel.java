package com.khiemnv.cinezone.model;

import java.util.UUID;

public class EpisodeModel {
    private String episodeId;
    private String title;
    private int season;
    private int episodeNumber;
    private String description;
    private int duration;
    private String releaseDate;
    private String videoUrl;
    private String thumbnailUrl;

    // Constructor
    public EpisodeModel() {
    }

    public EpisodeModel(String title, int season, int episodeNumber, String description, int duration,
                        String releaseDate, String videoUrl, String thumbnailUrl) {
        this.episodeId = UUID.randomUUID().toString();
        this.title = title;
        this.season = season;
        this.episodeNumber = episodeNumber;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getter và Setter
    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
