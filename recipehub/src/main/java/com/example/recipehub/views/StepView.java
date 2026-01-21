package com.example.recipehub.views;

import java.io.Serializable;

public class StepView implements Serializable {
    private int stepNumber;
    private String text;

    public StepView() {
    }

    public StepView(int stepNumber, String text) {
        this.stepNumber = stepNumber;
        this.text = text;
    }

    public int getStepNumber() {      // <--- важно
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getText() {         // <--- важно
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}