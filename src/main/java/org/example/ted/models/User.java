package org.example.ted.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "surname", length = 45)
    private String surname;

    @Column(name = "phone_number", length = 45)
    private String phoneNumber;

    @ColumnDefault("userPhoto.jpg")
    @Column(name = "image_path", length = 1024)
    private String imagePath;

    @ManyToMany(mappedBy = "request_users")
    @JsonIgnoreProperties({"request_users", "createdBy"})
    @JsonIgnore
    private Set<Advertisement> advertisement_requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user", "connection_user"})
    private Set<Connection> connect = new LinkedHashSet<>();

    @OneToMany(mappedBy = "connection_user")
    @JsonIgnoreProperties({"connection_user", "user"})
    private Set<Connection> connected = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnoreProperties("receiver")
    @JsonIgnore
    private Set<Notification> received_notification = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender")
    @JsonIgnoreProperties("sender")
    @JsonIgnore
    private Set<Notification> sent_notification = new LinkedHashSet<>();

    @OneToOne(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Profile profile;

    @OneToMany(mappedBy = "sender")
    @JsonIgnoreProperties({"sender", "receiver"})
    @JsonIgnore
    private Set<Request> sent_requests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnoreProperties({"receiver", "sender"})
    @JsonIgnore
    private Set<Request> received_requests = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties({"createdBy", "request_users"})
    private Set<Advertisement> advertisements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties("createdBy")
    private Set<Article> articles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties({"createdBy", "article"})
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnoreProperties({"createdBy", "article"})
    private Set<Like> likes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private Set<UserAdvertisementRating> userAdvertisementRatings = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "user_skills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnoreProperties("users")
    private Set<Skill> skills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private Set<UserArticleRating> userArticleRatings = new LinkedHashSet<>();

    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name, String surname, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}