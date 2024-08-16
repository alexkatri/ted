package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_multimedia")
public class ArticleMultimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "path", nullable = false, length = 1024)
    private String path;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id", nullable = false)
    @JsonIgnoreProperties("multimedia")
    private Article article;

    public ArticleMultimedia() {
    }

    public ArticleMultimedia(String path) {
        this.path = path;
    }
}