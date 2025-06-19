package org.example.repository;

import org.example.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentRepositoryTest {

    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
    }

    @Test
    void testFindAllByPostId_ReturnsComments() {
        // Arrange
        List<Comment> expected = List.of(
                new Comment(1L, "First comment"),
                new Comment(2L, "Second comment")
        );
        when(commentRepository.findAllByPostId(1L)).thenReturn(expected);

        // Act
        List<Comment> actual = commentRepository.findAllByPostId(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(new Comment(1L, "First comment")));
        verify(commentRepository).findAllByPostId(1L);
    }

    @Test
    void testSave_AddsCommentToPost() {
        // Act
        commentRepository.save(1L, "This is a comment");

        // Assert
        verify(commentRepository).save(eq(1L), eq("This is a comment"));
    }

    @Test
    void testUpdate_UpdatesComment() {
        // Arrange
        Comment updated = new Comment(1L, "Updated comment");
        when(commentRepository.update(updated)).thenReturn(updated);

        // Act
        Comment result = commentRepository.update(updated);

        // Assert
        assertNotNull(result);
        assertEquals("Updated comment", result.getText());
        verify(commentRepository).update(updated);
    }

    @Test
    void testDeleteById_DeletesComment() {
        // Act
        commentRepository.deleteById(1L);

        // Assert
        verify(commentRepository).deleteById(1L);
    }
}