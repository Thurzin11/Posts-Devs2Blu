package com.web.BlogApp.service;

import com.web.BlogApp.model.Comment;
import com.web.BlogApp.model.Post;
import com.web.BlogApp.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(UUID id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public String delete(UUID id,UUID idPost, RedirectAttributes redirectAttributes) {
        try {
            Optional<Comment> comment = findById(id);
            if (comment.isPresent()) {
                commentRepository.delete(comment.get());
                redirectAttributes.addFlashAttribute("message", "Comment deleted successfully!");
            }
            return "redirect:/posts/"+idPost;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Comment deleted unsuccessfully!");
            return "redirect:/posts/"+idPost;
        }
    }
}
