package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class FavoritesView implements Serializable {
    private List<RecipeCardView> favoriteRecipes;

    public FavoritesView() {
    }

    public FavoritesView(List<RecipeCardView> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public List<RecipeCardView> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(List<RecipeCardView> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }
}
