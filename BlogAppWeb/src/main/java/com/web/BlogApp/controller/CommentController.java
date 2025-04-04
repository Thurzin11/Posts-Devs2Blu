package com.web.BlogApp.controller;

import com.web.BlogApp.dtos.CommentDTO;
import com.web.BlogApp.model.Comment;
import com.web.BlogApp.model.Post;
import com.web.BlogApp.service.CommentService;
import com.web.BlogApp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/comment/{postId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/{postId}")
    public String addComment(@PathVariable UUID postId,
                             @Valid @ModelAttribute("comment") CommentDTO commentDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
        return "postDetails";
        }
        Post post = postService.findById(postId).orElseThrow();

        Comment comment = new Comment();
        comment.setAuthor(comment.getAuthor());
        comment.setContent(comment.getContent());
        comment.setPost(post);
        commentService.save(comment);

        return "redirect:/posts/" + postId;
    }

}
