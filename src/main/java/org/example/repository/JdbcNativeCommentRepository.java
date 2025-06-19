package org.example.repository;

import org.example.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Long postId, String text) {
        jdbcTemplate.update("INSERT INTO comments(post_id, content) VALUES (?, ?)", postId, text);
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        String sql = "SELECT id, content FROM comments WHERE post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Comment(
                rs.getLong("id"),
                rs.getString("content")
        ), postId);
    }

    @Override
    public Comment update(Comment comment) {
        jdbcTemplate.update("UPDATE comments SET content = ? WHERE id = ?",
                comment.getText(), comment.getId());
        return comment;
    }

    @Override
    public void deleteById(Long commentId) {
        jdbcTemplate.update("DELETE FROM comments WHERE id = ?", commentId);
    }
}