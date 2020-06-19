package com.rebwon.demosecurityboard.modules.post.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AuthAccount;
import com.rebwon.demosecurityboard.modules.post.domain.Post;
import com.rebwon.demosecurityboard.modules.post.service.PostService;
import com.rebwon.demosecurityboard.modules.post.api.payload.PostCreatePayload;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@PostMapping
	public ResponseEntity createPost(@RequestBody @Valid PostCreatePayload payload, @AuthAccount Account account) {
		Post newPost = postService.createPost(payload, account);
		WebMvcLinkBuilder selfLinkBuilder = linkTo(PostController.class).slash(newPost.getId());
		EntityModel<Post> model = EntityModel.of(newPost);
		model.add(selfLinkBuilder.withSelfRel(), selfLinkBuilder.withRel("update-post"));
		return ResponseEntity.created(selfLinkBuilder.toUri()).body(model);
	}
}