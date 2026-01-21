package com.example.recipehub.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.recipehub.services.FavoriteServiceImpl;
import com.example.recipehub.views.FavoritesView;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteServiceImpl favoriteService;

    public FavoriteController(FavoriteServiceImpl favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String favorites(Model model) {
        FavoritesView view = favoriteService.getFavoritesForCurrentUser();
        model.addAttribute("view", view);
        return "favorites/list";
    }

    @PostMapping("/add/{recipeId}")
    public String addFavorite(@PathVariable("recipeId") Long recipeId) {
        favoriteService.addFavorite(recipeId);
        return "redirect:/recipes/" + recipeId;
    }

    @PostMapping("/remove/{recipeId}")
    public String removeFavorite(@PathVariable("recipeId") Long recipeId) {
        favoriteService.removeFavorite(recipeId);
        return "redirect:/recipes/" + recipeId;
    }
}
