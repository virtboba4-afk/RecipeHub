package com.example.recipehub.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.recipehub.dto.AddIngredientDto;
import com.example.recipehub.dto.AddRecipeDto;
import com.example.recipehub.dto.AddStepDto;
import com.example.recipehub.dto.RecipeFilterDto;
import com.example.recipehub.models.entities.*;
import com.example.recipehub.models.enums.Difficulty;
import com.example.recipehub.repositories.*;
import com.example.recipehub.services.RecipeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.example.recipehub.views.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.recipehub.security.CustomUserDetails;
import org.springframework.data.domain.PageRequest;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final StepRepository stepRepository;
    private final RatingRepository ratingRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             CategoryRepository categoryRepository,
                             IngredientRepository ingredientRepository,
                             RecipeIngredientRepository recipeIngredientRepository,
                             StepRepository stepRepository,
                             RatingRepository ratingRepository,
                             FavoriteRepository favoriteRepository,
                             CommentRepository commentRepository,
                             UserRepository userRepository,
                             ModelMapper mapper) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.stepRepository = stepRepository;
        this.ratingRepository = ratingRepository;
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    private RecipeCardView mapToRecipeCardView(Recipe recipe) {
        RecipeCardView view = new RecipeCardView();

        view.setId(recipe.getId());
        view.setTitle(recipe.getTitle());
        view.setShortDescription(recipe.getShortDescription());
        view.setCookingTimeMinutes(recipe.getCookingTimeMinutes());

        return view;
    }


    //Главная страница

    @Override
    //@Cacheable(cacheNames = "homeView", key = "'home'")
    public HomeView getHomeView() {
        log.info("Получение данных для главной страницы");



        List<Long> topIds = ratingRepository
                .findTopRecipeIdsByAverageRating(PageRequest.of(0, 3));

        List<RecipeCardView> popularRecipes = new ArrayList<>();
        for (Long id : topIds) {
            recipeRepository.findById(id).ifPresent(recipe -> {
                Double avg = ratingRepository.findAverageRatingByRecipeId(id);

                RecipeCardView card = new RecipeCardView();
                card.setId(recipe.getId());
                card.setTitle(recipe.getTitle());
                card.setShortDescription(recipe.getShortDescription());
                card.setCookingTimeMinutes(recipe.getCookingTimeMinutes());
                card.setAverageRating(avg);

                popularRecipes.add(card);
            });
        }


        if (popularRecipes.isEmpty()) {
            List<Recipe> latest3 = recipeRepository.findTop3ByOrderByCreatedAtDesc();
            for (Recipe r : latest3) {
                RecipeCardView card = new RecipeCardView();
                card.setId(r.getId());
                card.setTitle(r.getTitle());
                card.setShortDescription(r.getShortDescription());
                card.setCookingTimeMinutes(r.getCookingTimeMinutes());
                card.setAverageRating(null); // или 0.0
                popularRecipes.add(card);
            }
        }


        List<Recipe> latest = recipeRepository.findTop10ByOrderByCreatedAtDesc();
        List<RecipeCardView> latestViews = latest.stream()
                .map(r -> {
                    RecipeCardView card = new RecipeCardView();
                    card.setId(r.getId());
                    card.setTitle(r.getTitle());
                    card.setShortDescription(r.getShortDescription());
                    card.setCookingTimeMinutes(r.getCookingTimeMinutes());
                    card.setAverageRating(
                            ratingRepository.findAverageRatingByRecipeId(r.getId())
                    );
                    return card;
                })
                .toList();

        HomeView view = new HomeView();
        view.setPopularRecipes(popularRecipes);
        view.setLatestRecipes(latestViews);

        return view;
    }

    // Список рецептов и поиск

    @Override

    public RecipeListView getRecipeListView(RecipeFilterDto filterDto) {
        log.info("Поиск рецептов по фильтру: text={}, categoryId={}, maxTime={}, difficulty={}",
                filterDto.getQueryText(),
                filterDto.getCategoryId(),
                filterDto.getMaxCookingTimeMinutes(),
                filterDto.getDifficulty());

        String text = filterDto.getQueryText();
        Integer categoryId = filterDto.getCategoryId();
        Integer maxTime = filterDto.getMaxCookingTimeMinutes();
        String difficultyStr = filterDto.getDifficulty();

        List<Recipe> recipes;

        if (text != null && !text.isBlank()) {
            recipes = recipeRepository.findByTitleContainingIgnoreCase(text);
        } else {
            recipes = recipeRepository.findAll();
        }

        if (categoryId != null) {
            recipes.removeIf(r -> r.getCategories() == null ||
                    r.getCategories().stream().noneMatch(c -> c.getId().equals(categoryId)));
        }

        if (maxTime != null) {
            recipes.removeIf(r -> r.getCookingTimeMinutes() == null
                    || r.getCookingTimeMinutes() > maxTime);
        }

        if (difficultyStr != null && !difficultyStr.isBlank()) {
            Difficulty diff = Difficulty.valueOf(difficultyStr.toUpperCase());
            recipes.removeIf(r -> r.getDifficulty() != diff);
        }

        List<RecipeCardView> cards = mapRecipesToCards(recipes);
        List<CategoryView> categories = mapCategories(categoryRepository.findAll());

        RecipeFilterView filterView = new RecipeFilterView(
                filterDto.getQueryText(),
                filterDto.getCategoryId(),
                filterDto.getMaxCookingTimeMinutes(),
                filterDto.getDifficulty()
        );

        // Пагинация зашлушка
        return new RecipeListView(filterView, cards, categories, 0, 1);
    }

    //  Детали рецепта

    @Override
    //@Cacheable(cacheNames = "recipeDetails", key = "#id")
    public RecipeDetailsView getRecipeDetails(Long id) {
        log.info("Получение деталей рецепта с id={}", id);

        Recipe recipe = recipeRepository.findByIdWithRelations(id)
                .orElseThrow(() -> {
                    log.warn("Рецепт с id={} не найден", id);
                    return new RuntimeException("Recipe not found");
                });

        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipe_Id(id);
        List<Step> steps = stepRepository.findByRecipe_IdOrderByStepNumberAsc(id);
        List<Rating> ratings = ratingRepository.findByRecipe(recipe);

        List<Comment> comments = commentRepository.findByRecipe_IdOrderByCreatedAtDesc(id);

        List<CommentView> commentViews = new ArrayList<>();
        for (Comment c : comments) {
            CommentView cv = new CommentView(
                    c.getId(),
                    c.getAuthor().getFullName(),
                    c.getText(),
                    c.getCreatedAt()
            );
            commentViews.add(cv);
        }


        Double avgRating = null;
        if (!ratings.isEmpty()) {
            avgRating = ratings.stream()
                    .mapToInt(Rating::getRating)
                    .average()
                    .orElse(0.0);
        }

        boolean isFavorite = false;
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId != null) {
            isFavorite = favoriteRepository.existsByUser_IdAndRecipe_Id(currentUserId, id);
        }

        List<String> categoryNames = new ArrayList<>();
        if (recipe.getCategories() != null) {
            for (Category c : recipe.getCategories()) {
                categoryNames.add(c.getName());
            }
        }

        List<IngredientView> ingredientViews = new ArrayList<>();
        for (RecipeIngredient ri : ingredients) {
            IngredientView iv = new IngredientView(
                    ri.getIngredient().getName(),
                    ri.getAmount(),
                    ri.getUnit()
            );
            ingredientViews.add(iv);
        }

        List<StepView> stepViews = new ArrayList<>();
        for (Step s : steps) {
            StepView sv = new StepView(s.getStepNumber(), s.getText());
            stepViews.add(sv);
        }



        return new RecipeDetailsView(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getDescription(),
                recipe.getCookingTimeMinutes(),
                recipe.getDifficulty().name(),
                recipe.getAuthor().getFullName(),
                categoryNames,
                ingredientViews,
                stepViews,
                isFavorite,
                avgRating,
                commentViews
        );

    }



    @Override
    public RecipeCreateView getEmptyCreateView() {
        log.info("Подготовка пустого RecipeCreateView");

        List<CategoryView> categories = mapCategories(categoryRepository.findAll());
        return new RecipeCreateView(
                null,
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                categories,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    //  Создание рецепта (запись)

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"homeView", "recipeList", "recipeDetails"}, allEntries = true)
    public Long createRecipe(AddRecipeDto dto) {
        log.info("Создание нового рецепта: title={}", dto.getTitle());

        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId == null) {
            log.warn("Попытка создать рецепт без авторизации");
            throw new RuntimeException("User is not logged in");
        }

        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = new Recipe();
        recipe.setTitle(dto.getTitle());
        recipe.setShortDescription(dto.getShortDescription());
        recipe.setDescription(dto.getDescription());
        recipe.setCookingTimeMinutes(dto.getCookingTimeMinutes());
        recipe.setDifficulty(dto.getDifficulty());
        recipe.setAuthor(author);
        recipe.setCreatedAt(LocalDateTime.now());

        // категории
        List<Category> categories = new ArrayList<>();
        if (dto.getCategoryIds() != null) {
            for (Integer catId : dto.getCategoryIds()) {
                categoryRepository.findById(catId).ifPresent(categories::add);
            }
        }
        recipe.setCategories(categories);

        recipe = recipeRepository.save(recipe);

        // ингредиенты
        if (dto.getIngredients() != null) {
            for (AddIngredientDto ingDto : dto.getIngredients()) {
                if (ingDto == null || ingDto.getName() == null || ingDto.getName().isBlank()) {
                    continue;
                }

                Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(ingDto.getName());
                if (ingredient == null) {
                    ingredient = new Ingredient(ingDto.getName());
                    ingredient = ingredientRepository.save(ingredient);
                }

                RecipeIngredient ri = new RecipeIngredient(
                        recipe,
                        ingredient,
                        ingDto.getAmount(),
                        ingDto.getUnit()
                );
                recipeIngredientRepository.save(ri);
            }
        }

        // шаги — НОРМАЛЬНАЯ НУМЕРАЦИЯ 1,2,3,...
        int stepIndex = 1;

        if (dto.getSteps() != null) {
            for (AddStepDto stepDto : dto.getSteps()) {
                if (stepDto == null || stepDto.getText() == null || stepDto.getText().isBlank()) {
                    continue; // пустые строки пропускаем
                }

                Step step = new Step();
                step.setRecipe(recipe);
                step.setStepNumber(stepIndex++); // тут вручную ставим номер
                step.setText(stepDto.getText());

                stepRepository.save(step);
            }
        }

        log.info("Рецепт создан с id={}", recipe.getId());
        return recipe.getId();
    }


    // Удаление рецепта

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"homeView", "recipeList", "recipeDetails"}, allEntries = true)
    public void deleteRecipe(Long id) {
        log.info("Удаление рецепта id={}", id);

        if (!recipeRepository.existsById(id)) {
            log.warn("Попытка удалить несуществующий рецепт id={}", id);
            throw new RuntimeException("Recipe not found");
        }

        recipeRepository.deleteById(id);
    }

    // Вспомогательные мапперы

    private List<RecipeCardView> mapRecipesToCards(List<Recipe> recipes) {
        List<RecipeCardView> result = new ArrayList<>();
        for (Recipe r : recipes) {
            List<String> categoryNames = new ArrayList<>();
            if (r.getCategories() != null) {
                for (Category c : r.getCategories()) {
                    categoryNames.add(c.getName());
                }
            }

            RecipeCardView card = new RecipeCardView(
                    r.getId(),
                    r.getTitle(),
                    r.getShortDescription(),
                    r.getCookingTimeMinutes(),
                    r.getDifficulty().name(),
                    categoryNames
            );
            result.add(card);
        }
        return result;
    }

    private List<CategoryView> mapCategories(List<Category> categories) {
        List<CategoryView> result = new ArrayList<>();
        for (Category c : categories) {
            CategoryView view = mapper.map(c, CategoryView.class);
            result.add(view);
        }
        return result;
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
