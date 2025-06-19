package org.example.DTO;

import org.springframework.web.multipart.MultipartFile;

public class PostDTO {
    private String title;
    private String text;
    private MultipartFile image;
    private String tags;

    // Конструктор, геттеры и сеттеры
    public PostDTO(String title, String text, MultipartFile image, String tags) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.tags = tags;
    }
    public PostDTO(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
// Геттеры...
}