package com.example.recipehub.services;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.recipehub.models.entities.Category;
import com.example.recipehub.repositories.CategoryRepository;
import com.example.recipehub.services.CategoryService;
import com.example.recipehub.views.CategoryView;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(cacheNames = "categories", key = "'all'")
    public List<CategoryView> getAllCategories() {
        log.info("Загрузка всех категорий");
        List<Category> categories = categoryRepository.findAll();
        List<CategoryView> result = new ArrayList<>();
        for (Category c : categories) {
            result.add(mapper.map(c, CategoryView.class));
        }
        return result;
    }
}
