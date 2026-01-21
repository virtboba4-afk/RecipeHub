package com.example.recipehub.dto;

import com.example.recipehub.models.enums.Difficulty;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class AddRecipeDto implements Serializable {

    @NotBlank(message = "Введите название рецепта")
    private String title;

    @NotBlank(message = "Введите краткое описание")
    private String shortDescription;

    @NotBlank(message = "Введите полное описание")
    private String description;

    @NotNull(message = "Укажите время приготовления")
    @Min(value = 1, message = "Время приготовления должно быть больше 0")
    private Integer cookingTimeMinutes;

    @NotNull(message = "Выберите сложность")
    private Difficulty difficulty;

    @NotEmpty(message = "Выберите хотя бы одну категорию")
    private List<Integer> categoryIds;

    private List<AddIngredientDto> ingredients;
    private List<AddStepDto> steps;

    public AddRecipeDto() {
        this.ingredients = new java.util.ArrayList<>();
        this.steps = new java.util.ArrayList<>();

        this.ingredients.add(new AddIngredientDto());
        this.steps.add(new AddStepDto());
    }

    public AddRecipeDto(String title,
                        String shortDescription,
                        String description,
                        Integer cookingTimeMinutes,
                        Difficulty difficulty,
                        List<Integer> categoryIds,
                        List<AddIngredientDto> ingredients,
                        List<AddStepDto> steps) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.difficulty = difficulty;
        this.categoryIds = categoryIds;
        this.ingredients = (ingredients != null) ? ingredients : new java.util.ArrayList<>();
        this.steps = (steps != null) ? steps : new java.util.ArrayList<>();
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<AddIngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AddIngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<AddStepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<AddStepDto> steps) {
        this.steps = steps;
    }
}
