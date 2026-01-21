package com.example.recipehub.services;



import com.example.recipehub.dto.AddRatingDto;

public interface RatingService {

    void addRating(Long recipeId, AddRatingDto dto);
}

