package com.example.recipehub.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipeid", nullable = false)
    private Recipe recipe;

    @Column(name = "stepnumber", nullable = false)
    private Integer stepNumber;

    @NotBlank(message = "Шаг не может быть пустым")
    @Size(max = 2000, message = "Шаг не должен превышать 2000 символов")
    private String text;

    public Step() {
    }

    public Step(Recipe recipe, Integer stepNumber, String text) {
        this.recipe = recipe;
        this.stepNumber = stepNumber;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
