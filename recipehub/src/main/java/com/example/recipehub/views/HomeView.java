package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class HomeView implements Serializable {
    private List<RecipeCardView> popularRecipes;
    private List<RecipeCardView> latestRecipes;
    private List<CategoryView> categories;

    public HomeView() {
    }

    public HomeView(List<RecipeCardView> popularRecipes,
                    List<RecipeCardView> latestRecipes,
                    List<CategoryView> categories) {
        this.popularRecipes = popularRecipes;
        this.latestRecipes = latestRecipes;
        this.categories = categories;
    }

    public List<RecipeCardView> getPopularRecipes() {
        return popularRecipes;
    }

    public void setPopularRecipes(List<RecipeCardView> popularRecipes) {
        this.popularRecipes = popularRecipes;
    }

    public List<RecipeCardView> getLatestRecipes() {
        return latestRecipes;
    }

    public void setLatestRecipes(List<RecipeCardView> latestRecipes) {
        this.latestRecipes = latestRecipes;
    }

    public List<CategoryView> getCategories() {
        return categories;
    }
    public void setCategories(List<CategoryView> categories) {
        this.categories = categories;
    }
}
