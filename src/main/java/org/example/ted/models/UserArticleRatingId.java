package org.example.ted.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserArticleRatingId implements java.io.Serializable {
    private static final long serialVersionUID = -8169912268038421646L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserArticleRatingId entity = (UserArticleRatingId) o;
        return Objects.equals(this.articleId, entity.articleId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, userId);
    }

}