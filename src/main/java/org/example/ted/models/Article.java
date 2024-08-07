package org.example.ted.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
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
    @JoinColumn(name = "createdBy", nullable = false)
    private User createdBy;

    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "views", nullable = false)
    private Integer views;

    @Column(name = "multimedia_path", length = 1024)
    private String multimediaPath;

    @OneToMany(mappedBy = "article")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "article")
    private Set<Like> likes = new LinkedHashSet<>();

}