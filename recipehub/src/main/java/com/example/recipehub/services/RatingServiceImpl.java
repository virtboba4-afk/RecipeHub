package com.example.recipehub.services;

import com.example.recipehub.dto.AddRatingDto;
import com.example.recipehub.models.entities.Rating;
import com.example.recipehub.models.entities.Recipe;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.repositories.RatingRepository;
import com.example.recipehub.repositories.RecipeRepository;
import com.example.recipehub.repositories.UserRepository;
import com.example.recipehub.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {   // 👈 implements RatingService

    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RatingServiceImpl(RatingRepository ratingRepository,
                             RecipeRepository recipeRepository,
                             UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addRating(Long recipeId, AddRatingDto dto) {
        Long userId = getCurrentUserIdOrThrow();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Rating> existingOpt = ratingRepository.findByUserAndRecipe(user, recipe);

        Rating rating;
        if (existingOpt.isPresent()) {
            rating = existingOpt.get();
            rating.setRating(dto.getRating());
            rating.setComment(dto.getComment());
            rating.setCreatedAt(LocalDateTime.now());
        } else {
            rating = new Rating(user, recipe, dto.getRating(), dto.getComment(), LocalDateTime.now());
        }

        ratingRepository.save(rating);
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
