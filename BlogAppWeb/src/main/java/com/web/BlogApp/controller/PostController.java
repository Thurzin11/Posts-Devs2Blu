package com.web.BlogApp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.web.BlogApp.model.Post;
import com.web.BlogApp.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.BlogApp.dtos.PostRecordDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/posts")
public class PostController {

	private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//	@GetMapping("/")
//	public String rootRedirect() {
//		return "redirect:/";
//	}

	@GetMapping()
	public ModelAndView getPosts() {
		ModelAndView mv = new ModelAndView("posts");
		List<Post> posts = postService.findAll();
		mv.addObject("post", posts);
		return mv;
	}

	@GetMapping(value = "/{id}")
	public ModelAndView getPostDetails(@PathVariable UUID id) {
		ModelAndView mv = new ModelAndView("postDetails");
		Optional<Post> post = postService.findById(id);

		if (post.isPresent()) {
			mv.addObject("post", post.get());
		}

		return mv;
	}

	@GetMapping(value = "/newpost")
	public String getPostForm(Model model) {
		model.addAttribute("postDto", new PostRecordDto(null,null, null, null));
		return "newpostForm";
	}

	@PostMapping(value = "/newpost")
	public String savePost(@Valid @ModelAttribute("postDto") PostRecordDto postDto,
						   BindingResult bindingResult,
						   RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "newpostForm";
		}

		Post post = new Post();
		post.setAutor(postDto.autor());
		post.setTitulo(postDto.titulo());
		post.setTexto(postDto.texto());
		post.setData(LocalDate.now());

		postService.save(post);

		redirectAttributes.addFlashAttribute("message", "Post created successfully!");
		return "redirect:/posts";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable UUID id, Model model) {
		Optional<Post> post = postService.findById(id);

		if (post.isPresent()) {
			Post postModel = post.get();
			PostRecordDto postDto = new PostRecordDto(
					postModel.getId(),
					postModel.getAutor(),
					postModel.getTitulo(),
					postModel.getTexto()
			);
			model.addAttribute("postDto", postDto);
			return "editPostForm";
		}

		return "redirect:/posts";
	}

	@PostMapping("/{id}")
	public String updatePost(@PathVariable UUID id,
							 @Valid @ModelAttribute("postDto") PostRecordDto postDto,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "editPostForm";
		}

		Optional<Post> existingPost = postService.findById(id);

		if (existingPost.isPresent()) {
			Post postToUpdate = existingPost.get();
			postToUpdate.setAutor(postDto.autor());
			postToUpdate.setTitulo(postDto.titulo());
			postToUpdate.setTexto(postDto.texto());

			postService.save(postToUpdate);

			redirectAttributes.addFlashAttribute("message", "Post updated successfully!");
		}

		return "redirect:/posts";
	}

	@GetMapping("/delete/{id}")
	public String deletePost(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		Optional<Post> post = postService.findById(id);

		if (post.isPresent()) {
			postService.delete(post.get());
			redirectAttributes.addFlashAttribute("message", "Post deleted successfully!");
		}

		return "redirect:/posts";
	}
}