package org.example.controller;

import org.example.DTO.PostDTO;
import org.example.model.Post;
import org.example.service.CommentService;
import org.example.service.PostService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.model.Paging;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Controller
public class PostsController {
    private final PostService postService;
    private final CommentService commentService;

    public PostsController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/posts/{id}")
    public String post(@PathVariable("id") Long id, Model model){
        List<Post> posts = postService.findAll();
        model.addAttribute("post",  postService.findById(id));
        return "post";
    }

    @GetMapping("/posts")
    public String posts(
            @RequestParam(name = "pageNumber",defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize",defaultValue = "5") int pageSize,
            @RequestParam(name = "search",required = false) String search,
            Model model) {

            Paging paging = new Paging(pageNumber, pageSize);
            List<Post> posts = postService.findAll();

            model.addAttribute("posts", posts);
            model.addAttribute("paging", paging);
            model.addAttribute("search", search);
        return "posts";
    }

    @GetMapping("/posts/add")
    public String addPost(){
        return "add-post";
    }

    @PostMapping({"/posts", "/posts/"})
    public String createPost(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
            RedirectAttributes redirectAttributes) {
        PostDTO postDto = new PostDTO(title, text, image, tags);
        Long postId = postService.createPost(postDto);
        return "redirect:/posts";
    }


    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/posts";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImages(@PathVariable("id") Long id, Model model) throws IOException {
        List<Post> posts = postService.findAll();

        Resource resource = new ClassPathResource("static/images/" + postService.findById(id).getImagePath());
        byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        if (post == null) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "add-post";
    }

    @PostMapping("/posts/{id}")
    public String updatePost(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("image") MultipartFile image,
            @RequestParam("tags") String tags) {

        PostDTO postDto = new PostDTO(title, text, image, tags);
        postService.updatePost(id, postDto);

        return "redirect:/posts/" + id;
    }
    @PostMapping("/posts/{id}/like")
    public String updateLike(@PathVariable("id") Long id,
                             @RequestParam("like") boolean isLike) {
        Post post = postService.findById(id);
        if (post != null) {
            long newLikes = isLike ? post.getLikesCount() + 1 : post.getLikesCount() - 1;
            post.setLikesCount(newLikes);
            postService.updatePost(post); // или отдельный метод
        }
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(
            @PathVariable("id") Long id,
            @RequestParam("text") String text) {

        commentService.addCommentToPost(id, text);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/comments/{commentId}")
    public String updateComment(
            @PathVariable("id") Long id,
            @PathVariable("commentId") Long commentId,
            @RequestParam("text") String text) {

        commentService.updateComment(commentId, text);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable("id") Long id,
            @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);
        return "redirect:/posts/" + id;
    }
}