package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_article_rating")
public class UserArticleRating {
    @EmbeddedId
    private UserArticleRatingId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("userArticleRatings")
    private User user;

    @MapsId("articleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    @JsonIgnoreProperties("userArticleRatings")
    private Article article;

    @Column(name = "value", columnDefinition = "int UNSIGNED not null")
    private int value;

}