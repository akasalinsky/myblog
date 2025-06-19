package org.example.service;

import org.example.DTO.PostDTO;
import org.example.model.Post;
import org.example.repository.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostsRepository postsRepository;

    private MultipartFile mockImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
    }

    @Test
    void testCreatePost_ShouldSavePostWithImageAndTags() throws IOException {
        // Arrange
        PostDTO dto = new PostDTO();
        dto.setTitle("Заголовок");
        dto.setText("Текст поста");
        dto.setTags("tag1,tag2");
        dto.setImage(mockImage);

        Post savedPost = new Post();
        savedPost.setId(1L);
        when(postsRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        Long postId = postService.createPost(dto);

        // Assert
        assertNotNull(postId);
        assertEquals(1L, postId);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postsRepository).save(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();
        assertEquals("Заголовок", capturedPost.getTitle());
        assertEquals("Текст поста", capturedPost.getText());
        assertEquals("[\"tag1\",\"tag2\"]", capturedPost.getTags());
        assertTrue(capturedPost.getImagePath().endsWith("test.jpg"));
        assertEquals(0, capturedPost.getLikesCount());
    }

    @Test
    void testUpdatePost_WithNewImage_ShouldUpdateAllFields() throws IOException {
        // Arrange
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Old Title");
        existingPost.setText("Old Text");
        existingPost.setTags("[\"old_tag\"]");

        when(postsRepository.findById(1L)).thenReturn(existingPost);

        PostDTO dto = new PostDTO();
        dto.setTitle("Updated Title");
        dto.setText("Updated Text");
        dto.setTags("new_tag1,new_tag2");
        dto.setImage(mockImage); // новое изображение

        // Act
        postService.updatePost(1L, dto);

        // Assert
        verify(postsRepository).update(any(Post.class));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postsRepository).update(postCaptor.capture());

        Post updatedPost = postCaptor.getValue();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Text", updatedPost.getText());
        assertTrue(updatedPost.getTags().contains("new_tag1"));
        assertTrue(updatedPost.getTags().contains("new_tag2"));
        assertTrue(updatedPost.getImagePath().endsWith("test.jpg"));
    }

    @Test
    void testUpdatePost_WithoutImage_ShouldNotChangeImagePath() {
        // Arrange
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Old Title");
        existingPost.setText("Old Text");
        existingPost.setImagePath("/path/to/old/image.jpg");
        existingPost.setTags("[\"old_tag\"]");

        when(postsRepository.findById(1L)).thenReturn(existingPost);

        PostDTO dto = new PostDTO();
        dto.setTitle("Updated Title");
        dto.setText("Updated Text");
        dto.setTags("new_tag1,new_tag2");
        dto.setImage(new MockMultipartFile("image", new byte[0])); // пустой файл

        // Act
        postService.updatePost(1L, dto);

        // Assert
        verify(postsRepository).update(any(Post.class));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postsRepository).update(postCaptor.capture());

        Post updatedPost = postCaptor.getValue();
        assertEquals("Updated Title", updatedPost.getTitle());
        assertEquals("Updated Text", updatedPost.getText());
        assertTrue(updatedPost.getTags().contains("new_tag1"));
        assertTrue(updatedPost.getTags().contains("new_tag2"));
        assertEquals("/path/to/old/image.jpg", updatedPost.getImagePath()); // не должен измениться
    }

    @Test
    void testDeletePost_ShouldCallRepositoryDeleteById() {
        // Act
        postService.deletePost(1L);

        // Assert
        verify(postsRepository).deleteById(1L);
    }

    @Test
    void testDeletePost_WithNullId_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(null));
    }

    @Test
    void testFindById_ShouldReturnPost() {
        // Arrange
        Post expectedPost = new Post();
        expectedPost.setId(1L);
        when(postsRepository.findById(1L)).thenReturn(expectedPost);

        // Act
        Post actualPost = postService.findById(1L);

        // Assert
        assertEquals(expectedPost, actualPost);
    }

    @Test
    void testFindAll_ShouldReturnListOfPosts() {
        // Arrange
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postsRepository.findAll()).thenReturn(posts);

        // Act
        List<Post> result = postService.findAll();

        // Assert
        assertEquals(2, result.size());
    }
}