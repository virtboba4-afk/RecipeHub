package com.example.recipehub.services;


import com.example.recipehub.views.FavoritesView;

public interface FavoriteService {

    FavoritesView getFavoritesForCurrentUser();

    void addFavorite(Long recipeId);

    void removeFavorite(Long recipeId);
}

