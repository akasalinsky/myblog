package org.example.repository;

import org.example.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.example.model.Post;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CommentRepository commentRepository;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate, CommentRepository commentRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT id, title, text, imagePath, likesCount, tags FROM posts";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long postId = rs.getLong("id");
            List<Comment> comments = commentRepository.findAllByPostId(postId);

            return new Post(
                    postId,
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getString("imagePath"),
                    rs.getLong("likesCount"),
                    comments,
                    rs.getString("tags")
            );
        });
    }

    public Post save(Post post) {
        // SQL-запрос с указанием необходимости возврата ключей
        String sql = "INSERT INTO posts(title, text, imagePath, likesCount, tags) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // Создаем PreparedStatement с флагом RETURN_GENERATED_KEYS
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            // Устанавливаем параметры
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getText());
            ps.setString(3, post.getImagePath());
            ps.setLong(4, post.getLikesCount());
            ps.setString(5, post.getTags());

            return ps;
        }, keyHolder);

        // Получаем сгенерированный ID
        Long id = keyHolder.getKey().longValue();

        // Устанавливаем ID в объект Post
        post.setId(id);

        // Возвращаем объект с установленным ID
        return post;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM comments WHERE post_id = ?", id);
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    @Override
    public Post findById(Long id) {
        String sql = "SELECT p.id AS post_id, p.title, p.text, p.imagePath, p.likesCount, p.tags FROM posts p WHERE p.id = ?";

        List<Post> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long postId = rs.getLong("post_id");

            List<Comment> comments = commentRepository.findAllByPostId(postId);

            return new Post(
                    postId,
                    rs.getString("title"),
                    rs.getString("text"),
                    rs.getString("imagePath"),
                    rs.getLong("likesCount"),
                    comments,
                    rs.getString("tags")
            );
        }, id);

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, text = ?, imagePath = ?, tags = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                post.getTitle(),
                post.getText(),
                post.getImagePath(),
                post.getTags(),
                post.getId());
    }

}