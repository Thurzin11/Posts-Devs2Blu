package com.web.BlogApp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.web.BlogApp.dtos.CommentDTO;
import com.web.BlogApp.dtos.PostRecordDto;
import com.web.BlogApp.model.Post;
import org.springframework.stereotype.Service;
import com.web.BlogApp.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class PostService {

	private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

	public ModelAndView findAll() {
		ModelAndView mv = new ModelAndView("posts");
		List<Post> posts = postRepository.findAll();
		mv.addObject("post", posts);
		return mv;
	}

	public String findById(UUID id, Model model) {
		Post post = postRepository.findById(id).orElseThrow();
		model.addAttribute("post", post);
		if (!model.containsAttribute("comment")) {
			model.addAttribute("comment", new CommentDTO());
		}
		return "postDetails";
	}

	public Optional<Post> findByIdForEdit(UUID id) {
		return postRepository.findById(id);
	}

	public String save(PostRecordDto postDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "newpostForm";
		}

		Post post = new Post();
		post.setAutor(postDto.autor());
		post.setTitulo(postDto.titulo());
		post.setTexto(postDto.texto());
		post.setData(LocalDate.now());

		postRepository.save(post);

		redirectAttributes.addFlashAttribute("message", "Post created successfully!");
		return "redirect:/posts";
	}

	public String edit(UUID id,PostRecordDto postDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "editPostForm";
		}

		Optional<Post> existingPost = findByIdForEdit(id);

		if (existingPost.isPresent()) {
			Post postToUpdate = existingPost.get();
			postToUpdate.setAutor(postDto.autor());
			postToUpdate.setTitulo(postDto.titulo());
			postToUpdate.setTexto(postDto.texto());

			postRepository.save(postToUpdate);

			redirectAttributes.addFlashAttribute("message", "Post updated successfully!");
		}

		return "redirect:/posts";
	}

	@Transactional
	public String delete(UUID id, RedirectAttributes redirectAttributes) {
		Optional<Post> post = findByIdForEdit(id);

		if (post.isPresent()) {
			postRepository.delete(post.get());
			redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
		}

		return "redirect:/posts";
	}
}
