package com.example.recipehub.controllers;

import com.example.recipehub.services.RecipeService;
import com.example.recipehub.views.HomeView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RecipeService recipeService;

    public HomeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        HomeView view = recipeService.getHomeView();
        model.addAttribute("view", view);
        return "home";  // templates/home.html
    }
}
