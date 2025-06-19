package org.example.service;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addCommentToPost(Long postId, String commentText) {
        commentRepository.save(postId, commentText);
    }

    public void updateComment(Long commentId, String newComment) {

        Comment comment = new Comment(commentId, newComment);
        commentRepository.update(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}