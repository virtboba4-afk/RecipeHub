package com.example.recipehub.dto;

import jakarta.persistence.Column;

import java.io.Serializable;

public class AddStepDto implements Serializable {

    private Integer stepNumber;


    private String text;

    public AddStepDto() {
    }

    public AddStepDto(Integer stepNumber, String text) {
        this.stepNumber = stepNumber;
        this.text = text;
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
