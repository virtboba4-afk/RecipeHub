package com.example.recipehub.views;

import java.io.Serializable;

public class RecipeFilterView implements Serializable {
    private String queryText;
    private Integer categoryId;
    private Integer maxCookingTimeMinutes;
    private String difficulty;

    public RecipeFilterView() {
    }

    public RecipeFilterView(String queryText,
                            Integer categoryId,
                            Integer maxCookingTimeMinutes,
                            String difficulty) {
        this.queryText = queryText;
        this.categoryId = categoryId;
        this.maxCookingTimeMinutes = maxCookingTimeMinutes;
        this.difficulty = difficulty;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getMaxCookingTimeMinutes() {
        return maxCookingTimeMinutes;
    }

    public void setMaxCookingTimeMinutes(Integer maxCookingTimeMinutes) {
        this.maxCookingTimeMinutes = maxCookingTimeMinutes;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
