package org.example.ted.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserAdvertisementRatingId implements java.io.Serializable {
    private static final long serialVersionUID = 2818865480451646843L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "advertisement_id", nullable = false)
    private Integer advertisementId;

    public UserAdvertisementRatingId() {
    }

    public UserAdvertisementRatingId(int userId, int advertisementId) {
        this.userId = userId;
        this.advertisementId = advertisementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserAdvertisementRatingId entity = (UserAdvertisementRatingId) o;
        return Objects.equals(this.advertisementId, entity.advertisementId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advertisementId, userId);
    }

}