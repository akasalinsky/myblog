package org.example.repository;

import org.example.model.Post;
import java.util.List;


public interface PostsRepository {
    List<Post> findAll();
    Post save(Post post);
    void deleteById(Long id);
    void update(Post post); // ← этот метод
    Post findById(Long id);
}

