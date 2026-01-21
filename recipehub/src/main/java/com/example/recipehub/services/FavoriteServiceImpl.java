package com.example.recipehub.services;

import org.springframework.stereotype.Service;
import com.example.recipehub.models.entities.Favorite;
import com.example.recipehub.models.entities.Recipe;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.repositories.FavoriteRepository;
import com.example.recipehub.repositories.RecipeRepository;
import com.example.recipehub.repositories.UserRepository;
import com.example.recipehub.views.FavoritesView;
import com.example.recipehub.views.RecipeCardView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.recipehub.security.CustomUserDetails;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FavoriteServiceImpl {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               RecipeRepository recipeRepository,
                               UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public FavoritesView getFavoritesForCurrentUser() {
        Long userId = getCurrentUserIdOrThrow();
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        List<RecipeCardView> cards = new ArrayList<>();

        for (Favorite f : favorites) {
            Recipe r = f.getRecipe();
            RecipeCardView card = new RecipeCardView(
                    r.getId(),
                    r.getTitle(),
                    r.getShortDescription(),
                    r.getCookingTimeMinutes(),
                    r.getDifficulty().name(),
                    new ArrayList<>()
            );
            cards.add(card);
        }

        return new FavoritesView(cards);
    }

    public void addFavorite(Long recipeId) {
        Long userId = getCurrentUserIdOrThrow();

        if (favoriteRepository.existsByUser_IdAndRecipe_Id(userId, recipeId)) {
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Favorite favorite = new Favorite(user, recipe);
        favoriteRepository.save(favorite);
    }


    @Transactional
    public void removeFavorite(Long recipeId) {
        Long userId = getCurrentUserIdOrThrow();
        favoriteRepository.deleteByUser_IdAndRecipe_Id(userId, recipeId);
    }

    private Long getCurrentUserIdOrThrow() {
        Long userId = getCurrentUserIdOrNull();
        if (userId == null) {
            throw new RuntimeException("User is not logged in");
        }
        return userId;
    }

    private Long getCurrentUserIdOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        }

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }

        return null;
    }
}

