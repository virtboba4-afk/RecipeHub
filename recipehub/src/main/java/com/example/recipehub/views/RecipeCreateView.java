package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class RecipeCreateView implements Serializable {

    private String title;
    private String shortDescription;
    private String description;
    private Integer cookingTimeMinutes;
    private String difficulty;
    private List<Integer> selectedCategoryIds;

    private List<CategoryView> availableCategories;
    private List<IngredientView> ingredients;
    private List<StepView> steps;

    public RecipeCreateView() {
    }

    public RecipeCreateView(String title,
                            String shortDescription,
                            String description,
                            Integer cookingTimeMinutes,
                            String difficulty,
                            List<Integer> selectedCategoryIds,
                            List<CategoryView> availableCategories,
                            List<IngredientView> ingredients,
                            List<StepView> steps) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.difficulty = difficulty;
        this.selectedCategoryIds = selectedCategoryIds;
        this.availableCategories = availableCategories;
        this.ingredients = ingredients;
        this.steps = steps;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCookingTimeMinutes() {
        return cookingTimeMinutes;
    }

    public void setCookingTimeMinutes(Integer cookingTimeMinutes) {
        this.cookingTimeMinutes = cookingTimeMinutes;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Integer> getSelectedCategoryIds() {
        return selectedCategoryIds;
    }

    public void setSelectedCategoryIds(List<Integer> selectedCategoryIds) {
        this.selectedCategoryIds = selectedCategoryIds;
    }

    public List<CategoryView> getAvailableCategories() {
        return availableCategories;
    }

    public void setAvailableCategories(List<CategoryView> availableCategories) {
        this.availableCategories = availableCategories;
    }

    public List<IngredientView> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientView> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepView> getSteps() {
        return steps;
    }

    public void setSteps(List<StepView> steps) {
        this.steps = steps;
    }
}
