package com.example.recipehub.repositories;

import com.example.recipehub.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByRecipe_IdOrderByCreatedAtDesc(Long recipeId);
}
