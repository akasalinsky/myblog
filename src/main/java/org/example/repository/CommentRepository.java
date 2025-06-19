package org.example.repository;

import org.example.model.Comment;
import java.util.List;

public interface CommentRepository {
    List<Comment> findAllByPostId(Long postId);
    void save(Long postId, String text);
    Comment update(Comment comment);
    void deleteById(Long commentId);
}
