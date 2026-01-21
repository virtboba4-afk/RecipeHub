package com.example.recipehub.dto;

public class AddCommentDto {

    private String text;

    public AddCommentDto() {
    }

    public AddCommentDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
