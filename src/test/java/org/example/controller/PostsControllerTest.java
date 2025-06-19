package org.example.controller;

import org.example.DTO.PostDTO;
import org.example.model.Post;
import org.example.service.CommentService;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostsControllerTest {

    @InjectMocks
    private PostsController postsController;

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postsController).build();
    }


    @Test
    void testGetPostById_ReturnsDetailsPage() throws Exception {
        Post post = new Post(1L, "Title", "Text", "/img.jpg", 0L, List.of(), "");
        when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void testShowAddPostForm() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"));
    }

    @Test
    void testCreatePost_RedirectsToPosts() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        mockMvc.perform(multipart("/posts")
                        .file(emptyFile)
                        .param("title", "Новый пост")
                        .param("text", "Содержание")
                        .param("tags", "тег1,тег2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService).createPost(any(PostDTO.class));
    }

    @Test
    void testEditPostForm_LoadsPost() throws Exception {
        Post post = new Post(1L, "Old Title", "Old Text", "/old.jpg", 0L, List.of(), "old_tag");
        when(postService.findById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post", post));

        verify(postService).findById(1L);
    }

    @Test
    void testUpdatePost_CallsServiceUpdate() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image",
                "",
                "image/jpeg",
                new byte[0]
        );

        mockMvc.perform(multipart("/posts/1")
                        .file(emptyFile)
                        .param("title", "Обновленный пост")
                        .param("text", "Новое содержание")
                        .param("tags", "новый_тег"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService).updatePost(eq(1L), any(PostDTO.class));
    }

    @Test
    void testDeletePost_CallsServiceDelete() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService).deletePost(1L);
    }

    @Test
    void testAddCommentToPost_CallsCommentService() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("text", "This is a comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService).addCommentToPost(eq(1L), eq("This is a comment"));
    }

    @Test
    void testUpdateComment_CallsCommentService() throws Exception {
        mockMvc.perform(post("/posts/1/comments/100")
                        .param("text", "Updated text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService).updateComment(100L, "Updated text");
    }

    @Test
    void testDeleteComment_CallsCommentService() throws Exception {
        mockMvc.perform(post("/posts/1/comments/100/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService).deleteComment(100L);
    }
}