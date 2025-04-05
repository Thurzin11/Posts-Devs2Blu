package com.web.BlogApp.service;

import com.web.BlogApp.dtos.CommentDTO;
import com.web.BlogApp.model.Comment;
import com.web.BlogApp.model.Post;
import com.web.BlogApp.repository.CommentRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(UUID id) {
        return commentRepository.findById(id);
    }

    public String save(UUID postId, CommentDTO commentDTO, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.comment", bindingResult);
            redirectAttributes.addFlashAttribute("comment", commentDTO);
            return "redirect:/posts/" + postId;
        }

        try {
            Post post = postService.findByIdForEdit(postId).orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));

            Comment comment = new Comment();
            comment.setAuthor(commentDTO.getAuthor());
            comment.setContent(commentDTO.getContent());
            comment.setPost(post);

            commentRepository.save(comment);

            return "redirect:/posts/" + postId;

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Post não encontrado");

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar comentário. Tente novamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro inesperado");
        }

        return "redirect:/posts/" + postId;
    }

    public String delete(UUID id, UUID idPost, RedirectAttributes redirectAttributes) {
        try {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado"));

            commentRepository.delete(comment);
            redirectAttributes.addFlashAttribute("success", "Comentário excluído com sucesso!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Comentário não encontrado");

        } catch (DataAccessException e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir comentário. Tente novamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro inesperado ao excluir comentário");
        }

        return "redirect:/posts/" + idPost;
    }
}
