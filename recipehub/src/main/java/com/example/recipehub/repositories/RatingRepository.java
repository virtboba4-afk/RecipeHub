package com.example.recipehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.recipehub.models.entities.Rating;
import com.example.recipehub.models.entities.Recipe;
import com.example.recipehub.models.entities.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRecipe(Recipe recipe);

    Optional<Rating> findByUserAndRecipe(User user, Recipe recipe);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double findAverageRatingByRecipeId(@Param("recipeId") Long recipeId);

    @Query("""
           SELECT r.recipe.id
           FROM Rating r
           GROUP BY r.recipe.id
           ORDER BY AVG(r.rating) DESC
           """)
    List<Long> findTopRecipeIdsByAverageRating(Pageable pageable);
}