package com.crking7.datn.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="article")
public class Article {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String title;

	@Column
	private String titleSummary;
	
	@Column()
	@Lob
	private String content;

	@Column
	private String tag;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;

	@Column
	private int status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private List<ArticleImage> articleImages;
}

