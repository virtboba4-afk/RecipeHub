package com.example.recipehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.recipehub.models.entities.Favorite;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    boolean existsByUser_IdAndRecipe_Id(Long userId, Long recipeId);


    @Modifying
    @Transactional
    void deleteByUser_IdAndRecipe_Id(Long userId, Long recipeId);
}
