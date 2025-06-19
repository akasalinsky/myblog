package org.example.model;

public class Comment {
    private Long id;
    private String text;

    public Comment() {}

    public Comment(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;

        Comment other = (Comment) o;

        return id != null && id.equals(other.id)
                && text != null && text.equals(other.text);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }
}