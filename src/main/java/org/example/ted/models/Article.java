package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties("articles")
    private User createdBy;

    @Column(name = "date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date date;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "multimedia_path", length = 1024)
    private String multimediaPath;

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties("article")
    @JsonIgnore
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties("article")
    @JsonIgnore
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article")
    @JsonIgnoreProperties("article")
    private Set<UserArticleRating> userArticleRatings = new LinkedHashSet<>();

}