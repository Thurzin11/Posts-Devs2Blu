package com.web.BlogApp.controller;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import com.web.BlogApp.model.Post;
import com.web.BlogApp.service.PostService;
import freemarker.core.ReturnInstruction;
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

	@GetMapping()
	public ModelAndView getPosts() {
		return postService.findAll();
	}

	@GetMapping("/{id}")
	public String getPost(@PathVariable UUID id, Model model) {
		return postService.findById(id, model);
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
		return postService.save(postDto,bindingResult,redirectAttributes);
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable UUID id, Model model) {
		Optional<Post> post = postService.findByIdForEdit(id);

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
		return postService.edit(id,postDto,bindingResult,redirectAttributes);

	}

	@GetMapping("/delete/{id}")
	public String deletePost(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
		return postService.delete(id, redirectAttributes);
	}
}