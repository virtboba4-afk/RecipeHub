package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class RecipeDetailsView implements Serializable {
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private int cookingTimeMinutes;
    private String difficulty;
    private String authorName;
    private List<String> categoryNames;
    private List<IngredientView> ingredients;
    private List<StepView> steps;
    private boolean favorite;
    private Double averageRating;
    private List<CommentView> comments;


    public RecipeDetailsView() {
    }

    public RecipeDetailsView(Long id,
                             String title,
                             String shortDescription,
                             String description,
                             int cookingTimeMinutes,
                             String difficulty,
                             String authorName,
                             List<String> categoryNames,
                             List<IngredientView> ingredients,
                             List<StepView> steps,
                             boolean favorite,
                             Double averageRating,
                             List<CommentView> comments) {

        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.difficulty = difficulty;
        this.authorName = authorName;
        this.categoryNames = categoryNames;
        this.ingredients = ingredients;
        this.steps = steps;
        this.favorite = favorite;
        this.averageRating = averageRating;
        this.comments = comments;
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

    public String getShortDescription() {     // 🔹 нужен для ${view.shortDescription}
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<CommentView> getComments() {
        return comments;
    }

    public void setComments(List<CommentView> comments) {
        this.comments = comments;
    }


}