package com.example.recipehub.services;

import com.example.recipehub.dto.AddCommentDto;
import com.example.recipehub.models.entities.Comment;
import com.example.recipehub.models.entities.Recipe;
import com.example.recipehub.models.entities.User;
import com.example.recipehub.repositories.CommentRepository;
import com.example.recipehub.repositories.RecipeRepository;
import com.example.recipehub.repositories.UserRepository;
import com.example.recipehub.security.CustomUserDetails;
import com.example.recipehub.services.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(RecipeRepository recipeRepository,
                              UserRepository userRepository,
                              CommentRepository commentRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(Long recipeId, AddCommentDto dto) {
        Long userId = getCurrentUserIdOrNull();
        if (userId == null) {
            log.warn("Попытка добавить комментарий без авторизации");
            throw new RuntimeException("User is not logged in");
        }

        if (dto.getText() == null || dto.getText().isBlank()) {
            log.warn("Пустой комментарий, ничего не сохраняем");
            return;
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment(recipe, author, dto.getText());
        commentRepository.save(comment);

        log.info("Комментарий добавлен, id={}, recipeId={}, userId={}",
                comment.getId(), recipeId, userId);
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

