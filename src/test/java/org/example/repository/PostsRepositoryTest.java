package org.example.repository;

import org.example.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostsRepositoryTest {

    private PostsRepository postsRepository;

    @BeforeEach
    void setUp() {
        postsRepository = mock(PostsRepository.class);
    }

    @Test
    void testFindAll_ReturnsListOfPosts() {
        // Arrange
        List<Post> expectedPosts = Arrays.asList(
                new Post(1L, "Title 1", "Text 1", "/image1.jpg", 5L, new ArrayList<>(), "tag1,tag2"),
                new Post(2L, "Title 2", "Text 2", "/image2.jpg", 3L, new ArrayList<>(), "tag3")
        );

        when(postsRepository.findAll()).thenReturn(expectedPosts);

        // Act
        List<Post> result = postsRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPosts, result);
        verify(postsRepository).findAll();
    }

    @Test
    void testFindById_ReturnsPost() {
        // Arrange
        Post expected = new Post(1L, "Test Title", "Test Text", "/test.jpg", 0L, new ArrayList<>(), "test");

        when(postsRepository.findById(1L)).thenReturn(expected);

        // Act
        Post actual = postsRepository.findById(1L);

        // Assert
        assertNotNull(actual);
        assertEquals("Test Title", actual.getTitle());
        assertEquals("Test Text", actual.getText());
        assertEquals("/test.jpg", actual.getImagePath());
        assertEquals("test", actual.getTags());
        assertEquals(0, actual.getLikesCount());
        assertTrue(actual.getComments().isEmpty());

        verify(postsRepository).findById(1L);
    }

    @Test
    void testSave_SavesPost() {
        // Arrange
        Post postToSave = new Post(null, "New Post", "Content", null, 0L, new ArrayList<>(), "new");

        Post savedPost = new Post(1L, "New Post", "Content", null, 0L, new ArrayList<>(), "new");

        when(postsRepository.save(postToSave)).thenReturn(savedPost);

        // Act
        Post result = postsRepository.save(postToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Post", result.getTitle());
        assertEquals("Content", result.getText());
        assertEquals("new", result.getTags());
        assertNull(result.getImagePath());
        assertEquals(0L, result.getLikesCount());
        assertTrue(result.getComments().isEmpty());

        verify(postsRepository).save(postToSave);
    }

    @Test
    void testUpdate_UpdatesPost() {
        // Arrange
        Post updatedPost = new Post(1L, "Updated Title", "Updated Text", "/updated.jpg", 10L, new ArrayList<>(), "updated");

        // Act
        postsRepository.update(updatedPost);

        // Assert
        verify(postsRepository).update(updatedPost);
    }

    @Test
    void testDeleteById_DeletesPost() {
        // Act
        postsRepository.deleteById(1L);

        // Assert
        verify(postsRepository).deleteById(1L);
    }
}