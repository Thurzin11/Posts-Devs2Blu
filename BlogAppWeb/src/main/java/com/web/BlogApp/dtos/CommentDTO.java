package com.web.BlogApp.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public class CommentDTO {
    @NotBlank(message = "Autor é obrigatório")
    private String author;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String content;

    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}