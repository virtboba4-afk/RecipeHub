package com.example.recipehub.dto;

import java.io.Serializable;

public class AddIngredientDto implements Serializable {

    private String name;
    private Double amount;
    private String unit;

    public AddIngredientDto() {
    }

    public AddIngredientDto(String name, Double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
