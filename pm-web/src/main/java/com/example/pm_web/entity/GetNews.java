package com.example.pm_web.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetNews {
    private String title;
    @JsonProperty("raw_description")
    private String rawDescription;
    private String description;
    private String summary;
    @JsonProperty("publication_date")
    private String publicationDate;
    private String url;
    private String sentiment;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getRawDescription() {
        return rawDescription;
    }
    public void setRawDescription(String rawDescription) {
        this.rawDescription = rawDescription;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSentiment() {
        return sentiment;
    }
    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    // Getters and Setters
    
}