package com.example.recipehub.views;

import java.io.Serializable;
import java.util.List;

public class RecipeListView implements Serializable {

    private RecipeFilterView filter;
    private List<RecipeCardView> recipes;
    private List<CategoryView> categories;
    private int currentPage;
    private int totalPages;

    public RecipeListView() {
    }

    public RecipeListView(RecipeFilterView filter,
                          List<RecipeCardView> recipes,
                          List<CategoryView> categories,
                          int currentPage,
                          int totalPages) {
        this.filter = filter;
        this.recipes = recipes;
        this.categories = categories;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public RecipeFilterView getFilter() {
        return filter;
    }

    public void setFilter(RecipeFilterView filter) {
        this.filter = filter;
    }

    public List<RecipeCardView> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeCardView> recipes) {
        this.recipes = recipes;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<CategoryView> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryView> categories) {
        this.categories = categories;
    }
}
