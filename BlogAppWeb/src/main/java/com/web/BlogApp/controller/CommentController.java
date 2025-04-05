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
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/{postId}")
    public String addComment(@PathVariable UUID postId,
                             @Valid @ModelAttribute("comment") CommentDTO commentDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", bindingResult);
            redirectAttributes.addFlashAttribute("comment", commentDTO);
            return "redirect:/posts/" + postId;
        }

        Post post = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

        Comment comment = new Comment();
        comment.setAuthor(commentDTO.getAuthor());
        comment.setContent(commentDTO.getContent());
        comment.setPost(post);
        commentService.save(comment);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{id}/{idPost}")
    public String deleteComment(@PathVariable UUID id,@PathVariable UUID idPost, RedirectAttributes redirectAttributes) {
        return commentService.delete(id,idPost, redirectAttributes);
    }

}
