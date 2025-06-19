package org.example.service;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCommentToPost_ShouldCallRepositorySaveWithCorrectArgs() {
        // Act
        commentService.addCommentToPost(1L, "This is a comment");

        // Assert
        verify(commentRepository).save(eq(1L), eq("This is a comment"));
    }

    @Test
    void testUpdateComment_ShouldCallRepositoryUpdateWithCorrectComment() {
        // Act
        commentService.updateComment(100L, "Updated comment text");

        // Assert
        Comment expected = new Comment(100L, "Updated comment text");
        verify(commentRepository).update(expected);
    }

    @Test
    void testDeleteComment_ShouldCallRepositoryDeleteById() {
        // Act
        commentService.deleteComment(200L);

        // Assert
        verify(commentRepository).deleteById(200L);
    }

    @Test
    void testGetCommentsByPostId_ShouldReturnListOfComments() {
        // Arrange
        List<Comment> comments = Arrays.asList(
                new Comment(1L, "First comment"),
                new Comment(2L, "Second comment")
        );
        when(commentRepository.findAllByPostId(1L)).thenReturn(comments);

        // Act
        List<Comment> result = commentService.getCommentsByPostId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new Comment(1L, "First comment")));
        assertTrue(result.contains(new Comment(2L, "Second comment")));

        verify(commentRepository).findAllByPostId(1L);
    }
}