package com.example.recipehub.controllers;

import com.example.recipehub.dto.AddCommentDto;
import com.example.recipehub.dto.AddRatingDto;
import com.example.recipehub.dto.AddRecipeDto;
import com.example.recipehub.dto.RecipeFilterDto;
import com.example.recipehub.services.CommentService;
import com.example.recipehub.services.RatingService;
import com.example.recipehub.services.RecipeService;
import com.example.recipehub.views.RecipeCreateView;
import com.example.recipehub.views.RecipeListView;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final CommentService commentService;
    private final RatingService ratingService;   // 👈 добавили

    public RecipeController(RecipeService recipeService,
                            CommentService commentService,
                            RatingService ratingService) {
        this.recipeService = recipeService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    @GetMapping
    public String listRecipes(@RequestParam(required = false) String queryText,
                              @RequestParam(required = false) Integer categoryId,
                              @RequestParam(required = false) Integer maxCookingTimeMinutes,
                              @RequestParam(required = false) String difficulty,
                              Model model) {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setQueryText(queryText);
        filterDto.setCategoryId(categoryId);
        filterDto.setMaxCookingTimeMinutes(maxCookingTimeMinutes);
        filterDto.setDifficulty(difficulty);

        RecipeListView view = recipeService.getRecipeListView(filterDto);

        model.addAttribute("view", view);
        model.addAttribute("currentPage", view.getCurrentPage());
        model.addAttribute("totalPages", view.getTotalPages());

        return "recipes/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AddRecipeDto());
        }

        RecipeCreateView view = recipeService.getEmptyCreateView();
        model.addAttribute("view", view);

        return "recipes/add";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addRecipe(@Valid @ModelAttribute("form") AddRecipeDto form,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("form", form);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.form",
                    bindingResult
            );
            return "redirect:/recipes/add";
        }

        Long id = recipeService.createRecipe(form);
        redirectAttributes.addFlashAttribute("successMessage", "Рецепт добавлен!");
        return "redirect:/recipes/" + id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        recipeService.deleteRecipe(id);
        redirectAttributes.addFlashAttribute("successMessage", "Рецепт удалён");
        return "redirect:/recipes";
    }

    @GetMapping("/{id}")
    public String getRecipeDetails(@PathVariable("id") Long id, Model model) {

        var view = recipeService.getRecipeDetails(id);

        model.addAttribute("view", view);
        model.addAttribute("rating", new AddRatingDto());
        model.addAttribute("comment", new AddCommentDto());

        return "recipes/details";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable("id") Long id,
                             @ModelAttribute("comment") AddCommentDto dto) {

        commentService.addComment(id, dto);
        return "redirect:/recipes/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/rating")
    public String addRating(@PathVariable("id") Long id,
                            @ModelAttribute("rating") AddRatingDto dto) {

        ratingService.addRating(id, dto);
        return "redirect:/recipes/" + id;
    }
}
