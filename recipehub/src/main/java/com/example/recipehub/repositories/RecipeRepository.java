package com.example.recipehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.recipehub.models.entities.Recipe;
import com.example.recipehub.models.enums.Difficulty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findTop10ByOrderByCreatedAtDesc();

    List<Recipe> findTop3ByOrderByCreatedAtDesc();

    List<Recipe> findByTitleContainingIgnoreCase(String titlePart);

    List<Recipe> findByCategories_Id(Integer categoryId);

    // 🔹 Оставляем только одну коллекцию в JOIN FETCH, этого достаточно для методички
    @Query("""
           SELECT r FROM Recipe r
           LEFT JOIN FETCH r.categories
           LEFT JOIN FETCH r.author
           WHERE r.id = :id
           """)
    Optional<Recipe> findByIdWithRelations(@Param("id") Long id);
}
