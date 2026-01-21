package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class RecipeCardView implements Serializable {
    private Long id;
    private String title;
    private String shortDescription;
    private int cookingTimeMinutes;
    private String difficulty;
    private List<String> categoryNames;
    private Double averageRating;

    public RecipeCardView() {
    }

    public RecipeCardView(Long id,
                          String title,
                          String shortDescription,
                          int cookingTimeMinutes,
                          String difficulty,
                          List<String> categoryNames){
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.difficulty = difficulty;
        this.categoryNames = categoryNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getCookingTimeMinutes() {
        return cookingTimeMinutes;
    }

    public void setCookingTimeMinutes(int cookingTimeMinutes) {
        this.cookingTimeMinutes = cookingTimeMinutes;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
