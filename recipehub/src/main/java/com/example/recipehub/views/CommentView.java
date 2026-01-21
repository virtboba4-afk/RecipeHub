package com.example.recipehub.views;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentView implements Serializable {

    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime createdAt;

    public CommentView() {
    }

    public CommentView(Long id, String authorName, String text, LocalDateTime createdAt) {
        this.id = id;
        this.authorName = authorName;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
