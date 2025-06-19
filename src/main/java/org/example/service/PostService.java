package org.example.service;

import org.example.DTO.PostDTO;
import org.example.model.Post;
import org.example.repository.PostsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PostService {
    private final PostsRepository postsRepository;

    public PostService(PostsRepository postsRepository){
        this.postsRepository = postsRepository;
    }

    public List<Post> findAll() {
        return postsRepository.findAll();
    }

    public Post findById(Long id) {
        return postsRepository.findById(id);
    }


    private static final Path UPLOAD_DIR = Path.of("uploads");

    public Long createPost(PostDTO postDto) {
        try {
            String imagePath = saveImage(postDto.getImage());

            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setText(postDto.getText());
            post.setImagePath(imagePath);
            post.setLikesCount(0L);
            post.setTags(convertTags(postDto.getTags()));

            return postsRepository.save(post).getId();

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private String convertTags(String tags) {
        return "[\"" + tags.replace(",", "\",\"") + "\"]";
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }

        String filename = String.format("%d_%s",
                System.currentTimeMillis(),
                image.getOriginalFilename());

        Path filePath = UPLOAD_DIR.resolve(filename);
        Files.copy(image.getInputStream(), filePath);

        return filePath.toString();
    }

    public void deletePost(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID поста не может быть null");
        }
        postsRepository.deleteById(id);
    }

    public void updatePost(Long id, PostDTO dto) {
        Post post = postsRepository.findById(id);
        if (post == null) {
            throw new IllegalArgumentException("Пост с ID " + id + " не найден");
        }

        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setTags(dto.getTags());

        if (!dto.getImage().isEmpty()) {
            try {
                String imagePath = saveImage(dto.getImage());
                post.setImagePath(imagePath);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }
        postsRepository.update(post);
    }
    public void updatePost(Post post) {
        postsRepository.update(post);
    }

}
