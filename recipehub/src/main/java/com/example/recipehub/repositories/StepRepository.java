package com.example.recipehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.recipehub.models.entities.Step;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

    List<Step> findByRecipe_IdOrderByStepNumberAsc(Long recipeId);
}
