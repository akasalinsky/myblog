package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private Long likesCount;
    private List<Comment> comments = new ArrayList<>();
    private String tags = "";

    public Post(){}

    public Post(Long id, String title, String text, String imagePath, Long likesCount, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
        this.comments = comments;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Post(Long id, String title, String text, String imagePath, Long likesCount, List<Comment> comments, String tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.likesCount = likesCount;
        this.comments = comments;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTags(){
        return tags;
    }

    public String getTextPreview(){
        return text.substring(0,5);
    }

    public String getTagsAsText() { return tags; }
}
