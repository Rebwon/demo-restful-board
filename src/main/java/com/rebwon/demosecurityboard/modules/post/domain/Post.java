package com.rebwon.demosecurityboard.modules.post.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rebwon.demosecurityboard.modules.account.domain.Account;
import com.rebwon.demosecurityboard.modules.account.domain.AccountSerializer;
import com.rebwon.demosecurityboard.modules.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@DynamicInsert @DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Post extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String title;
	@Lob @Column(nullable = false)
	private String content;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = AccountSerializer.class)
	private Account writer;
	@ManyToOne(cascade = CascadeType.ALL)
	private Category category;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private List<Tag> tags = new ArrayList<>();
	private int countOfRecommend = 0;
	private boolean commented = false;

	public static Post of(String title, String content, Account writer, String categoryName, List<Tag> tags) {
		Post post = new Post();
		post.title = title;
		post.content = content;
		post.writer = writer;
		post.category = new Category(categoryName);
		post.tags = tags;
		return post;
	}
}