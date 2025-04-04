package com.web.BlogApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.web.BlogApp.model.Post;
import org.springframework.stereotype.Service;
import com.web.BlogApp.repository.PostRepository;
import jakarta.transaction.Transactional;
@Service
public class PostService {

	private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public Optional<Post> findById(UUID id) {
		return postRepository.findById(id);
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	@Transactional
	public void delete(Post post) {
		postRepository.delete(post);
	}
}
