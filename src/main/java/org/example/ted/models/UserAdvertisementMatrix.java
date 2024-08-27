package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "user_advertisement_matrix")
public class UserAdvertisementMatrix {
    @EmbeddedId
    private UserAdvertisementMatrixId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("userAdvertisementMatrices")
    private User user;

    @MapsId("advertisementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advertisement_id", nullable = false)
    @JsonIgnoreProperties("userAdvertisementMatrices")
    private Advertisement advertisement;

    @ColumnDefault("0")
    @Column(name = "value", nullable = false)
    private Integer value;

    @ColumnDefault("unknown")
    @Column(name = "state", nullable = false, length = 45)
    private String state;

    public UserAdvertisementMatrix(User user, Advertisement advertisement) {
        this.user = user;
        this.advertisement = advertisement;
    }

    public UserAdvertisementMatrix() {

    }

    public UserAdvertisementMatrix(User user, Optional<Advertisement> byId) {
        this.user = user;
        this.advertisement = byId.orElse(null);
    }

    public UserAdvertisementMatrix(User user, Advertisement advertisement, Integer value, String state) {
        this.user = user;
        this.advertisement = advertisement;
        this.value = value;
        this.state = state;
    }
}