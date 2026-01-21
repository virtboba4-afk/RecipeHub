package com.example.recipehub.services;

import com.example.recipehub.dto.AddRecipeDto;
import com.example.recipehub.dto.RecipeFilterDto;
import com.example.recipehub.views.HomeView;
import com.example.recipehub.views.RecipeCreateView;
import com.example.recipehub.views.RecipeDetailsView;
import com.example.recipehub.views.RecipeListView;

public interface RecipeService {

    HomeView getHomeView();

    RecipeListView getRecipeListView(RecipeFilterDto filter);

    RecipeDetailsView getRecipeDetails(Long id);

    RecipeCreateView getEmptyCreateView();

    Long createRecipe(AddRecipeDto dto);

    void deleteRecipe(Long id);
}

