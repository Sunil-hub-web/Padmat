package com.example.foodhub.model;

public class SliderItem {

    private String description,imageUrl,id;

    public SliderItem(String id, String description, String imageUrl) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public SliderItem() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SliderItem{" +
                "description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
