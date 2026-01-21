package com.example.recipehub.services;

import com.example.recipehub.dto.AddCommentDto;

public interface CommentService {

    void addComment(Long recipeId, AddCommentDto dto);
}